package com.example.triangle.service;

import com.example.triangle.entity.Point;
import com.example.triangle.entity.Triangle;
import com.example.triangle.type.TriangleType;
import com.example.triangle.util.PointUtil; // Using the utility class
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class TriangleCalculationService {

    private static final Logger logger = LogManager.getLogger(TriangleCalculationService.class);

    public double calculatePerimeter(Triangle triangle) {
        // Rule 20: Avoid chaining
        Point pA = triangle.getPointA();
        Point pB = triangle.getPointB();
        Point pC = triangle.getPointC();

        double sideAB = PointUtil.distance(pA, pB);
        double sideBC = PointUtil.distance(pB, pC);
        double sideCA = PointUtil.distance(pC, pA);

        double perimeter = sideAB + sideBC + sideCA;
        logger.debug("Calculated perimeter for Triangle ID {}: {}", triangle.getTriangleId(), perimeter);
        return perimeter;
    }

    public double calculateArea(Triangle triangle) {
        // Rule 20: Avoid chaining
        Point pA = triangle.getPointA();
        Point pB = triangle.getPointB();
        Point pC = triangle.getPointC();

        // Using determinant formula (more direct than Heron's for coordinates)
        double area = 0.5 * Math.abs(pA.getX() * (pB.getY() - pC.getY()) +
                pB.getX() * (pC.getY() - pA.getY()) +
                pC.getX() * (pA.getY() - pB.getY()));
        logger.debug("Calculated area for Triangle ID {}: {}", triangle.getTriangleId(), area);
        return area;
    }
}