package ru.castroy10.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class FileService {
    private static final String OUTPUT_FILE_NAME = "out.txt";

    public Stream<String> readLines(final Path path) throws IOException {
        return Files.lines(path, StandardCharsets.UTF_8);
    }

    public void writeGroups(final List<List<String>> groups) throws IOException {
        try (final BufferedWriter writer = Files.newBufferedWriter(Paths.get(OUTPUT_FILE_NAME), StandardCharsets.UTF_8)) {
            writer.write("Число групп с более чем одним элементом: " + groups.size());
            writer.newLine();
            writer.newLine();

            int groupNum = 1;
            for (final List<String> group : groups) {
                writer.write("Группа " + groupNum++);
                writer.newLine();
                for (final String line : group) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.newLine();
            }
        }
    }
}
