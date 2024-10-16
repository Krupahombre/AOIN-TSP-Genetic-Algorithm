package com.krupahombre;

import java.util.*;
import java.util.stream.IntStream;

public class GeneticAlgorithm {
    private List<List<Integer>> population;
    private int[][] distanceMatrix;
    private int popSize;
    private int generations;
    private double crossoverRate;
    private double mutationRate;
    private int tournamentSize;
    private Random random;

    public GeneticAlgorithm(int[][] distanceMatrix, int popSize, int generations, double crossoverRate, double mutationRate, int tournamentSize) {
        this.distanceMatrix = distanceMatrix;
        this.popSize = popSize;
        this.generations = generations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
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

    public Integer run(int citiesNumber, String initPopulationStrategy) {
        initializePopulation(citiesNumber, initPopulationStrategy);

        for (int generation = 0; generation < generations; generation++) {
            List<List<Integer>> newPopulation = new ArrayList<>();

            for (int i = 0; i < popSize; i++) {
                break;
            }

            List<Integer> bestIndividual = population.getFirst();
            for (List<Integer> individual : population) {
                if (evaluate(individual) < evaluate(bestIndividual)) {
                    bestIndividual = individual;
                }
            }
            System.out.println("Generation " + generation + " Best distance: " + evaluate(bestIndividual));
        }

        List<Integer> bestIndividual = population.getFirst();
        for (List<Integer> individual : population) {
            if (evaluate(individual) < evaluate(bestIndividual)) {
                bestIndividual = individual;
            }
        }

        System.out.println("\nBest tour: " + bestIndividual);
        System.out.println("Best distance: " + evaluate(bestIndividual));
        return 0;
    }
}
