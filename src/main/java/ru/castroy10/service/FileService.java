package ru.castroy10.service;

import ru.castroy10.model.GroupDto;
import ru.castroy10.model.RowDto;

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

    public void writeGroups(final List<GroupDto> groups, final long countGroupsGreaterOne) throws IOException {
        try (final BufferedWriter writer = Files.newBufferedWriter(Paths.get(OUTPUT_FILE_NAME), StandardCharsets.UTF_8)) {
            writer.write("Число групп с более чем одним элементом: " + countGroupsGreaterOne);
            writer.newLine();
            writer.newLine();

            int groupNum = 1;
            for (final GroupDto group : groups) {
                if (group.size() > 1) {
                    writer.write("Группа " + groupNum++);
                    writer.newLine();
                    for (final RowDto row : group.getRows()) {
                        writer.write(row.getOriginalLine());
                        writer.newLine();
                    }
                    writer.newLine();
                }
            }
        }
    }
}
