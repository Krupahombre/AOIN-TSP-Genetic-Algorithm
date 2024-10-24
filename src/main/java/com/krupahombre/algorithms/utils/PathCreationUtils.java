package com.krupahombre.algorithms.utils;

import java.util.List;

public class PathCreationUtils {
    public static Path createAndCalculatePath(int totalCost, int bestCost, int worstCost, List<Integer> costs, int iterations) {
        int averageCost = totalCost / iterations;
        double varianceSum = 0;

        for (int cost : costs) {
            varianceSum += Math.pow(cost - averageCost, 2);
        }
        double standardDeviation = Math.sqrt(varianceSum / iterations);

        Path path = new Path();
        path.setBestCost(bestCost);
        path.setWorstCost(worstCost);
        path.setAverageCost(averageCost);
        path.setStandardDeviation(standardDeviation);

        return path;
    }
}
