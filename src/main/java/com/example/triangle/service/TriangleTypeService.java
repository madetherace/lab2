package com.example.triangle.service;

import com.example.triangle.entity.Point;
import com.example.triangle.entity.Triangle;
import com.example.triangle.type.TriangleType;
import com.example.triangle.util.PointUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TriangleTypeService {

    private static final Logger logger = LogManager.getLogger(TriangleTypeService.class);

    public TriangleType determineType(Triangle triangle) {
        // Rule 20: Avoid chaining
        Point pA = triangle.getPointA();
        Point pB = triangle.getPointB();
        Point pC = triangle.getPointC();

        // Use squared distances for comparison to avoid sqrt inaccuracies initially
        double distSqAB = PointUtil.distanceSq(pA, pB);
        double distSqBC = PointUtil.distanceSq(pB, pC);
        double distSqCA = PointUtil.distanceSq(pC, pA);

        // Use actual distances for equilateral/isosceles checks for clarity
        double distAB = Math.sqrt(distSqAB);
        double distBC = Math.sqrt(distSqBC);
        double distCA = Math.sqrt(distSqCA);

        boolean isEquilateral = PointUtil.areEqual(distAB, distBC) && PointUtil.areEqual(distBC, distCA);
        if (isEquilateral) { // Rule 21: Use boolean directly
            logger.debug("Triangle ID {} is EQUILATERAL", triangle.getTriangleId());
            return TriangleType.EQUILATERAL;
        }

        boolean isIsosceles = PointUtil.areEqual(distAB, distBC) ||
                PointUtil.areEqual(distBC, distCA) ||
                PointUtil.areEqual(distCA, distAB);
        // Check for right angle using Pythagorean theorem (a^2 + b^2 = c^2)
        // Check all permutations
        boolean isRightAngled = PointUtil.areEqual(distSqAB + distSqBC, distSqCA) ||
                PointUtil.areEqual(distSqBC + distSqCA, distSqAB) ||
                PointUtil.areEqual(distSqCA + distSqAB, distSqBC);

        // Classification logic:
        // Decide precedence if both Isosceles and Right Angled (e.g., prioritize Right Angled)
        if (isRightAngled) {
            // Could log if it's also isosceles here if needed
            logger.debug("Triangle ID {} is RIGHT_ANGLED", triangle.getTriangleId());
            return TriangleType.RIGHT_ANGLED;
        }

        // Rule 1: Positive scenario first
        if (isIsosceles) {
            logger.debug("Triangle ID {} is ISOSCELES", triangle.getTriangleId());
            return TriangleType.ISOSCELES;
        } else {
            // If none of the above, it's Scalene (Arbitrary)
            logger.debug("Triangle ID {} is SCALENE", triangle.getTriangleId());
            return TriangleType.SCALENE;
        }
    }
}
