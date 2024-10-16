package com.krupahombre;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String filename = "src/main/resources/ulysses16.tsp";
        Integer bestSolution = 6859;
        String initPopulationStrategy = "greedy";

        TSPParser parser = new TSPParser();

        try {
            parser.parse(filename);
            List<City> cities = parser.getCities();

            int popSize = 100;
            int generations = 10;
            double crossoverProbability = 0.7;
            double mutationProbability = 0.1;
            int tournamentSize = 5;

            GeneticAlgorithm ga = new GeneticAlgorithm(parser.getDistanceMatrix(), popSize, generations, crossoverProbability, mutationProbability, tournamentSize);
            Integer bestFoundSolution = ga.run(cities.size(), initPopulationStrategy);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}