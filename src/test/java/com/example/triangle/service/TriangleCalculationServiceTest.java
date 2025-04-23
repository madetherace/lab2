package com.example.triangle.service;

import com.example.triangle.entity.Point;
import com.example.triangle.entity.Triangle;
import org.testng.annotations.BeforeClass; // Or BeforeMethod if needed per test
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*; // Static import for assertions

public class TriangleCalculationServiceTest {

    private TriangleCalculationService calculationService;

    // Rule 25: Test objects as constants or fields if reused
    private static final Point P00 = new Point(0, 0);
    private static final Point P30 = new Point(3, 0);
    private static final Point P04 = new Point(0, 4);
    private static final Point P_EQUIL_A = new Point(0, 0);
    private static final Point P_EQUIL_B = new Point(6, 0);
    private static final Point P_EQUIL_C = new Point(3, 5.19615); // Approx height for equilateral

    // Rule 26: Create objects with 'new' in tests, not factory (unless testing factory itself)
    private static final Triangle RIGHT_TRIANGLE = new Triangle(P00, P30, P04);
    private static final Triangle EQUILATERAL_TRIANGLE = new Triangle(P_EQUIL_A, P_EQUIL_B, P_EQUIL_C);

    @BeforeClass
    public void setUp() {
        calculationService = new TriangleCalculationService();
    }

    // --- Test Data ---
    @DataProvider(name = "perimeterTestData")
    public Object[][] perimeterDataProvider() {
        return new Object[][] {
                { RIGHT_TRIANGLE, 12.0 }, // 3 + 4 + 5 = 12
                { EQUILATERAL_TRIANGLE, 18.0 } // 6 + 6 + 6 = 18 (approx due to height)
        };
    }

    @DataProvider(name = "areaTestData")
    public Object[][] areaDataProvider() {
        return new Object[][] {
                { RIGHT_TRIANGLE, 6.0 }, // 0.5 * base * height = 0.5 * 3 * 4 = 6
                { EQUILATERAL_TRIANGLE, 15.58845 } // 0.5 * base * height = 0.5 * 6 * 5.19615
        };
    }


    // --- Tests ---
    @Test(dataProvider = "perimeterTestData")
    public void testCalculatePerimeter(Triangle triangle, double expectedPerimeter) {
        // given (already set up via data provider and @BeforeClass)
        double delta = 0.001; // Tolerance for double comparison

        // when (Rule 27: Single call to method under test)
        double actualPerimeter = calculationService.calculatePerimeter(triangle);

        // then (Rule 27: Assertions)
        // Rule 23: Use assertEquals for values
        assertEquals(actualPerimeter, expectedPerimeter, delta,
                "Perimeter calculation mismatch for triangle: " + triangle);
    }

    @Test(dataProvider = "areaTestData")
    public void testCalculateArea(Triangle triangle, double expectedArea) {
        // given
        double delta = 0.001;

        // when
        double actualArea = calculationService.calculateArea(triangle);

        // then
        assertEquals(actualArea, expectedArea, delta,
                "Area calculation mismatch for triangle: " + triangle);
    }

    // Example of testing an edge case (though validation should prevent this)
    @Test
    public void testCalculateArea_CollinearPoints() {
        // given
        Point p1 = new Point(0, 0);
        Point p2 = new Point(1, 1);
        Point p3 = new Point(2, 2);
        Triangle collinearTriangle = new Triangle(p1, p2, p3); // Assume constructor allows it for test
        double expectedArea = 0.0;
        double delta = 0.0001;

        // when
        double actualArea = calculationService.calculateArea(collinearTriangle);

        // then
        assertEquals(actualArea, expectedArea, delta, "Area for collinear points should be zero.");
    }
}