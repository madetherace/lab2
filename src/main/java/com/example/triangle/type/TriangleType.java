package com.example.triangle.type;

// Rule 14: Enum elements named as constants
public enum TriangleType {
    EQUILATERAL,    // Равносторонний
    ISOSCELES,      // Равнобедренный
    RIGHT_ANGLED,   // Прямоугольный
    SCALENE         // Произвольный (не равносторонний, не равнобедренный, не прямоугольный)
    // Note: A triangle can be Isosceles AND Right-Angled.
    // The service logic will need to decide how to classify these.
    // Often, Right-Angled takes precedence if both apply, or Scalene is used
    // only if none of the others fit. Let's assume Scalene means "none of the above".
}