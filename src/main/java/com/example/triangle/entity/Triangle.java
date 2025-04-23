package com.example.triangle.entity;

import java.util.StringJoiner; // Импортируем StringJoiner

/**
 * Entity class representing a Triangle defined by three points.
 * Contains no business logic methods.
 */
public class Triangle {
    private final Point pointA;
    private final Point pointB;
    private final Point pointC;
    private final long triangleId; // Optional: Add an ID if needed

    // Counter for generating IDs (if used)
    private static long idCounter = 0;

    public Triangle(Point pointA, Point pointB, Point pointC) {
        // Basic validation can happen here (null checks)
        if (pointA == null || pointB == null || pointC == null) {
            // Or throw a custom exception if preferred
            // Используем стандартное исключение, т.к. это конструктор сущности,
            // а не бизнес-логика, где требуется кастомное.
            throw new IllegalArgumentException("Points cannot be null");
        }
        this.pointA = pointA;
        this.pointB = pointB;
        this.pointC = pointC;
        this.triangleId = generateId(); // Assign ID
    }

    // Private helper for ID generation
    private static synchronized long generateId() {
        return ++idCounter;
    }

    public Point getPointA() {
        return pointA;
    }

    public Point getPointB() {
        return pointB;
    }

    public Point getPointC() {
        return pointC;
    }

    public long getTriangleId() {
        return triangleId;
    }

    // No java.util.Objects allowed per requirement, implement manually
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triangle triangle = (Triangle) o;

        // Consider triangles equal if vertices match, regardless of order
        // This is more complex. For simplicity, let's assume order matters for now,
        // or that the factory ensures a consistent order.
        // A more robust equals would check if the set of points is the same.
        // For this simple equals, point equality check is sufficient.
        // Point.equals already handles double comparison with tolerance.
        boolean pointsEqual = pointA.equals(triangle.pointA) &&
                pointB.equals(triangle.pointB) &&
                pointC.equals(triangle.pointC);
        // Note: A more robust equals might need to check permutations or sort points first.

        // Also compare IDs if they are meant to be part of the identity
        // return triangleId == triangle.triangleId && pointsEqual;
        // Or just compare points if ID is just metadata
        return pointsEqual; // Simplified version focusing on geometry
    }

    @Override
    public int hashCode() {
        // Consistent with the simplified equals where order matters
        int result = pointA.hashCode();
        result = 31 * result + pointB.hashCode();
        result = 31 * result + pointC.hashCode();
        // Include ID in hashcode if it's part of equals
        // result = 31 * result + (int) (triangleId ^ (triangleId >>> 32));
        return result;
        // Alternative using standard Objects (if allowed for equals/hashCode):
        // return Objects.hash(pointA, pointB, pointC, triangleId);
    }

    /**
     * Returns a string representation of the triangle using StringJoiner.
     * Format: Triangle{id=value, a=Point{...}, b=Point{...}, c=Point{...}}
     * @return String representation of the triangle.
     */
    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", Triangle.class.getSimpleName() + "{", "}");
        joiner.add("id=" + triangleId);       // Добавляем ID
        joiner.add("a=" + pointA);           // Добавляем точку A (вызовется Point.toString())
        joiner.add("b=" + pointB);           // Добавляем точку B
        joiner.add("c=" + pointC);           // Добавляем точку C
        return joiner.toString();           // Возвращаем собранную строку
    }
}