package org.example.parser;

import org.example.model.City;

public interface CityParser {
    City parse(String filePath);
}