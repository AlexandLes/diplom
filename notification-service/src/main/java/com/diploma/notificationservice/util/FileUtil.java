package com.diploma.notificationservice.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileUtil {
    public static String readFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = FileUtil.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName);
             Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            return scanner.useDelimiter("\\A").next();
        }
    }
}
