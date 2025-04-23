package com.example.triangle.reader;

import com.example.triangle.exception.TriangleException;
import org.testng.annotations.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.*;

public class TriangleFileReaderTest {

    private TriangleFileReader reader;
    private static final String TEST_DIR = "temp_test_data"; // Временная директория
    private Path tempFileValid;
    private Path tempFileEmpty;
    private Path nonExistentFile = Paths.get(TEST_DIR, "non_existent_file.txt");

    @BeforeClass
    public void setUp() throws IOException {
        reader = new TriangleFileReader();
        // Создаем временную директорию
        Files.createDirectories(Paths.get(TEST_DIR));
        // Создаем временные файлы для тестов
        tempFileValid = Paths.get(TEST_DIR, "valid_data.txt");
        Files.write(tempFileValid, Arrays.asList("line 1", "line 2", "line 3"));

        tempFileEmpty = Paths.get(TEST_DIR, "empty_data.txt");
        Files.write(tempFileEmpty, Collections.emptyList());
    }

    @AfterClass
    public void tearDown() throws IOException {
        // Удаляем временные файлы и директорию
        Files.deleteIfExists(tempFileValid);
        Files.deleteIfExists(tempFileEmpty);
        Files.deleteIfExists(Paths.get(TEST_DIR));
    }

    @Test
    public void testReadLines_ValidFile_Success() throws TriangleException {
        // given
        String filePath = tempFileValid.toString();
        List<String> expectedLines = Arrays.asList("line 1", "line 2", "line 3");

        // when
        List<String> actualLines = reader.readLines(filePath);

        // then
        assertNotNull(actualLines);
        assertEquals(actualLines.size(), expectedLines.size());
        assertEquals(actualLines, expectedLines);
    }

    @Test
    public void testReadLines_EmptyFile_Success() throws TriangleException {
        // given
        String filePath = tempFileEmpty.toString();
        List<String> expectedLines = Collections.emptyList();

        // when
        List<String> actualLines = reader.readLines(filePath);

        // then
        assertNotNull(actualLines);
        assertTrue(actualLines.isEmpty());
        assertEquals(actualLines, expectedLines);
    }

    @Test(expectedExceptions = TriangleException.class,
            expectedExceptionsMessageRegExp = "File not found: .*non_existent_file\\.txt")
    public void testReadLines_NonExistentFile_ThrowsException() throws TriangleException {
        // given
        String filePath = nonExistentFile.toString(); // Файл заведомо не существует

        // when
        reader.readLines(filePath);

        // then - Ожидается TriangleException (проверяется аннотацией)
    }

    // Тест на нечитаемый файл сложнее сделать кроссплатформенно без мокинга.
    // Можно попробовать установить права только на запись, но это зависит от ОС.
    // @Test(expectedExceptions = TriangleException.class,
    //       expectedExceptionsMessageRegExp = "File not readable: .*")
    // public void testReadLines_UnreadableFile_ThrowsException() throws IOException, TriangleException {
    //     // given
    //     Path unreadableFile = Paths.get(TEST_DIR, "unreadable.txt");
    //     Files.write(unreadableFile, Collections.singletonList("cant read this"));
    //     // Попытка сделать файл нечитаемым (может не сработать или требовать прав админа)
    //     boolean success = unreadableFile.toFile().setReadable(false);
    //     if (!success) {
    //         throw new SkipException("Could not set file to unreadable, skipping test");
    //     }
    //     if (unreadableFile.toFile().canRead()) {
    //          throw new SkipException("File remained readable, skipping test");
    //     }
    //     String filePath = unreadableFile.toString();
    //
    //     try {
    //         // when
    //         reader.readLines(filePath);
    //     } finally {
    //         // then - Ожидается TriangleException
    //         // Важно вернуть права, чтобы файл можно было удалить в @AfterClass
    //         unreadableFile.toFile().setReadable(true);
    //         Files.deleteIfExists(unreadableFile);
    //     }
    // }
}