package com.krupahombre.helpers;

public class CalculationsHelper {
    public static double toRadians(double coord) {
        long deg = nint(coord);
        double min = coord - deg;
        return Math.PI * (deg + 5.0 * min / 3.0) / 180.0;
    }

    public static long nint(double x) {
        return Math.round(x + 0.5);
    }
}
