package com.diploma.carrentalservice;

import com.diploma.carrentalservice.dto.request.CarRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.math.BigDecimal;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class CarRentalServiceApplicationTests {
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.8");

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}
	@Test
	void shouldCreateCar() throws Exception {
		CarRequestDto carRequestDto = getCarRequest();
		String carRequestString = objectMapper.writeValueAsString(carRequestDto);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/car")
				.contentType(MediaType.APPLICATION_JSON)
				.content(carRequestString)).andExpect(MockMvcResultMatchers.status().isCreated());
	}

	private CarRequestDto getCarRequest() {
		return CarRequestDto.builder()
				.model("ass")
				.brand("far")
				.dailyFee(BigDecimal.valueOf(1300))
				.build();
	}

}
