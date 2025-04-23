package com.example.triangle.factory;

import com.example.triangle.entity.Point;
import com.example.triangle.entity.Triangle;
import com.example.triangle.exception.TriangleException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.*;

public class TriangleFactoryImplTest {

    private TriangleFactory factory; // Используем интерфейс

    // Правило 26: Объекты в тестах делать через new
    private static final Point P00 = new Point(0, 0);
    private static final Point P30 = new Point(3, 0);
    private static final Point P04 = new Point(0, 4);
    private static final Point P_COLLINEAR = new Point(6, 0); // P00, P30, P_COLLINEAR - коллинеарны

    @BeforeClass
    public void setUp() {
        factory = new TriangleFactoryImpl();
    }

    // --- Тесты для createTriangle(Point, Point, Point) ---

    @Test
    public void testCreateTriangleFromPoints_Valid_Success() throws TriangleException {
        // given - P00, P30, P04

        // when
        Triangle triangle = factory.createTriangle(P00, P30, P04);

        // then
        assertNotNull(triangle);
        assertEquals(triangle.getPointA(), P00);
        assertEquals(triangle.getPointB(), P30);
        assertEquals(triangle.getPointC(), P04);
    }

    @Test(expectedExceptions = TriangleException.class)
    public void testCreateTriangleFromPoints_Collinear_ThrowsException() throws TriangleException {
        // given - P00, P30, P_COLLINEAR

        // when
        factory.createTriangle(P00, P30, P_COLLINEAR);

        // then - Ожидается TriangleException
    }

    @Test(expectedExceptions = TriangleException.class)
    public void testCreateTriangleFromPoints_Duplicate_ThrowsException() throws TriangleException {
        // given - P00, P30, P00

        // when
        factory.createTriangle(P00, P30, P00);

        // then - Ожидается TriangleException
    }

    @Test(expectedExceptions = IllegalArgumentException.class) // Конструктор кидает это при null
    public void testCreateTriangleFromPoints_Null_ThrowsException() throws TriangleException {
        // given
        Point p1 = null;

        // when
        factory.createTriangle(p1, P30, P04);

        // then - Ожидается IllegalArgumentException от конструктора Triangle
        //        или TriangleException от валидатора, если он проверяет null первым
        //        (В текущей реализации валидатор проверяет null -> TriangleException)
        // Изменим ожидаемое исключение на TriangleException, т.к. валидатор вызывается первым
    }
    // Исправленный тест на null:
    @Test(expectedExceptions = TriangleException.class)
    public void testCreateTriangleFromPoints_Null_ThrowsTriangleException() throws TriangleException {
        // given
        Point p1 = null;

        // when
        factory.createTriangle(p1, P30, P04);

        // then - Ожидается TriangleException от валидатора
    }

    // --- Тесты для createTriangle(List<Double>) ---

    @Test
    public void testCreateTriangleFromList_Valid_Success() throws TriangleException {
        // given
        List<Double> coords = Arrays.asList(0.0, 0.0, 3.0, 0.0, 0.0, 4.0);

        // when
        Triangle triangle = factory.createTriangle(coords);

        // then
        assertNotNull(triangle);
        assertEquals(triangle.getPointA(), P00);
        assertEquals(triangle.getPointB(), P30);
        assertEquals(triangle.getPointC(), P04);
    }

    @Test(expectedExceptions = TriangleException.class)
    public void testCreateTriangleFromList_InvalidSize_ThrowsException() throws TriangleException {
        // given
        List<Double> coords = Arrays.asList(0.0, 0.0, 3.0, 0.0, 0.0); // Не 6 элементов

        // when
        factory.createTriangle(coords);

        // then - Ожидается TriangleException
    }

    @Test(expectedExceptions = TriangleException.class)
    public void testCreateTriangleFromList_CollinearCoords_ThrowsException() throws TriangleException {
        // given
        List<Double> coords = Arrays.asList(0.0, 0.0, 3.0, 0.0, 6.0, 0.0); // Коллинеарные

        // when
        factory.createTriangle(coords);

        // then - Ожидается TriangleException
    }

    // --- Тесты для createTriangle(double...) ---

    @Test
    public void testCreateTriangleFromDoubles_Valid_Success() throws TriangleException {
        // given
        double x1=0, y1=0, x2=3, y2=0, x3=0, y3=4;

        // when
        Triangle triangle = factory.createTriangle(x1, y1, x2, y2, x3, y3);

        // then
        assertNotNull(triangle);
        assertEquals(triangle.getPointA(), P00);
        assertEquals(triangle.getPointB(), P30);
        assertEquals(triangle.getPointC(), P04);
    }

    @Test(expectedExceptions = TriangleException.class)
    public void testCreateTriangleFromDoubles_CollinearCoords_ThrowsException() throws TriangleException {
        // given
        double x1=0, y1=0, x2=3, y2=0, x3=6, y3=0; // Коллинеарные

        // when
        factory.createTriangle(x1, y1, x2, y2, x3, y3);

        // then - Ожидается TriangleException
    }
}