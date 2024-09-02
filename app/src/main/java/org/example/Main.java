package org.example;

import org.example.converter.*;
import org.example.model.*;
import org.example.parser.*;
import org.example.saver.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Запуск программы");

        CityParser parser = new JsonCityParser();
        CityXmlConverter converter = new CityToXmlConverter();
        FileSaver fileSaver = new FileSaverImpl();

        processFile(parser, converter, fileSaver, "city.json", "city1.xml");
        processFile(parser, converter, fileSaver, "city-error.json", "city2.xml");

        logger.info("Программа завершена успешно");
    }

    private static void processFile(CityParser parser, CityXmlConverter converter, FileSaver fileSaver, String inputFilePath, String outputFilePath) {
        City city = parser.parse(inputFilePath);
        if (city != null) {
            String xml = converter.toXml(city);
            fileSaver.save(xml, outputFilePath);
            logger.info("Обработка файла " + inputFilePath + " завершена успешно");
        } else {
            logger.warn("Не удалось обработать файл " + inputFilePath);
        }
    }
}