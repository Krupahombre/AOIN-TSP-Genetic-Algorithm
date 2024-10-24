package com.krupahombre;

import com.krupahombre.algorithms.GeneticAlgorithm;
import com.krupahombre.algorithms.GreedyAlgorithm;
import com.krupahombre.algorithms.RandomAlgorithm;
import com.krupahombre.algorithms.utils.Path;
import com.krupahombre.helpers.City;
import com.krupahombre.helpers.TSPParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static double calculatePercentageToOptimal(Integer optimalValue, Integer calculatedValue) {
        double difference = calculatedValue - optimalValue;

        return (difference / optimalValue.doubleValue()) * 100;
    }

    public static void main(String[] args) throws IOException {
        String filename = "src/main/resources/berlin52.tsp";
        String initPopulationStrategy = "greedy"; //greedy
        String selectionStrategy = "tournament";  //roulette
        String crossoverStrategy = "pmx"; //cx
        String mutationStrategy = "inverse"; //inverse
        Integer bestKnown = 7542; //berlin

        TSPParser parser = new TSPParser();

        try {
            parser.parse(filename);
            List<City> cities = parser.getCities();

            Path randomData = RandomAlgorithm.execute(parser.getDistanceMatrix());
            Path greedyData = GreedyAlgorithm.execute(parser.getDistanceMatrix());

            System.out.println("Random Data: " + randomData.getBestCost() + " " + randomData.getWorstCost());
            System.out.println("Greedy Data: " + greedyData.getBestCost() + " " + greedyData.getWorstCost());

//            int popSize = 500;
//            int generations = 3000;
//            double crossoverProbability = 0.7;
//            double mutationProbability = 0.1;
//            int tournamentSize = 100;
//
//            GeneticAlgorithm ga = new GeneticAlgorithm(
//                    parser.getDistanceMatrix(),
//                    popSize,
//                    generations,
//                    crossoverProbability,
//                    mutationProbability,
//                    tournamentSize
//            );
//            Integer bestFoundSolution = ga.run(
//                    cities.size(),
//                    initPopulationStrategy,
//                    selectionStrategy,
//                    crossoverStrategy,
//                    mutationStrategy
//            );
//
//            System.out.println("\n" + calculatePercentageToOptimal(bestKnown, bestFoundSolution));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}