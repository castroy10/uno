package ru.castroy10;

import ru.castroy10.service.FileService;
import ru.castroy10.service.GroupingService;
import ru.castroy10.service.LineParserService;
import ru.castroy10.service.LineValidatorService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
        final GroupingService groupingService = new GroupingService(
                fileService,
                new LineValidatorService(),
                new LineParserService()
        );

        try {
            final List<List<String>> groups = groupingService.process(inputPath);

            if (groups.isEmpty()) {
                System.out.println("Групп не найдено.");
                return;
            }

            fileService.writeGroups(groups);

            System.out.println("Групп > 1: " + groups.size());
            final long duration = (System.currentTimeMillis() - startTime) / 1000;
            System.out.println("Время выполнения: " + duration + " сек.");

        } catch (final IOException e) {
            System.err.println("Ошибка при выполнении программы: " + e.getMessage());
        }
    }

}
