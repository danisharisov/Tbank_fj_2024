package org.example.model;

import lombok.Data;

@Data
public class City {
    private String slug;
    private Coordinates coords;

    @Data
    public static class Coordinates {
        private double lat;
        private double lon;
    }
}