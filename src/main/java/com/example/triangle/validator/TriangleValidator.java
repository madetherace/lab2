package com.example.triangle.validator;

import com.example.triangle.entity.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TriangleValidator {

    private static final Logger logger = LogManager.getLogger(TriangleValidator.class);
    // Epsilon for floating point comparisons
    private static final double EPSILON = 1e-9;

    /**
     * Validates if a list of doubles contains exactly 6 values.
     * (Basic structural validation, parsing already did numeric check).
     */
    public boolean hasCorrectNumberOfCoordinates(List<Double> coordinates) {
        boolean isValid = coordinates != null && coordinates.size() == 6;
        if (!isValid) {
            logger.warn("Validation failed: Incorrect number of coordinates. Expected 6, got {}",
                    coordinates == null ? "null" : coordinates.size());
        }
        // Rule 22: Simplify boolean return
        return isValid;
    }

    /**
     * Validates if three points can form a non-degenerate triangle.
     * Checks for non-null points and non-collinearity.
     */
    public boolean isValidTriangle(Point p1, Point p2, Point p3) {
        if (p1 == null || p2 == null || p3 == null) {
            logger.warn("Validation failed: One or more points are null.");
            return false;
        }

        // Check for distinct points (optional, but good practice)
        if (p1.equals(p2) || p1.equals(p3) || p2.equals(p3)) {
            logger.warn("Validation failed: Points are not distinct. p1={}, p2={}, p3={}", p1, p2, p3);
            return false;
        }

        // Check for collinearity using area calculation (determinant method)
        // Area = 0.5 * |x1(y2 - y3) + x2(y3 - y1) + x3(y1 - y2)|
        // If area is close to zero, points are collinear.
        double area = 0.5 * Math.abs(p1.getX() * (p2.getY() - p3.getY()) +
                p2.getX() * (p3.getY() - p1.getY()) +
                p3.getX() * (p1.getY() - p2.getY()));

        boolean isNonCollinear = area > EPSILON;

        if (!isNonCollinear) {
            logger.warn("Validation failed: Points are collinear. p1={}, p2={}, p3={}", p1, p2, p3);
        } else {
            logger.debug("Validation success: Points form a valid triangle. p1={}, p2={}, p3={}", p1, p2, p3);
        }
        // Rule 21: Simplify boolean condition check
        // Rule 22: Simplify boolean return
        return isNonCollinear;
    }
}