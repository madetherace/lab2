package com.example.triangle.parser;

import com.example.triangle.exception.TriangleException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class TriangleStringParserTest {

    private TriangleStringParser parser;

    @BeforeClass
    public void setUp() {
        parser = new TriangleStringParser();
    }

    @DataProvider(name = "validLinesProvider")
    public Object[][] validLinesProvider() {
        return new Object[][] {
                {"0 0 1 1 0 1", Arrays.asList(0.0, 0.0, 1.0, 1.0, 0.0, 1.0)},
                {" 1.5 -2.0 3e2 4.1 5.67 0.0 ", Arrays.asList(1.5, -2.0, 300.0, 4.1, 5.67, 0.0)},
                {"\t1\t2\t3\t4\t5\t6\t", Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)}
        };
    }

    @DataProvider(name = "invalidLinesProvider")
    public Object[][] invalidLinesProvider() {
        return new Object[][] {
                {"1 2 3 4 5"},      // Недостаточно координат
                {"1 2 3 4 5 6 7"}, // Слишком много координат
                {"1 2 3 4 5 six"}, // Нечисловое значение
                {"1 2, 3 4 5 6"},  // Неправильный разделитель
                {""},              // Пустая строка
                {"   "},           // Строка с пробелами
                {null}             // null строка
        };
    }

    @Test(dataProvider = "validLinesProvider")
    public void testParseCoordinates_ValidLine_Success(String line, List<Double> expected) throws TriangleException {
        // given - line from data provider

        // when
        List<Double> actual = parser.parseCoordinates(line);

        // then
        assertNotNull(actual);
        assertEquals(actual.size(), 6);
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "invalidLinesProvider", expectedExceptions = TriangleException.class)
    public void testParseCoordinates_InvalidLine_ThrowsException(String line) throws TriangleException {
        // given - line from data provider

        // when
        parser.parseCoordinates(line);

        // then - Ожидается TriangleException
    }
}