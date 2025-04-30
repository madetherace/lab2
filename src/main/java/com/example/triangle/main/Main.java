package com.example.triangle.main;

import com.example.triangle.entity.Triangle;
import com.example.triangle.exception.TriangleException;
import com.example.triangle.factory.TriangleFactory;
import com.example.triangle.factory.TriangleFactoryImpl;
import com.example.triangle.observer.Observer;
import com.example.triangle.parser.TriangleStringParser;
import com.example.triangle.reader.TriangleFileReader;
import com.example.triangle.repository.TriangleRepository; // Используем репозиторий
import com.example.triangle.service.TriangleTypeService;
import com.example.triangle.specification.*; // Импортируем спецификации
import com.example.triangle.type.TriangleType;
import com.example.triangle.warehouse.TriangleMetrics;
import com.example.triangle.warehouse.TriangleWarehouse; // Используем хранилище
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final String INPUT_FILE_PATH = "data/triangles.txt";

    public static void main(String[] args) {
        logger.info("Triangle application starting with Repository and Warehouse...");

        // --- Получаем Singleton экземпляры ---
        TriangleRepository repository = TriangleRepository.getInstance();
        TriangleWarehouse warehouse = TriangleWarehouse.getInstance();

        // --- Регистрируем Warehouse как наблюдателя за Repository ---
        repository.attach(warehouse);
        logger.info("Warehouse attached as observer to Repository.");

        // Очистим репозиторий и хранилище на случай повторного запуска в той же JVM (редко, но возможно)
        repository.clear();
        warehouse.clearAllMetrics();


        // --- Остальные компоненты ---
        TriangleFileReader reader = new TriangleFileReader();
        TriangleStringParser parser = new TriangleStringParser();
        TriangleFactory factory = new TriangleFactoryImpl(); // Фабрика остается

        List<String> linesWithError = new ArrayList<>();
        List<Triangle> createdTriangles = new ArrayList<>(); // Временный список для добавления в репозиторий

        // --- Чтение, парсинг, создание ---
        try {
            List<String> rawLines = reader.readLines(INPUT_FILE_PATH);
            logger.info("Read {} lines from the file.", rawLines.size());

            for (String line : rawLines) {
                try {
                    List<Double> coordinates = parser.parseCoordinates(line);
                    Triangle triangle = factory.createTriangle(coordinates);
                    createdTriangles.add(triangle); // Сначала собираем, потом добавляем в репозиторий
                } catch (TriangleException e) {
                    logger.error("Skipping line due to error: '{}'. Reason: {}", line, e.getMessage());
                    linesWithError.add(line);
                }
            }

            // --- Добавление созданных треугольников в репозиторий ---
            // Это вызовет уведомление Warehouse для каждого треугольника
            repository.addAll(createdTriangles);

            logger.info("Finished processing file. Repository size: {}. Warehouse size: {}", repository.size(), warehouse.size());
            if (!linesWithError.isEmpty()) {
                logger.warn("Skipped {} lines due to errors.", linesWithError.size());
            }

            // --- Демонстрация работы ---
            if (repository.size() > 0) {
                // 1. Получение всех треугольников
                System.out.println("\n--- All Triangles in Repository (" + repository.size() + ") ---");
                repository.getAll().forEach(System.out::println);

                // 2. Демонстрация Warehouse
                System.out.println("\n--- Metrics from Warehouse ---");
                for (Triangle t : repository.getAll()) {
                    Optional<TriangleMetrics> metricsOpt = warehouse.getMetrics(t.getTriangleId());
                    metricsOpt.ifPresent(metrics ->
                            System.out.printf("ID: %d, %s%n", t.getTriangleId(), metrics)
                    );
                    if (!metricsOpt.isPresent()) { // Проверка, если вдруг метрики не посчитались
                        System.out.printf("ID: %d, Metrics not found in Warehouse!%n", t.getTriangleId());
                    }
                }

                // 3. Демонстрация Спецификаций
                System.out.println("\n--- Query Examples using Specifications ---");

                // Найти по ID (например, первый добавленный, если ID начинаются с 1)
                long firstId = repository.getAll().get(0).getTriangleId();
                Specification<Triangle> idSpec = new TriangleIdSpecification(firstId);
                System.out.println("Query: Find by ID " + firstId);
                repository.query(idSpec).forEach(System.out::println);

                // Найти все равнобедренные (ISOSCELES)
                Specification<Triangle> typeSpec = new TriangleTypeSpecification(TriangleType.ISOSCELES);
                System.out.println("\nQuery: Find ISOSCELES triangles");
                repository.query(typeSpec).forEach(System.out::println);

                // Найти треугольники с площадью от 5 до 10
                Specification<Triangle> areaSpec = new AreaRangeSpecification(5.0, 10.0);
                System.out.println("\nQuery: Find triangles with Area between 5.0 and 10.0");
                repository.query(areaSpec).forEach(t -> {
                    Optional<TriangleMetrics> m = warehouse.getMetrics(t.getTriangleId());
                    System.out.println(t + " " + m.orElse(new TriangleMetrics(Double.NaN, Double.NaN))); // Показать метрики
                });

                // Найти треугольники в первом квадранте
                Specification<Triangle> quadrantSpec = new FirstQuadrantSpecification();
                System.out.println("\nQuery: Find triangles in the First Quadrant");
                repository.query(quadrantSpec).forEach(System.out::println);

                // Комбинированная спецификация: Прямоугольные ИЛИ Равносторонние
                Specification<Triangle> rightSpec = new TriangleTypeSpecification(TriangleType.RIGHT_ANGLED);
                Specification<Triangle> equiSpec = new TriangleTypeSpecification(TriangleType.EQUILATERAL);
                Specification<Triangle> combinedSpec = rightSpec.or(equiSpec);
                System.out.println("\nQuery: Find RIGHT_ANGLED or EQUILATERAL triangles");
                repository.query(combinedSpec).forEach(System.out::println);


                // 4. Демонстрация Сортировки
                System.out.println("\n--- Sorting Examples ---");

                System.out.println("Sorted by ID:");
                repository.sortById().forEach(t -> System.out.println(" ID: " + t.getTriangleId()));

                System.out.println("\nSorted by Area:");
                repository.sortByArea().forEach(t -> {
                    Optional<TriangleMetrics> m = warehouse.getMetrics(t.getTriangleId());
                    System.out.println(" ID: " + t.getTriangleId() + ", " + m.orElse(new TriangleMetrics(Double.NaN, Double.NaN)));
                });

                System.out.println("\nSorted by Perimeter:");
                repository.sortByPerimeter().forEach(t -> {
                    Optional<TriangleMetrics> m = warehouse.getMetrics(t.getTriangleId());
                    System.out.println(" ID: " + t.getTriangleId() + ", " + m.orElse(new TriangleMetrics(Double.NaN, Double.NaN)));
                });

                System.out.println("\nSorted by Point A X-coordinate:");
                repository.sortByPointACoordinateX().forEach(t -> System.out.println(" ID: " + t.getTriangleId() + ", PointA.X: " + t.getPointA().getX()));

                System.out.println("\nSorted by Type:");
                repository.sortByType().forEach(t -> {
                    TriangleType type = new TriangleTypeService().determineType(t); // Переопределяем тип для вывода
                    System.out.println(" ID: " + t.getTriangleId() + ", Type: " + type);
                });


            } else {
                logger.warn("Repository is empty. No operations to demonstrate.");
                System.out.println("Repository is empty. Cannot demonstrate queries or sorting.");
            }

        } catch (TriangleException e) {
            logger.fatal("A critical error occurred during file processing: {}", e.getMessage(), e);
            System.err.println("Application failed during file processing: " + e.getMessage());
        } catch (Exception e) {
            logger.fatal("An unexpected error occurred: {}", e.getMessage(), e);
            System.err.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            // Отписываем наблюдателя (хорошая практика, хотя для Singleton не так критично)
            repository.detach(warehouse);
            logger.info("Warehouse detached from Repository.");
        }

        logger.info("Triangle application finished.");
    }
}