package com.krupahombre;

import com.krupahombre.algorithms.GeneticAlgorithm;
import com.krupahombre.algorithms.GreedyAlgorithm;
import com.krupahombre.algorithms.RandomAlgorithm;
import com.krupahombre.algorithms.utils.Path;
import com.krupahombre.helpers.CSVWriterHelper;
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
        CSVWriterHelper csvWriterFinal = new CSVWriterHelper("final_results.csv");
        String[] finalHeaders = {"Alg. Type", "Optimal", "Best", "Worst", "Average", "Standard Deviation"};

        try {
            parser.parse(filename);
            List<City> cities = parser.getCities();
            csvWriterFinal.writeToCsvFinalResults(finalHeaders);

            executeAndLogResults("Random", RandomAlgorithm.execute(parser.getDistanceMatrix()), bestKnown, csvWriterFinal);
            executeAndLogResults("Greedy", GreedyAlgorithm.execute(parser.getDistanceMatrix()), bestKnown, csvWriterFinal);
            executeAndLogResults("Genetic", GeneticAlgorithm.executeGA(
                    parser.getDistanceMatrix(),
                    cities.size(),
                    initPopulationStrategy,
                    selectionStrategy,
                    crossoverStrategy,
                    mutationStrategy), bestKnown, csvWriterFinal);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void executeAndLogResults(String algorithmName, Path path, Integer bestKnown, CSVWriterHelper csvWriter) {
        System.out.println(algorithmName + " Data: " + path);
        String[] dataToLog = {
                algorithmName,
                bestKnown.toString(),
                path.getBestCost().toString(),
                path.getWorstCost().toString(),
                path.getAverageCost().toString(),
                path.getStandardDeviation().toString()
        };
        csvWriter.writeToCsvFinalResults(dataToLog);
    }
}