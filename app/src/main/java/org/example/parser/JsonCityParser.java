package org.example.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class JsonCityParser implements CityParser {
    private static final Logger logger = LoggerFactory.getLogger(JsonCityParser.class);

    @Override
    public City parse(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            logger.info("Начинается парсинг JSON из файла: " + filePath);
            return objectMapper.readValue(new File(filePath), City.class);
        } catch (IOException e) {
            logger.error("Ошибка при чтении или парсинге JSON-файла: " + e.getMessage());
            return null;
        }
    }
}