package com.krupahombre.algorithms.utils;

public class Path {
    private Integer bestCost;
    private Integer worstCost;
    private Integer averageCost;
    private Double standardDeviation;

    public void setBestCost(Integer bestCost) {
        this.bestCost = bestCost;
    }

    public void setWorstCost(Integer worstCost) {
        this.worstCost = worstCost;
    }

    public void setAverageCost(Integer averageCost) {
        this.averageCost = averageCost;
    }

    public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    @Override
    public String toString() {
        return "Path{" +
                "bestCost=" + bestCost +
                ", worstCost=" + worstCost +
                ", averageCost=" + averageCost +
                ", standardDeviation=" + standardDeviation +
                '}';
    }
}
