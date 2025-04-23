package com.example.triangle.factory;

import com.example.triangle.entity.Point;
import com.example.triangle.entity.Triangle;
import com.example.triangle.exception.TriangleException;
import com.example.triangle.validator.TriangleValidator; // Импортируем валидатор
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Concrete implementation of the TriangleFactory interface.
 * Uses a TriangleValidator to ensure points form a valid triangle before instantiation.
 */
public class TriangleFactoryImpl implements TriangleFactory {

    private static final Logger logger = LogManager.getLogger(TriangleFactoryImpl.class);
    // Фабрика зависит от валидатора для проверки корректности данных перед созданием объекта
    private final TriangleValidator validator = new TriangleValidator();

    /**
     * Creates a Triangle object from three Point objects after validation.
     *
     * @param p1 The first vertex point.
     * @param p2 The second vertex point.
     * @param p3 The third vertex point.
     * @return The created Triangle object.
     * @throws TriangleException if validation fails (points are null, not distinct, or collinear).
     */
    @Override
    public Triangle createTriangle(Point p1, Point p2, Point p3) throws TriangleException {
        logger.debug("Attempting to create triangle from points: {}, {}, {}", p1, p2, p3);

        // Правило 1: Сначала положительный сценарий в if
        if (validator.isValidTriangle(p1, p2, p3)) {
            // Создаем объект только если валидация прошла успешно
            Triangle triangle = new Triangle(p1, p2, p3);
            // Логируем успешное создание, можно добавить ID
            logger.info("Successfully created Triangle with ID {}", triangle.getTriangleId());
            return triangle;
        } else {
            // Правило 4: Не ловим исключение сразу, если генерируем новое
            // Выбрасываем исключение, если точки не образуют валидный треугольник
            String errorMessage = String.format("Cannot create triangle: Points are invalid (e.g., null, not distinct, or collinear). Points: %s, %s, %s", p1, p2, p3);
            logger.error(errorMessage);
            throw new TriangleException(errorMessage);
        }
    }

    /**
     * Creates a Triangle object from a list of 6 coordinates after validation.
     *
     * @param coordinates A list containing exactly 6 double values.
     * @return The created Triangle object.
     * @throws TriangleException if the list size is incorrect or points are invalid.
     */
    @Override
    public Triangle createTriangle(List<Double> coordinates) throws TriangleException {
        logger.debug("Attempting to create triangle from coordinate list: {}", coordinates);

        // Правило 18: Объявление и инициализация вместе
        // Правило 19: Минимальное расстояние между объявлением и использованием
        boolean hasCorrectNumber = validator.hasCorrectNumberOfCoordinates(coordinates);

        // Правило 2: if может обрабатывать и отрицательный сценарий
        if (!hasCorrectNumber) {
            String errorMessage = "Cannot create triangle: Invalid number of coordinates provided. Expected 6, got " + (coordinates == null ? "null" : coordinates.size());
            logger.error(errorMessage);
            throw new TriangleException(errorMessage);
        }

        // Правило 20: Избегаем цепочек вызовов, используем локальные переменные
        double x1 = coordinates.get(0);
        double y1 = coordinates.get(1);
        double x2 = coordinates.get(2);
        double y2 = coordinates.get(3);
        double x3 = coordinates.get(4);
        double y3 = coordinates.get(5);

        // Создаем точки из координат
        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);
        Point p3 = new Point(x3, y3);

        // Делегируем создание и основную валидацию другому методу этой же фабрики
        // Это позволяет избежать дублирования логики валидации (коллинеарность и т.д.)
        return createTriangle(p1, p2, p3);
    }

    /**
     * Creates a Triangle object from six individual coordinate values after validation.
     *
     * @param x1 X-coordinate of the first vertex.
     * @param y1 Y-coordinate of the first vertex.
     * @param x2 X-coordinate of the second vertex.
     * @param y2 Y-coordinate of the second vertex.
     * @param x3 X-coordinate of the third vertex.
     * @param y3 Y-coordinate of the third vertex.
     * @return The created Triangle object.
     * @throws TriangleException if the derived points cannot form a valid triangle.
     */
    @Override
    public Triangle createTriangle(double x1, double y1, double x2, double y2, double x3, double y3) throws TriangleException {
        logger.debug("Attempting to create triangle from coordinate values: ({}, {}), ({}, {}), ({}, {})", x1, y1, x2, y2, x3, y3);

        // Создаем точки из координат
        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);
        Point p3 = new Point(x3, y3);

        // Делегируем создание и основную валидацию другому методу
        return createTriangle(p1, p2, p3);
    }
}