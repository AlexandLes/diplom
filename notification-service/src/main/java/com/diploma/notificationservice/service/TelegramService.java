package com.diploma.notificationservice.service;

import com.diploma.notificationservice.config.BotConfig;
import com.diploma.notificationservice.dto.OrderLineItemsDto;
import com.diploma.notificationservice.dto.OrderRequestDto;
import com.diploma.notificationservice.dto.OrderResponseDto;
import com.diploma.notificationservice.dto.PaymentRequestDto;
import com.diploma.notificationservice.util.FileUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class TelegramService extends TelegramLongPollingBot
        implements NotificationService{
    private static final String ORDER_URL = "http://localhost:8080/api/order";
    private static final String PAYMENT_URL = "http://localhost:8080/api/payment";
    final RestTemplate restTemplate = new RestTemplate();
    private final BotConfig botConfig;

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            if (matchPattern(messageText)) {
                OrderResponseDto order = restTemplate.getForEntity(ORDER_URL +"?userEmail=" + messageText, OrderResponseDto.class).getBody();
                order.setChatId(update.getMessage().getChatId());
                OrderRequestDto orderRequestDto = new OrderRequestDto(order);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<OrderRequestDto> requestEntity = new HttpEntity<>(orderRequestDto, headers);

                ResponseEntity<OrderResponseDto> response = restTemplate.exchange(ORDER_URL, HttpMethod.PUT, requestEntity, OrderResponseDto.class);
                try {
                    sendMessage(update.getMessage().getChatId().toString(), FileUtil.readFileAsString("instruct.txt"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                OrderLineItemsDto orderLineItemsDto = order.getOrderLineItemsDtoList().get(0);
                BigDecimal fee = orderLineItemsDto.getDailyFee().multiply(BigDecimal.valueOf(ChronoUnit.DAYS.between(order.getDateOfRental(), order.getDateOfReturn())));
                headers.setContentType(MediaType.APPLICATION_JSON);
                PaymentRequestDto paymentRequestDto = new PaymentRequestDto(fee.longValueExact(), order.getOrderNumber(), LocalDate.now());

                HttpEntity<PaymentRequestDto> dtoRequestEntity = new HttpEntity<>(paymentRequestDto, headers);

                ResponseEntity<String> paymentHtml = restTemplate.postForEntity(PAYMENT_URL, dtoRequestEntity , String.class);
                sendMessage(update.getMessage().getChatId().toString(), "Посилання на оплату: " + paymentHtml.getBody());


                return;
            }

            switch (messageText) {
                case "/start":
                    startCommandReceived(String.valueOf(update.getMessage().getChatId()),
                            update.getMessage().getChat().getFirstName());
                    break;
                case "Здати":
                    OrderResponseDto order = restTemplate.getForEntity(ORDER_URL +"/close?chatId=" + update.getMessage().getChatId(), OrderResponseDto.class).getBody();
                    closeOrderAndUpdate(update, order);
                    processPayment(update, order);
                    break;

                default:
                    sendMessage(String.valueOf(update.getMessage().getChatId()), "unknown command");
            }
        }
    }
    private void closeOrderAndUpdate(Update update, OrderResponseDto order) {
        order.setChatId(update.getMessage().getChatId());
        OrderRequestDto orderRequestDto = new OrderRequestDto(order);
        orderRequestDto.setActive(false);
        orderRequestDto.setActualDateOfReturn(LocalDate.now());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderRequestDto> requestEntity = new HttpEntity<>(orderRequestDto, headers);
        ResponseEntity<OrderResponseDto> response = restTemplate.exchange(ORDER_URL + "/close", HttpMethod.PUT, requestEntity, OrderResponseDto.class);
    }

    // Метод для обработки задолженности
    private void processPayment(Update update, OrderResponseDto order) {
        OrderLineItemsDto orderLineItemsDto = order.getOrderLineItemsDtoList().get(0);
        BigDecimal fee = orderLineItemsDto.getDailyFee().multiply(BigDecimal.valueOf(ChronoUnit.DAYS.between(order.getDateOfReturn(), LocalDate.now())));
        if (fee.longValueExact() > 0) {
            PaymentRequestDto paymentRequestDto = new PaymentRequestDto(fee.longValueExact(), order.getOrderNumber() + "fee", LocalDate.now());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PaymentRequestDto> dtoRequestEntity = new HttpEntity<>(paymentRequestDto, headers);
            ResponseEntity<String> paymentHtml = restTemplate.postForEntity(PAYMENT_URL, dtoRequestEntity, String.class);
            sendMessage(update.getMessage().getChatId().toString(), "Cплатіть борг за невчасну здачу: " + paymentHtml.getBody());
        } else {
            sendMessage(update.getMessage().getChatId().toString(), "Дякуємо за користування!");
        }
    }

    private void startCommandReceived(String chatId, String name) {
        String answer = "Добрий день, " + name + ", раді бачити вас!";
        sendMessage(chatId, answer);
    }

    public void sendMessage(String chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can`t send message due to error occured: ", e);
        }
    }

    /*@Override
    public void sendMessageAboutNewRental(RentalResponseDto rental) {
        User userById = userService.findById(rental.getUserId());
        if (userById.getChatId() != null) {
            sendMessage(userById.getChatId(),
                    "New rental was added with ID: "
                            + rental.getId() + "\n"
                            + "Car brand:" + carService.findById(rental.getCarId())
                            .getBrand() + "\n"
                            + "Rental date: " + rental.getRentalDate() + "\n"
                            + "Return date: " + rental.getReturnDate());
        }
    }*/

    private boolean matchPattern(String email) {
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Scheduled(cron = "2 * * * * ?")
    public void notifyAllUsersWhereActualReturnDateIsAfterReturnDate() {
        List<OrderResponseDto> orders = Arrays.stream(restTemplate.getForEntity(ORDER_URL + "/remind?tomorrow=" + LocalDate.now().plusDays(1), OrderResponseDto[].class)
                        .getBody())
                .toList();
        for (OrderResponseDto order:orders) {
            sendMessage(order.getChatId().toString(), "Оренда №" + order.getOrderNumber() + " закінчується завтра! Будь-ласка, при виборі ділянки паркування перегляньте всі доступні у вашій зоні");
        }
    }
}
