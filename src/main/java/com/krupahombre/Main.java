package com.krupahombre;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String filename = "src/main/resources/berlin52.tsp";

        TSPParser parser = new TSPParser();

        try {
            parser.parse(filename);
            List<City> cities = parser.getCities();

            System.out.println("Wczytane miasta:");
            for (City city : cities) {
                System.out.println(city);
            }

            System.out.println(Arrays.deepToString(parser.getDistanceMatrix()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}