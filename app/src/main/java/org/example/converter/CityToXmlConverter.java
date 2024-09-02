package org.example.converter;

import org.example.model.City;
public class CityToXmlConverter implements CityXmlConverter {

    @Override
    public String toXml(City city) {
        if (city.getCoords() == null) {
            return "<city>\n" +
                    "    <slug>" + city.getSlug() + "</slug>\n" +
                    "    <coords>\n" +
                    "        <lat></lat>\n" +
                    "        <lon></lon>\n" +
                    "    </coords>\n" +
                    "</city>";
        }

        return "<city>\n" +
                "    <slug>" + city.getSlug() + "</slug>\n" +
                "    <coords>\n" +
                "        <lat>" + city.getCoords().getLat() + "</lat>\n" +
                "        <lon>" + city.getCoords().getLon() + "</lon>\n" +
                "    </coords>\n" +
                "</city>";
    }
}