package com.krupahombre.algorithms;

import com.krupahombre.algorithms.utils.Path;
import com.krupahombre.algorithms.utils.PathCreationUtils;

import java.util.ArrayList;
import java.util.List;

public class GreedyAlgorithm {
    public static Path execute(int[][] distanceMatrix) {
        int citiesNum = distanceMatrix.length;
        int bestCost = Integer.MAX_VALUE;
        int worstCost = 0;
        int totalCost = 0;
        List<Integer> costs = new ArrayList<>();

        for (int i = 0; i < citiesNum; i++) {
            int currentCity = i;
            boolean[] visited = new boolean[citiesNum];
            visited[i] = true;

            int currentCost = 0;
            int visitedCount = 1;

            while (visitedCount < citiesNum) {
                int nextCity = findNearestCity(distanceMatrix, currentCity, visited);
                currentCost += distanceMatrix[currentCity][nextCity];
                visited[nextCity] = true;
                currentCity = nextCity;
                visitedCount++;
            }

            currentCost += distanceMatrix[currentCity][i];

            costs.add(currentCost);
            bestCost = Math.min(bestCost, currentCost);
            worstCost = Math.max(worstCost, currentCost);
            totalCost += currentCost;
        }

        return PathCreationUtils.createAndCalculatePath(totalCost, bestCost, worstCost, costs, citiesNum);
    }

    private static int findNearestCity(int[][] distanceMatrix, int currentCity, boolean[] visited) {
        int nearestCity = -1;
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < distanceMatrix[currentCity].length; i++) {
            if (!visited[i] && distanceMatrix[currentCity][i] < minDistance) {
                minDistance = distanceMatrix[currentCity][i];
                nearestCity = i;
            }
        }
        return nearestCity;
    }
}
