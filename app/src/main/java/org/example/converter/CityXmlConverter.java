package org.example.converter;
import org.example.model.City;

public interface CityXmlConverter {
    String toXml(City city);
}