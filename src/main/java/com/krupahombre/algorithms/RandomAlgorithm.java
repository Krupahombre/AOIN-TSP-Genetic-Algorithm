package com.krupahombre.algorithms;

import com.krupahombre.algorithms.utils.Path;
import com.krupahombre.algorithms.utils.PathCreationUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class RandomAlgorithm {
    public static Path execute(int[][] distanceMatrix) {
        final int iterations = 10000;
        int citiesNum = distanceMatrix.length;
        int bestCost = Integer.MAX_VALUE;
        int worstCost = 0;
        int totalCost = 0;
        List<Integer> costs = new ArrayList<>();
        List<Integer> tour = new ArrayList<>(IntStream.rangeClosed(0, citiesNum - 1)
                .boxed()
                .toList());

        for (int i = 0; i < iterations; i++) {
            Collections.shuffle(tour);
            int currentCost = 0;

            for (int j = 0; j < tour.size() - 1; j++) {
                currentCost += distanceMatrix[tour.get(j)][tour.get(j + 1)];
            }
            currentCost += distanceMatrix[tour.getLast()][tour.getFirst()];

            costs.add(currentCost);

            bestCost = Math.min(bestCost, currentCost);
            worstCost = Math.max(worstCost, currentCost);
            totalCost += currentCost;
        }

        return PathCreationUtils.createAndCalculatePath(totalCost, bestCost, worstCost, costs, iterations);
    }
}
