package com.example.triangle.parser;

import com.example.triangle.exception.TriangleException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
// Убираем неиспользуемый импорт Pattern, если не используем Regex для валидации формата
// import java.util.regex.Pattern;

public class TriangleStringParser {

    private static final Logger logger = LogManager.getLogger(TriangleStringParser.class);
    // Убираем Regex, т.к. он не учитывал комментарии и создавал путаницу
    // private static final String COORD_REGEX_STR = "^(\\s*[-+]?\\d+(\\.\\d+)?\\s+){5}[-+]?\\d+(\\.\\d+)?\\s*$";
    // private static final Pattern COORDINATE_PATTERN = Pattern.compile(COORD_REGEX_STR);
    private static final String WHITESPACE_DELIMITER = "\\s+"; // Split by one or more whitespace
    private static final String COMMENT_MARKER = "#"; // Символ начала комментария

    /**
     * Parses a string line expected to contain 6 coordinate values, ignoring comments.
     * @param line The input string line.
     * @return A list of 6 Double values.
     * @throws TriangleException if the line format is invalid or values are not numeric after removing comments.
     */
    public List<Double> parseCoordinates(String line) throws TriangleException {
        if (line == null) { // Убрали trim().isEmpty() здесь, обработаем ниже
            logger.warn("Input line is null. Skipping.");
            throw new TriangleException("Input line is null.");
        }

        // 1. Удаляем комментарии
        String lineWithoutComment = line;
        int commentIndex = line.indexOf(COMMENT_MARKER);
        if (commentIndex != -1) { // Если комментарий найден
            lineWithoutComment = line.substring(0, commentIndex);
        }

        // 2. Убираем лишние пробелы по краям
        String trimmedLine = lineWithoutComment.trim();
        logger.debug("Parsing effective line part: '{}'", trimmedLine);

        // 3. Проверяем на пустоту ПОСЛЕ удаления комментария и trim
        if (trimmedLine.isEmpty()) {
            logger.warn("Line is empty after removing comments and trimming. Original line: '{}'", line);
            throw new TriangleException("Line becomes empty after removing comments: " + line);
        }

        // 4. Разбиваем на части
        String[] parts = trimmedLine.split(WHITESPACE_DELIMITER);
        if (parts.length != 6) {
            logger.error("Invalid number of coordinates in line part: '{}'. Expected 6, found {}. Original line: '{}'",
                    trimmedLine, parts.length, line);
            throw new TriangleException("Invalid number of coordinates in line: " + line + ". Expected 6 after removing comments.");
        }

        // 5. Преобразуем в Double
        List<Double> coordinates = new ArrayList<>(6);
        for (String part : parts) {
            try {
                double value = Double.parseDouble(part);
                coordinates.add(value);
            } catch (NumberFormatException e) {
                logger.error("Non-numeric value '{}' found in line part: '{}'. Original line: '{}'",
                        part, trimmedLine, line, e);
                throw new TriangleException("Invalid numeric value '" + part + "' in line: " + line, e);
            }
        }

        logger.debug("Successfully parsed coordinates: {}", coordinates);
        return coordinates;
    }
}