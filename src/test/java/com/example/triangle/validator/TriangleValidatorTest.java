package com.example.triangle.validator;

import com.example.triangle.entity.Point;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.*;

public class TriangleValidatorTest {

    private TriangleValidator validator;

    // Правило 25: Тестовые объекты лучше размещать как константы/поля
    private static final Point P00 = new Point(0, 0);
    private static final Point P10 = new Point(1, 0);
    private static final Point P20 = new Point(2, 0);
    private static final Point P01 = new Point(0, 1);
    private static final Point P11 = new Point(1, 1);

    @BeforeClass
    public void setUp() {
        validator = new TriangleValidator();
    }

    // --- Тесты для hasCorrectNumberOfCoordinates ---

    @Test
    public void testHasCorrectNumberOfCoordinates_Valid_ReturnsTrue() {
        // given
        List<Double> validCoords = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);

        // when
        boolean result = validator.hasCorrectNumberOfCoordinates(validCoords);

        // then
        assertTrue(result); // Правило 24: Использовать assertTrue(isValid)
    }

    @DataProvider(name = "invalidCoordinateLists")
    public Object[][] invalidCoordinateLists() {
        return new Object[][] {
                { Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0) }, // Too few
                { Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0) }, // Too many
                { Collections.emptyList() }, // Empty
                { null } // Null
        };
    }

    @Test(dataProvider = "invalidCoordinateLists")
    public void testHasCorrectNumberOfCoordinates_Invalid_ReturnsFalse(List<Double> coords) {
        // given - coords from data provider

        // when
        boolean result = validator.hasCorrectNumberOfCoordinates(coords);

        // then
        assertFalse(result);
    }

    // --- Тесты для isValidTriangle ---

    @Test
    public void testIsValidTriangle_ValidPoints_ReturnsTrue() {
        // given - P00, P10, P01 образуют правильный треугольник

        // when
        boolean result = validator.isValidTriangle(P00, P10, P01);

        // then
        assertTrue(result);
    }

    @DataProvider(name = "invalidTrianglePoints")
    public Object[][] invalidTrianglePoints() {
        return new Object[][] {
                { P00, P10, P20 }, // Коллинеарные по X
                { P00, P01, new Point(0, 2) }, // Коллинеарные по Y
                { P00, P11, new Point(2, 2) }, // Коллинеарные по диагонали
                { P00, P10, P00 }, // Две точки совпадают
                { P11, P11, P11 }, // Все точки совпадают
                { null, P10, P01 }, // Одна точка null
                { P00, null, P01 }, // Одна точка null
                { P00, P10, null }, // Одна точка null
                { null, null, null } // Все точки null
        };
    }

    @Test(dataProvider = "invalidTrianglePoints")
    public void testIsValidTriangle_InvalidPoints_ReturnsFalse(Point p1, Point p2, Point p3) {
        // given - points from data provider

        // when
        boolean result = validator.isValidTriangle(p1, p2, p3);

        // then
        assertFalse(result);
    }
}