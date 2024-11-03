package com.krupahombre;

import com.krupahombre.algorithms.GeneticAlgorithm;
import com.krupahombre.algorithms.GreedyAlgorithm;
import com.krupahombre.algorithms.RandomAlgorithm;
import com.krupahombre.algorithms.utils.Path;
import com.krupahombre.helpers.CSVWriterHelper;
import com.krupahombre.helpers.City;
import com.krupahombre.helpers.TSPParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        Map<String, Integer> instanceMap = new HashMap<>(Map.of(
                "berlin52.tsp", 7542,
                "kroA100.tsp", 21282,
                "kroA150.tsp", 26524,
                "kroA200.tsp", 29368,
                "ali535.tsp", 202339,
                "gr666.tsp", 294358
        ));

        String basePath = "src/main/resources/";
//        String initPopulationStrategy = "greedy"; //random
//        String selectionStrategy = "tournament";  //roulette
//        String crossoverStrategy = "pmx"; //cx
//        String mutationStrategy = "inverse"; //inverse

        /// Compare algorithms
        System.out.println("\nAlgorithms comparison");
//        instanceMap.forEach((key, value) -> algorithmComparisonTest(
//                key,
//                basePath,
//                value,
//                "greedy",
//                "tournament",
//                "pmx",
//                "inverse"
//        ));

        /// GA for all tsp files
        System.out.println("\nGA for all tsp");
//        instanceMap.forEach((key, value) -> geneticAlgorithmTest(
//                key,
//                basePath,
//                value,
//                "greedy",
//                "tournament",
//                "pmx",
//                "inverse"
//        ));

        /// GA parameters test
//        parametersGAComparisonTest(instanceMap);
        configurationModificationTest(instanceMap);
    }

    private static void algorithmComparisonTest(String filename,
                                                String basePath,
                                                Integer bestKnown,
                                                String initPopulationStrategy,
                                                String selectionStrategy,
                                                String crossoverStrategy,
                                                String mutationStrategy) {
        TSPParser parser = new TSPParser();
        CSVWriterHelper csvWriterFinal = new CSVWriterHelper("algorithmComparison.csv");
        String[] finalHeaders = {"Alg. Type", "Optimal", "Best", "Worst", "Average", "Standard Deviation"};
        String[] filenameHeader = {filename};
        String filePath = basePath + filename;

        try {
            parser.parse(filePath);
            List<City> cities = parser.getCities();
            csvWriterFinal.writeToCsvFinalResults(filenameHeader);
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

    private static void geneticAlgorithmTest(String filename,
                                             String basePath,
                                             Integer bestKnown,
                                             String initPopulationStrategy,
                                             String selectionStrategy,
                                             String crossoverStrategy,
                                             String mutationStrategy) {
        TSPParser parser = new TSPParser();
        CSVWriterHelper csvWriterFinal = new CSVWriterHelper(filename + ".csv");
        String[] finalHeaders = {"Generation", "Best", "Worst", "Average"};
        String filePath = basePath + filename;

        int popSize = 100;
        int generations = 100;
        double crossoverProbability = 0.7;
        double mutationProbability = 0.1;
        int tournamentSize = 5;

        try {
            parser.parse(filePath);
            List<City> cities = parser.getCities();
            csvWriterFinal.writeToCsvFinalResults(finalHeaders);

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
                    mutationStrategy,
                    csvWriterFinal
            );

            System.out.println("Best found solution: " + bestFoundSolution);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void parametersGAComparisonTest(Map<String, Integer> instanceMap) {
        String[] initPopulationOptions = {"greedy", "random"};
        String[] selectionOptions = {"tournament", "roulette"};
        String[] crossoverOptions = {"pmx", "cx"};
        String[] mutationOptions = {"inverse", "swap"};

        int combinationNumber = 1;
        for (String initPopulationStrategy : initPopulationOptions) {
            for (String selectionStrategy : selectionOptions) {
                for (String crossoverStrategy : crossoverOptions) {
                    for (String mutationStrategy : mutationOptions) {
                        System.out.println(combinationNumber + ". " +
                                initPopulationStrategy + ", " +
                                selectionStrategy + ", " +
                                crossoverStrategy + ", " +
                                mutationStrategy);
                        combinationNumber++;
                    }
                }
            }
        }
//        TSPParser parser = new TSPParser();
//        CSVWriterHelper csvWriterFinal = new CSVWriterHelper("parametersComparison.csv");
    }

    private static void configurationModificationTest(Map<String, Integer> instanceMap) {
        String defaultInitPopulation = "greedy";
        String defaultSelection = "tournament";
        String defaultCrossover = "pmx";
        String defaultMutation = "inverse";

        String altInitPopulation = "random";
        String altSelection = "roulette";
        String altCrossover = "cx";
        String altMutation = "swap";

        int popSize = 100;
        int generations = 100;
        double crossoverProbability = 0.7;
        double mutationProbability = 0.1;
        int tournamentSize = 5;

        TSPParser parser = new TSPParser();
        CSVWriterHelper csvWriterFinal = new CSVWriterHelper("algorithmComparison.csv");
        String[] finalHeaders = {"Optimal", "Best", "Worst", "Average", "Standard Deviation"};
        String[] filenameHeader = {filename};
        String filePath = basePath + filename;
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