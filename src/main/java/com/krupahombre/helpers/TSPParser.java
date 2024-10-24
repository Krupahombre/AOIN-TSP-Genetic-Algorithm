package com.krupahombre.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSPParser {
    private final List<City> cities;
    private int[][] distanceMatrix;
    private String edgeWeightType;

    public TSPParser() {
        this.cities = new ArrayList<>();
    }

    public int[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public void parse(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("EDGE_WEIGHT_TYPE")) {
                this.edgeWeightType = line.split(":")[1].trim();
            }
            if (line.startsWith("NODE_COORD_SECTION")) {
                break;
            }
        }

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("EOF")) {
                break;
            }
            String[] parts = line.split("\\s+");
            if (parts.length >= 3) {
                int id = Integer.parseInt(parts[0]);
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                cities.add(new City(id, x, y));
            }
        }

        int size = cities.size();
        distanceMatrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    if ("EUC_2D".equals(edgeWeightType)) {
                        distanceMatrix[i][j] = cities.get(i).distanceToEUC_2D(cities.get(j));
                    } else if ("GEO".equals(edgeWeightType)) {
                        distanceMatrix[i][j] = cities.get(i).distanceToGEO(cities.get(j));
                    }
                } else {
                    distanceMatrix[i][j] = 0;
                }
            }
        }

        reader.close();
    }

    public List<City> getCities() {
        return cities;
    }
}
