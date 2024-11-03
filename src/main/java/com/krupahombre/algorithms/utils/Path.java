package com.krupahombre.algorithms.utils;

public class Path {
    private Integer bestCost;
    private Integer worstCost;
    private Long averageCost;
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

    public Long getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(Long averageCost) {
        this.averageCost = averageCost;
    }

    public Double getStandardDeviation() {
        return standardDeviation;
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
