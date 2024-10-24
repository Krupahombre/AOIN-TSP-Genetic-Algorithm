package com.krupahombre;

import com.krupahombre.algorithms.GeneticAlgorithm;
import com.krupahombre.algorithms.GreedyAlgorithm;
import com.krupahombre.algorithms.RandomAlgorithm;
import com.krupahombre.algorithms.utils.Path;
import com.krupahombre.helpers.City;
import com.krupahombre.helpers.TSPParser;

import java.io.IOException;
import java.util.List;

public class Main {
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
            Path gaData = GeneticAlgorithm.executeGA(
                    parser.getDistanceMatrix(),
                    cities.size(),
                    initPopulationStrategy,
                    selectionStrategy,
                    crossoverStrategy,
                    mutationStrategy
            );

            System.out.println("Random Data: " + randomData.toString());
            System.out.println("Greedy Data: " + greedyData.toString());
            System.out.println("GA Data: " + gaData.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}