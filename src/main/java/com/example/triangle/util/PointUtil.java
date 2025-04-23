package com.example.triangle.util;

import com.example.triangle.entity.Point;

public class PointUtil {

    // Epsilon for floating point comparisons
    private static final double EPSILON = 1e-9;

    /**
     * Calculates the squared distance between two points.
     * Avoiding sqrt can be faster if only comparing distances.
     */
    public static double distanceSq(Point p1, Point p2) {
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        return dx * dx + dy * dy;
    }

    /**
     * Calculates the distance between two points.
     */
    public static double distance(Point p1, Point p2) {
        return Math.sqrt(distanceSq(p1, p2));
    }

    /**
     * Compares two double values using a tolerance (epsilon).
     */
    public static boolean areEqual(double d1, double d2) {
        return Math.abs(d1 - d2) < EPSILON;
    }
}