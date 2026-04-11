package ru.castroy10;

import ru.castroy10.model.GroupDto;
import ru.castroy10.model.RowDto;
import ru.castroy10.service.FileService;
import ru.castroy10.service.GroupingService;
import ru.castroy10.service.LineParserService;
import ru.castroy10.service.LineValidatorService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class App {

    public static void main(final String[] args) {
        if (args.length < 1) {
            System.out.println("Использование: java -jar uno-1.0-SNAPSHOT.jar <путь_к_файлу>");
            return;
        }

        final long startTime = System.currentTimeMillis();
        final Path inputPath = Paths.get(args[0]);

        if (!Files.exists(inputPath)) {
            System.err.println("Файл не найден: " + args[0]);
            return;
        }

        final FileService fileService = new FileService();
        final LineValidatorService lineValidatorService = new LineValidatorService();
        final LineParserService lineParserService = new LineParserService();
        final GroupingService groupingService = new GroupingService();

        try (final Stream<String> lines = fileService.readLines(inputPath)) {
            final List<RowDto> allRows = lines
                    .filter(line -> !line.isBlank())
                    .filter(lineValidatorService::isValid)
                    .map(line -> new RowDto(line, lineParserService.parse(line)))
                    .toList();

            if (allRows.isEmpty()) {
                System.err.println("В файле отсутствуют корректные данные для обработки.");
                return;
            }

            final List<GroupDto> groups = groupingService.process(allRows);
            final long countGroups = groups.stream()
                                           .filter(g -> g.size() > 1)
                                           .count();

            fileService.writeGroups(groups, countGroups);

            System.out.println("Групп > 1: " + countGroups);
            final long duration = (System.currentTimeMillis() - startTime) / 1000;
            System.out.println("Время выполнения: " + duration + " сек.");

        } catch (final IOException e) {
            System.err.println("Ошибка при выполнении программы: " + e.getMessage());
        }
    }
}
