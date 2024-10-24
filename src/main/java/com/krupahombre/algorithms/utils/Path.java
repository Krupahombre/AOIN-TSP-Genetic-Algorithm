package com.krupahombre.algorithms.utils;

public class Path {
    private Integer bestCost;
    private Integer worstCost;
    private Integer averageCost;
    private Double standardDeviation;

    public Integer getBestCost() {
        return bestCost;
    }

    public void setBestCost(Integer bestCost) {
        this.bestCost = bestCost;
    }

    public Integer getWorstCost() {
        return worstCost;
    }

    public void setWorstCost(Integer worstCost) {
        this.worstCost = worstCost;
    }

    public Integer getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(Integer averageCost) {
        this.averageCost = averageCost;
    }

    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }
}
