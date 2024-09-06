package org.example.converter;

import org.example.model.City;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CityToXmlConverter implements CityXmlConverter {
    private static final Logger logger = LoggerFactory.getLogger(CityToXmlConverter.class);
    private final XmlMapper xmlMapper;

    public CityToXmlConverter() {
        this.xmlMapper = new XmlMapper();
    }

    @Override
    public String toXml(City city) {
        try {
            logger.info("Начинается конвертация объекта City в XML");
            String xml = xmlMapper.writeValueAsString(city);
            logger.debug("Конвертация завершена успешно {}", xml);
            return xml;
        } catch (Exception e) {
            logger.error("Ошибка при конвертации объекта City в XML", e);
            return null;
        }
    }

}
