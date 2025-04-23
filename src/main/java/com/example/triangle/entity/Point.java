package com.example.triangle.entity;

import java.util.StringJoiner; // Импортируем StringJoiner

/**
 * Represents a point in a 2D plane.
 */
public class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // No java.util.Objects allowed per requirement, implement manually
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        // Use tolerance for double comparison
        double epsilon = 1e-9;
        boolean xEquals = Math.abs(point.x - x) < epsilon;
        boolean yEquals = Math.abs(point.y - y) < epsilon;
        return xEquals && yEquals;
    }

    @Override
    public int hashCode() {
        // Simple hash code implementation for doubles
        long tempX = Double.doubleToLongBits(x);
        long tempY = Double.doubleToLongBits(y);
        int result = (int) (tempX ^ (tempX >>> 32));
        result = 31 * result + (int) (tempY ^ (tempY >>> 32));
        return result;
        // Alternative using standard Objects (if allowed for equals/hashCode):
        // return Objects.hash(x, y);
    }

    /**
     * Returns a string representation of the point using StringJoiner.
     * Format: Point{x=value, y=value}
     * @return String representation of the point.
     */
    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", Point.class.getSimpleName() + "{", "}");
        joiner.add("x=" + x); // Добавляем поле x
        joiner.add("y=" + y); // Добавляем поле y
        return joiner.toString(); // Возвращаем собранную строку
    }
}