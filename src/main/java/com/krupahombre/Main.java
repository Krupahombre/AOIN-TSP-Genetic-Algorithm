package com.krupahombre;

import java.io.IOException;
import java.util.List;

public class Main {
    public static double calculatePercentageToOptimal(Integer optimalValue, Integer calculatedValue) {
        double difference = calculatedValue - optimalValue;

        return (difference / optimalValue.doubleValue()) * 100;
    }

    public static void main(String[] args) throws IOException {
        String filename = "src/main/resources/berlin52.tsp";
        String initPopulationStrategy = "greedy";
        String selectionStrategy = "tournament";
        String crossoverStrategy = "ox";
        String mutationStrategy = "inverse";
        Integer bestKnown = 7542;

        TSPParser parser = new TSPParser();

        try {
            parser.parse(filename);
            List<City> cities = parser.getCities();

            int popSize = 1000;
            int generations = 3000;
            double crossoverProbability = 0.5;
            double mutationProbability = 0.5;
            int tournamentSize = 5;

            GeneticAlgorithm ga = new GeneticAlgorithm(
                    parser.getDistanceMatrix(),
                    popSize,
                    generations,
                    crossoverProbability,
                    mutationProbability,
                    tournamentSize
            );
            Integer bestFoundSolution = ga.run(
                    cities.size(),
                    initPopulationStrategy,
                    selectionStrategy,
                    crossoverStrategy,
                    mutationStrategy
            );

            System.out.println("\n" + calculatePercentageToOptimal(bestKnown, bestFoundSolution));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}