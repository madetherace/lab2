package com.example.triangle.reader;

import com.example.triangle.exception.TriangleException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TriangleFileReader {

    private static final Logger logger = LogManager.getLogger(TriangleFileReader.class);

    public List<String> readLines(String relativeFilePath) throws TriangleException {
        logger.info("Attempting to read file: {}", relativeFilePath);
        Path path = Paths.get(relativeFilePath);

        if (!Files.exists(path)) {
            logger.error("File not found at path: {}", path.toAbsolutePath());
            throw new TriangleException("File not found: " + relativeFilePath);
        }

        if (!Files.isReadable(path)) {
            logger.error("File is not readable: {}", path.toAbsolutePath());
            throw new TriangleException("File not readable: " + relativeFilePath);
        }

        List<String> lines;
        // Using try-with-resources ensures the stream is closed
        try (Stream<String> stream = Files.lines(path)) {
            lines = stream.collect(Collectors.toList());
            logger.info("Successfully read {} lines from file: {}", lines.size(), relativeFilePath);
        } catch (IOException e) {
            logger.error("IOException occurred while reading file: {}", relativeFilePath, e);
            // Do not catch the generated exception immediately (Rule 4)
            throw new TriangleException("Error reading file: " + relativeFilePath, e);
        }
        // No need for finally block to close stream because of try-with-resources

        return lines;
    }

    // Alternative using BufferedReader (if preferred or for specific needs)
    public List<String> readLinesWithBufferedReader(String relativeFilePath) throws TriangleException {
        logger.info("Attempting to read file with BufferedReader: {}", relativeFilePath);
        Path path = Paths.get(relativeFilePath);
        // ... (add exists and readable checks as above) ...

        List<String> lines;
        BufferedReader reader = null; // Declare outside try
        try {
            reader = Files.newBufferedReader(path);
            lines = reader.lines().collect(Collectors.toList());
            logger.info("Successfully read {} lines from file: {}", lines.size(), relativeFilePath);
        } catch (IOException e) {
            logger.error("IOException occurred while reading file: {}", relativeFilePath, e);
            throw new TriangleException("Error reading file: " + relativeFilePath, e);
        } finally {
            // Rule 7: Close in finally
            if (reader != null) {
                try {
                    reader.close();
                    logger.debug("BufferedReader closed for file: {}", relativeFilePath);
                } catch (IOException e) {
                    // Rule 5: Do not generate exceptions in finally (log instead)
                    logger.error("Failed to close BufferedReader for file: {}", relativeFilePath, e);
                    // Avoid throwing or returning here
                }
            }
        }
        return lines;
    }
}