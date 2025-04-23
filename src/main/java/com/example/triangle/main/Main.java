package com.example.triangle.main;

import com.example.triangle.entity.Triangle;
import com.example.triangle.exception.TriangleException;
import com.example.triangle.factory.TriangleFactory;
import com.example.triangle.factory.TriangleFactoryImpl;
import com.example.triangle.parser.TriangleStringParser;
import com.example.triangle.reader.TriangleFileReader;
import com.example.triangle.service.TriangleAnalysisService;
import com.example.triangle.service.TriangleCalculationService;
import com.example.triangle.type.TriangleType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    // Rule 12: Use relative path. Place file in 'data' folder in project root.
    private static final String INPUT_FILE_PATH = "data/triangles.txt";

    public static void main(String[] args) {
        logger.info("Triangle application starting...");

        // Instantiate necessary components
        TriangleFileReader reader = new TriangleFileReader();
        TriangleStringParser parser = new TriangleStringParser();
        TriangleFactory factory = new TriangleFactoryImpl(); // Use the interface type
        TriangleAnalysisService analysisService = new TriangleAnalysisService();
        TriangleCalculationService calculationService = new TriangleCalculationService(); // Needed for output

        List<Triangle> triangles = new ArrayList<>();
        List<String> linesWithError = new ArrayList<>();

        try {
            List<String> rawLines = reader.readLines(INPUT_FILE_PATH);
            logger.info("Read {} lines from the file.", rawLines.size());

            for (String line : rawLines) {
                try {
                    List<Double> coordinates = parser.parseCoordinates(line);
                    Triangle triangle = factory.createTriangle(coordinates);
                    triangles.add(triangle);
                } catch (TriangleException e) {
                    // Log the error and the line, then continue with the next line
                    logger.error("Skipping line due to error: '{}'. Reason: {}", line, e.getMessage());
                    linesWithError.add(line);
                }
            }

            logger.info("Successfully created {} triangles.", triangles.size());
            if (!linesWithError.isEmpty()) {
                logger.warn("Skipped {} lines due to errors.", linesWithError.size());
                // Optionally print skipped lines: linesWithError.forEach(logger::warn);
            }

            // Perform analysis
            if (!triangles.isEmpty()) {
                Map<TriangleType, List<Triangle>> groupedTriangles = analysisService.groupTrianglesByType(triangles);

                // Output counts
                System.out.println("\n--- Triangle Counts by Type ---");
                groupedTriangles.forEach((type, list) ->
                        System.out.printf("Type: %-15s Count: %d%n", type, list.size())
                );

                // Find and output min/max for each group
                Map<TriangleType, Map<String, Optional<Triangle>>> analysisResults =
                        analysisService.analyzeTriangleGroups(groupedTriangles);

                System.out.println("\n--- Min/Max Analysis per Group ---");
                analysisResults.forEach((type, resultsMap) -> {
                    System.out.println("\nGroup: " + type);
                    printAnalysisResult("Min Area", resultsMap.get("minArea"), calculationService);
                    printAnalysisResult("Max Area", resultsMap.get("maxArea"), calculationService);
                    printAnalysisResult("Min Perimeter", resultsMap.get("minPerimeter"), calculationService);
                    printAnalysisResult("Max Perimeter", resultsMap.get("maxPerimeter"), calculationService);
                });

            } else {
                logger.warn("No valid triangles were created from the file.");
                System.out.println("No valid triangles found in the input file.");
            }

        } catch (TriangleException e) {
            // Catch exceptions from file reading or other unrecoverable issues
            logger.fatal("A critical error occurred: {}", e.getMessage(), e);
            System.err.println("Application failed: " + e.getMessage());
        } catch (Exception e) {
            // Catch unexpected runtime exceptions
            logger.fatal("An unexpected error occurred: {}", e.getMessage(), e);
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }

        logger.info("Triangle application finished.");
    }

    // Helper method for printing analysis results
    private static void printAnalysisResult(String label, Optional<Triangle> triangleOpt, TriangleCalculationService calcService) {
        System.out.printf("%-15s: ", label);
        if (triangleOpt.isPresent()) { // Rule 21: Check boolean directly
            Triangle t = triangleOpt.get();
            double area = calcService.calculateArea(t);
            double perimeter = calcService.calculatePerimeter(t);
            System.out.printf("Triangle ID %d (Area: %.2f, Perimeter: %.2f)%n", t.getTriangleId(), area, perimeter);
        } else {
            System.out.println("N/A (Group might be empty or calculation failed)");
        }
    }
}