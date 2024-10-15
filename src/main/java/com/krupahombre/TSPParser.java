package com.krupahombre;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSPParser {
    private List<City> cities;
    private double[][] distanceMatrix;

    public TSPParser() {
        this.cities = new ArrayList<>();
    }

    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public void parse(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("NODE_COORD_SECTION")) {
                break;
            }
        }

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("EOF")) {
                break;
            }
            String[] parts = line.split(" ");
            if (parts.length >= 3) {
                int id = Integer.parseInt(parts[0]);
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                cities.add(new City(id, x, y));
            }
        }

        int size = cities.size();
        distanceMatrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    distanceMatrix[i][j] = cities.get(i).distanceTo(cities.get(j));
                } else {
                    distanceMatrix[i][j] = 0.0;
                }
            }
        }

        reader.close();
    }

    public List<City> getCities() {
        return cities;
    }

    public double getDistance(int city1, int city2) {
        return distanceMatrix[city1 - 1][city2 - 1];
    }
}
