package com.example.triangle.service;

import com.example.triangle.entity.Triangle;
import com.example.triangle.type.TriangleType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class TriangleAnalysisService {

    private static final Logger logger = LogManager.getLogger(TriangleAnalysisService.class);
    private final TriangleTypeService typeService = new TriangleTypeService(); // Dependency
    private final TriangleCalculationService calculationService = new TriangleCalculationService(); // Dependency

    /**
     * Groups a list of triangles by their type.
     */
    public Map<TriangleType, List<Triangle>> groupTrianglesByType(List<Triangle> triangles) {
        if (triangles == null) {
            logger.warn("Input triangle list is null, returning empty map.");
            return Collections.emptyMap(); // Return empty map for null input
        }
        logger.info("Grouping {} triangles by type.", triangles.size());
        Map<TriangleType, List<Triangle>> groupedTriangles = triangles.stream()
                .collect(Collectors.groupingBy(typeService::determineType)); // Method reference

        // Log the counts for each type
        groupedTriangles.forEach((type, list) ->
                logger.info("Found {} triangles of type: {}", list.size(), type)
        );

        return groupedTriangles;
    }

    /**
     * Finds the triangle with the minimum area in a list.
     */
    public Optional<Triangle> findMinAreaTriangle(List<Triangle> triangles) {
        if (triangles == null || triangles.isEmpty()) {
            return Optional.empty();
        }
        // Comparator comparing triangles based on area
        Comparator<Triangle> areaComparator = Comparator.comparingDouble(calculationService::calculateArea);
        return triangles.stream().min(areaComparator);
    }

    /**
     * Finds the triangle with the maximum area in a list.
     */
    public Optional<Triangle> findMaxAreaTriangle(List<Triangle> triangles) {
        if (triangles == null || triangles.isEmpty()) {
            return Optional.empty();
        }
        Comparator<Triangle> areaComparator = Comparator.comparingDouble(calculationService::calculateArea);
        return triangles.stream().max(areaComparator);
    }

    /**
     * Finds the triangle with the minimum perimeter in a list.
     */
    public Optional<Triangle> findMinPerimeterTriangle(List<Triangle> triangles) {
        if (triangles == null || triangles.isEmpty()) {
            return Optional.empty();
        }
        Comparator<Triangle> perimeterComparator = Comparator.comparingDouble(calculationService::calculatePerimeter);
        return triangles.stream().min(perimeterComparator);
    }

    /**
     * Finds the triangle with the maximum perimeter in a list.
     */
    public Optional<Triangle> findMaxPerimeterTriangle(List<Triangle> triangles) {
        if (triangles == null || triangles.isEmpty()) {
            return Optional.empty();
        }
        Comparator<Triangle> perimeterComparator = Comparator.comparingDouble(calculationService::calculatePerimeter);
        return triangles.stream().max(perimeterComparator);
    }

    /**
     * Performs analysis on grouped triangles to find min/max area/perimeter for each group.
     * Returns a map where the key is the TriangleType and the value is another map
     * containing keys like "minArea", "maxArea", "minPerimeter", "maxPerimeter"
     * mapped to the corresponding Optional<Triangle>.
     */
    public Map<TriangleType, Map<String, Optional<Triangle>>> analyzeTriangleGroups(
            Map<TriangleType, List<Triangle>> groupedTriangles) {

        Map<TriangleType, Map<String, Optional<Triangle>>> analysisResults = new HashMap<>();

        logger.info("Analyzing groups to find min/max area/perimeter...");

        for (Map.Entry<TriangleType, List<Triangle>> entry : groupedTriangles.entrySet()) {
            TriangleType type = entry.getKey();
            List<Triangle> groupList = entry.getValue();
            logger.debug("Analyzing group: {}", type);

            Map<String, Optional<Triangle>> groupAnalysis = new HashMap<>();

            Optional<Triangle> minArea = findMinAreaTriangle(groupList);
            Optional<Triangle> maxArea = findMaxAreaTriangle(groupList);
            Optional<Triangle> minPerimeter = findMinPerimeterTriangle(groupList);
            Optional<Triangle> maxPerimeter = findMaxPerimeterTriangle(groupList);

            groupAnalysis.put("minArea", minArea);
            groupAnalysis.put("maxArea", maxArea);
            groupAnalysis.put("minPerimeter", minPerimeter);
            groupAnalysis.put("maxPerimeter", maxPerimeter);

            analysisResults.put(type, groupAnalysis);

            // Log results for the group
            minArea.ifPresent(t -> logger.info("Group [{}]: Min Area Triangle ID: {}, Area: {}", type, t.getTriangleId(), calculationService.calculateArea(t)));
            maxArea.ifPresent(t -> logger.info("Group [{}]: Max Area Triangle ID: {}, Area: {}", type, t.getTriangleId(), calculationService.calculateArea(t)));
            minPerimeter.ifPresent(t -> logger.info("Group [{}]: Min Perimeter Triangle ID: {}, Perimeter: {}", type, t.getTriangleId(), calculationService.calculatePerimeter(t)));
            maxPerimeter.ifPresent(t -> logger.info("Group [{}]: Max Perimeter Triangle ID: {}, Perimeter: {}", type, t.getTriangleId(), calculationService.calculatePerimeter(t)));
        }

        return analysisResults;
    }
}