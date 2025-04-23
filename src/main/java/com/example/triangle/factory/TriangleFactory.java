package com.example.triangle.factory;

import com.example.triangle.entity.Point;
import com.example.triangle.entity.Triangle;
import com.example.triangle.exception.TriangleException;

import java.util.List;

/**
 * Interface defining the contract for creating Triangle objects.
 * Follows the Factory Method pattern principles by providing dedicated creation methods.
 */
public interface TriangleFactory {

    /**
     * Creates a Triangle object from three Point objects.
     *
     * @param p1 The first vertex point.
     * @param p2 The second vertex point.
     * @param p3 The third vertex point.
     * @return The created Triangle object.
     * @throws TriangleException if the points are null, not distinct, or collinear,
     *                           preventing the formation of a valid triangle.
     */
    Triangle createTriangle(Point p1, Point p2, Point p3) throws TriangleException;

    /**
     * Creates a Triangle object from a list of 6 coordinate values (x1, y1, x2, y2, x3, y3).
     *
     * @param coordinates A list containing exactly 6 double values representing the coordinates.
     * @return The created Triangle object.
     * @throws TriangleException if the list does not contain exactly 6 numeric values,
     *                           or if the resulting points cannot form a valid triangle.
     */
    Triangle createTriangle(List<Double> coordinates) throws TriangleException;

    /**
     * Creates a Triangle object from six individual coordinate values.
     *
     * @param x1 X-coordinate of the first vertex.
     * @param y1 Y-coordinate of the first vertex.
     * @param x2 X-coordinate of the second vertex.
     * @param y2 Y-coordinate of the second vertex.
     * @param x3 X-coordinate of the third vertex.
     * @param y3 Y-coordinate of the third vertex.
     * @return The created Triangle object.
     * @throws TriangleException if the points derived from the coordinates cannot form a valid triangle.
     */
    Triangle createTriangle(double x1, double y1, double x2, double y2, double x3, double y3) throws TriangleException;
}