package com.example.livedoorwhether;

import java.util.List;

public class WeatherResponse {
    List<PinPointLocation> pinPointLocation;
    String link;
    List<Forecast> forecasts;
    Location location;
    String publicTime;
    Copyright copyright;
    String title;
    Description description;


    static class PinPointLocation {
        String link;
        String name;
    }

    static class Forecast {
        String dateLavel;
        String telop;
        Temperature temprature;
        Image image;
    }
    static class Temperature {
        TemperatureSub min;
        TemperatureSub max;

        static class TemperatureSub {
            String celsius;
            String fahrenheit;
        }
    }
    static class Image {
        int width;
        String url;
        String title;
        int height;
    }

    static class Location{
        String city;
        String area;
        String prefecture;
    }

    static class Copyright {
        List<Provider> provider;
        String link;
        String title;
        Image image;

        static class Provider {
            String link;
            String name;
        }
    }

    static class Description {
        String text;
        String publicTime;
    }

}


