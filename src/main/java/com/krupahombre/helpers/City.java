package com.krupahombre.helpers;

import static com.krupahombre.helpers.CalculationsHelper.toRadians;
import static com.krupahombre.helpers.CalculationsHelper.nint;

public class City {
    private int id;
    private double x;
    private double y;

    public City(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int distanceToEUC_2D(City other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return (int) nint(Math.sqrt(dx * dx + dy * dy));
    }

    public int distanceToGEO(City other) {
        final double RRR = 6378.388;

        double lat1 = toRadians(this.x);
        double lon1 = toRadians(this.y);
        double lat2 = toRadians(other.x);
        double lon2 = toRadians(other.y);

        double q1 = Math.cos(lon1 - lon2);
        double q2 = Math.cos(lat1 - lat2);
        double q3 = Math.cos(lat1 + lat2);
        double distance = RRR * Math.acos(0.5 * ((1.0 + q1) * q2 - (1.0 - q1) * q3)) + 1.0;

        return (int) distance;
    }
    
    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
