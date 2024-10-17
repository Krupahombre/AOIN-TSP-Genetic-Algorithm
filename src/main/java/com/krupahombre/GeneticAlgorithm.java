package com.krupahombre;

import java.util.*;
import java.util.stream.IntStream;

public class GeneticAlgorithm {
    private List<List<Integer>> population;
    private final int[][] distanceMatrix;
    private final int popSize;
    private final int generations;
    private final double crossoverProbability;
    private final double mutationProbability;
    private final int tournamentSize;
    private final Random random;

    public GeneticAlgorithm(int[][] distanceMatrix, int popSize, int generations, double crossoverProbability, double mutationProbability, int tournamentSize) {
        this.distanceMatrix = distanceMatrix;
        this.popSize = popSize;
        this.generations = generations;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        this.tournamentSize = tournamentSize;
        this.random = new Random();
        this.population = new ArrayList<>();
    }

    private int evaluate(List<Integer> tour) {
        int totalDistance = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            totalDistance += distanceMatrix[tour.get(i)][tour.get(i + 1)];
        }
        totalDistance += distanceMatrix[tour.getLast()][tour.getFirst()];
        return totalDistance;
    }

    private void initializePopulation(int citiesNumber, String initPopulationStrategy) {
        switch (initPopulationStrategy.toLowerCase()) {
            case "random":
                for (int i = 0; i < popSize; i++) {
                    population.add(randomTour(citiesNumber));
                }
                break;
            case "greedy":
                for (int i = 0; i < popSize; i++) {
                    population.add(greedyTour(citiesNumber));
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid population type: " + initPopulationStrategy);
        }
    }

    private List<Integer> selectIndividual(String selectionStrategy) {
        return switch (selectionStrategy.toLowerCase()) {
            case "tournament" -> tournamentSelection();
            case "roulette" -> rouletteSelection();
            default -> throw new IllegalArgumentException("Invalid mutation strategy: " + selectionStrategy);
        };
    }

    private List<Integer> crossoverIndividuals(String crossoverStrategy, List<Integer> parent1, List<Integer> parent2) {
        if (random.nextDouble() < crossoverProbability) {
            return switch (crossoverStrategy.toLowerCase()) {
                case "ox" -> orderedCrossover(parent1, parent2);
                case "pmx" -> partiallyMappedCrossover(parent1, parent2);
                default -> throw new IllegalArgumentException("Invalid mutation strategy: " + crossoverStrategy);
            };
        } else {
            return parent1;
        }
    }

    private void mutateIndividual(String mutationStrategy, List<Integer> individual) {
        if (random.nextDouble() < mutationProbability) {
            switch (mutationStrategy.toLowerCase()) {
                case "swap" -> swapMutation(individual);
                case "inverse" -> inverseMutation(individual);
                default -> throw new IllegalArgumentException("Invalid mutation strategy: " + mutationStrategy);
            }
        }
    }

    /// INITIALIZATION
    public List<Integer> greedyTour(int citiesNumber) {
        List<Integer> tour = new ArrayList<>();
        boolean[] visited = new boolean[citiesNumber];

        int currentCity = random.nextInt(citiesNumber);
        tour.add(currentCity);
        visited[currentCity] = true;

        for (int i = 1; i < citiesNumber; i++) {
            int nearestCity = Integer.MIN_VALUE;
            int shortestDistance = Integer.MAX_VALUE;

            for (int nextCity = 0; nextCity < citiesNumber; nextCity++) {
                if (!visited[nextCity] && distanceMatrix[currentCity][nextCity] < shortestDistance) {
                    shortestDistance = distanceMatrix[currentCity][nextCity];
                    nearestCity = nextCity;
                }
            }

            tour.add(nearestCity);
            visited[nearestCity] = true;
            currentCity = nearestCity;
        }

        return tour;
    }

    public List<Integer> randomTour(int citiesNumber) {
        List<Integer> tour = new ArrayList<>(IntStream.rangeClosed(0, citiesNumber - 1)
                .boxed()
                .toList());

        Collections.shuffle(tour);

        return tour;
    }

    /// SELECTION
    private List<Integer> tournamentSelection() {
        List<List<Integer>> tournament = new ArrayList<>();
        for (int i = 0; i < tournamentSize; i++) {
            tournament.add(population.get(random.nextInt(popSize)));
        }

        return tournament.stream().min(Comparator.comparingInt(this::evaluate)).orElse(null);
    }

    private List<Integer> rouletteSelection() {
        List<Double> fitness = new ArrayList<>();
        double totalFitness = 0.0;

        for (List<Integer> individual : population) {
            double fitValue = 1.0 / evaluate(individual);
            fitness.add(fitValue);
            totalFitness += fitValue;
        }

        double randValue = random.nextDouble() * totalFitness;

        double cumulativeFitness = 0.0;
        for (int i = 0; i < population.size(); i++) {
            cumulativeFitness += fitness.get(i);
            if (cumulativeFitness >= randValue) {
                return population.get(i);
            }
        }

        throw new IllegalStateException("No Winner found");
    }

    /// CROSSOVER
    private List<Integer> orderedCrossover(List<Integer> parent1, List<Integer> parent2) {
        int size = parent1.size();
        int start = random.nextInt(size);
        int end = random.nextInt(size - start) + start;

        List<Integer> child = new ArrayList<>(Collections.nCopies(size, -1));
        Set<Integer> excludedCities = new HashSet<>();

        for (int i = start; i < end; i++) {
            child.set(i, parent1.get(i));
            excludedCities.add(parent1.get(i));
        }

        int parent2Idx = 0;
        for (int childIdx = 0; childIdx < size; childIdx++) {
            if (child.get(childIdx) == -1) {
                while (excludedCities.contains(parent2.get(parent2Idx))) {
                    parent2Idx = (parent2Idx + 1) % size;
                }
                child.set(childIdx, parent2.get(parent2Idx));
                excludedCities.add(parent2.get(parent2Idx));
                parent2Idx++;
            }
        }

        return child;
    }

    private List<Integer> partiallyMappedCrossover(List<Integer> parent1, List<Integer> parent2) {
        return null;
    }

    /// MUTATION
    private void swapMutation(List<Integer> individual) {
        int index1 = random.nextInt(individual.size());
        int index2 = random.nextInt(individual.size());
        Collections.swap(individual, index1, index2);
    }

    private void inverseMutation(List<Integer> individual) {
        int start = random.nextInt(individual.size());
        int end = random.nextInt(individual.size() - start) + start;
        while (start < end) {
            Collections.swap(individual, start, end);
            start++;
            end--;
        }
    }

    /// STARTING POINT
    public Integer run(
            int citiesNumber,
            String initPopulationStrategy,
            String selectionStrategy,
            String crossoverStrategy,
            String mutationStrategy
    ) {
        initializePopulation(citiesNumber, initPopulationStrategy);

        List<Integer> bestPath = population.getFirst();
        int bestSolution = evaluate(bestPath);

        for (int generation = 0; generation < generations; generation++) {
            List<List<Integer>> newPopulation = new ArrayList<>();

            while (newPopulation.size() < popSize) {
                List<Integer> parent1 = selectIndividual(selectionStrategy);
                List<Integer> parent2 = selectIndividual(selectionStrategy);

                List<Integer> offspring;
                offspring = crossoverIndividuals(crossoverStrategy, parent1, parent2);
                mutateIndividual(mutationStrategy, offspring);
                newPopulation.add(offspring);

                int offspringSolution = evaluate(offspring);
                if (offspringSolution < bestSolution) {
                    bestPath = offspring;
                    bestSolution = offspringSolution;
                }
            }

            population = newPopulation;

            System.out.println("Generation " + generation + " Best distance: " + bestSolution);
        }

        System.out.println("\nBest path: " + bestPath);
        System.out.println("Best solution: " + bestSolution);
        System.out.println("\n");

        return bestSolution;
    }
}
