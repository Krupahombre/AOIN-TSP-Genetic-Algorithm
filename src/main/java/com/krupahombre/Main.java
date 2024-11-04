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

        /// Compare algorithms
        System.out.println("\nAlgorithms comparison");
        instanceMap.forEach((key, value) -> algorithmComparisonTest(
                key,
                basePath,
                value,
                "greedy",
                "tournament",
                "pmx",
                "inverse"
        ));

        /// GA for all tsp files
        System.out.println("\nGA for all tsp");
        instanceMap.forEach((key, value) -> geneticAlgorithmTest(
                key,
                basePath,
                value,
                "greedy",
                "tournament",
                "pmx",
                "inverse"
        ));

        /// GA parameters test
        System.out.println("\nGA configuration parameters comparison");
        configurationModificationTest(instanceMap, basePath);

        /// GA numeric parameter test
        numericParametersTest(instanceMap, basePath);
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
                    mutationStrategy, false), bestKnown, csvWriterFinal);
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

    private static void configurationModificationTest(Map<String, Integer> instanceMap, String basePath) {
        String defaultInitPopulation = "greedy";
        String defaultSelection = "tournament";
        String defaultCrossover = "pmx";
        String defaultMutation = "inverse";

        String[] initPopulationOptions = {"greedy", "random"};
        String[] selectionOptions = {"tournament", "roulette"};
        String[] crossoverOptions = {"pmx", "ox"};
        String[] mutationOptions = {"inverse", "swap"};

        String[] finalHeaders = {"Compare", "Optimal", "Best", "Worst", "Average", "Standard Deviation"};

        CSVWriterHelper csvWriterFinal1 = new CSVWriterHelper("initPopTest.csv");
        for (String initPopulation : initPopulationOptions) {
            instanceMap.forEach((key, value) -> {
                TSPParser parser = new TSPParser();
                String[] filenameHeader = {key};
                String[] typeHeader = {initPopulation};
                String filePath = basePath + key;

                try {
                    parser.parse(filePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                List<City> cities = parser.getCities();
                csvWriterFinal1.writeToCsvFinalResults(filenameHeader);
                csvWriterFinal1.writeToCsvFinalResults(typeHeader);
                csvWriterFinal1.writeToCsvFinalResults(finalHeaders);

                executeAndLogResults("greedy-random", GeneticAlgorithm.executeGA(
                        parser.getDistanceMatrix(),
                        cities.size(),
                        initPopulation,
                        defaultSelection,
                        defaultCrossover,
                        defaultMutation, false), value, csvWriterFinal1);
            });
        }

        CSVWriterHelper csvWriterFinal2 = new CSVWriterHelper("selectionTest.csv");
        for (String selection : selectionOptions) {
            instanceMap.forEach((key, value) -> {
                TSPParser parser = new TSPParser();
                String[] filenameHeader = {key};
                String[] typeHeader = {selection};
                String filePath = basePath + key;

                try {
                    parser.parse(filePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                List<City> cities = parser.getCities();
                csvWriterFinal2.writeToCsvFinalResults(filenameHeader);
                csvWriterFinal2.writeToCsvFinalResults(typeHeader);
                csvWriterFinal2.writeToCsvFinalResults(finalHeaders);

                executeAndLogResults("tournament-roulette", GeneticAlgorithm.executeGA(
                        parser.getDistanceMatrix(),
                        cities.size(),
                        defaultInitPopulation,
                        selection,
                        defaultCrossover,
                        defaultMutation, false), value, csvWriterFinal2);
            });
        }

        CSVWriterHelper csvWriterFinal3 = new CSVWriterHelper("crossoverTest.csv");
        for (String crossover : crossoverOptions) {
            instanceMap.forEach((key, value) -> {
                TSPParser parser = new TSPParser();
                String[] filenameHeader = {key};
                String[] typeHeader = {crossover};
                String filePath = basePath + key;

                try {
                    parser.parse(filePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                List<City> cities = parser.getCities();
                csvWriterFinal3.writeToCsvFinalResults(filenameHeader);
                csvWriterFinal3.writeToCsvFinalResults(typeHeader);
                csvWriterFinal3.writeToCsvFinalResults(finalHeaders);

                executeAndLogResults("pmx-cx", GeneticAlgorithm.executeGA(
                        parser.getDistanceMatrix(),
                        cities.size(),
                        defaultInitPopulation,
                        defaultSelection,
                        crossover,
                        defaultMutation, false), value, csvWriterFinal3);
            });
        }

        CSVWriterHelper csvWriterFinal4 = new CSVWriterHelper("mutationTest.csv");
        for (String mutation : mutationOptions) {
            instanceMap.forEach((key, value) -> {
                TSPParser parser = new TSPParser();
                String[] filenameHeader = {key};
                String[] typeHeader = {mutation};
                String filePath = basePath + key;

                try {
                    parser.parse(filePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                List<City> cities = parser.getCities();
                csvWriterFinal4.writeToCsvFinalResults(filenameHeader);
                csvWriterFinal4.writeToCsvFinalResults(typeHeader);
                csvWriterFinal4.writeToCsvFinalResults(finalHeaders);

                executeAndLogResults("inverse-swap", GeneticAlgorithm.executeGA(
                        parser.getDistanceMatrix(),
                        cities.size(),
                        defaultInitPopulation,
                        defaultSelection,
                        defaultCrossover,
                        mutation, false), value, csvWriterFinal4);
            });
        }
    }

    private static void numericParametersTest(Map<String, Integer> instanceMap, String basePath) {
        System.out.println("Numeric Parameters Test");
        Integer popSizeDef = 100;
        Integer generationsDef = 100;
        Double crossoverProbabilityDef = 0.7;
        Double mutationProbabilityDef = 0.1;
        Integer tournamentSizeDef = 5;

        Integer[] popSizeOptions = {100, 1000, 3000};
        Integer[] generationsOptions = {100, 1000, 3000};
        Double[] crossoverProbabilityOptions = {0.2, 0.4, 0.7};
        Double[] mutationProbabilityOptions = {0.1, 0.3, 0.5};
        Integer[] tournamentSizeOptions = {3, 5, 7};

        String[] finalHeaders = {"Compare", "Optimal", "Best", "Worst", "Average", "Standard Deviation"};

        CSVWriterHelper csvWriterFinal1 = new CSVWriterHelper("initPopTest.csv");
        for (Integer popSize : popSizeOptions) {
            instanceMap.forEach((key, value) -> {
                TSPParser parser = new TSPParser();
                String[] filenameHeader = {key};
                String[] typeHeader = {"Pop Size" + popSize};
                String filePath = basePath + key;

                try {
                    parser.parse(filePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                List<City> cities = parser.getCities();
                csvWriterFinal1.writeToCsvFinalResults(filenameHeader);
                csvWriterFinal1.writeToCsvFinalResults(typeHeader);
                csvWriterFinal1.writeToCsvFinalResults(finalHeaders);

                executeAndLogResults("popsize", GeneticAlgorithm.executeGACustomNumeric(
                        parser.getDistanceMatrix(),
                        cities.size(),
                        popSize,
                        generationsDef,
                        crossoverProbabilityDef,
                        mutationProbabilityDef,
                        tournamentSizeDef, false), value, csvWriterFinal1);
            });
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