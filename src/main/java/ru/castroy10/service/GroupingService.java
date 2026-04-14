package ru.castroy10.service;

import lombok.RequiredArgsConstructor;
import ru.castroy10.util.DisjointSetUnion;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class GroupingService {
    private final FileService fileService;
    private final LineValidatorService lineValidatorService;
    private final LineParserService lineParserService;

    public List<List<String>> process(final Path inputPath) throws IOException {
        final List<String> allLines;
        try (final var lines = fileService.readLines(inputPath)) {
            allLines = lines.toList();
        }

        final int validCount = (int) allLines.stream()
                .filter(line -> !line.isBlank())
                .filter(lineValidatorService::isValid)
                .count();

        if (validCount == 0) {
            return List.of();
        }

        final DisjointSetUnion dsu = new DisjointSetUnion(validCount);
        final List<String> validLines = new ArrayList<>(validCount);
        final List<Map<String, Integer>> columnMapsList = new ArrayList<>();

        for (final String line : allLines) {
            if (line.isBlank() || !lineValidatorService.isValid(line)) {
                continue;
            }

            final int rowIndex = validLines.size();
            validLines.add(line);

            final String[] values = lineParserService.parse(line);
            for (int col = 0; col < values.length; col++) {
                final String value = values[col];
                if (value.isEmpty()) {
                    continue;
                }

                while (columnMapsList.size() <= col) {
                    columnMapsList.add(new HashMap<>());
                }

                final Map<String, Integer> currentColumnMap = columnMapsList.get(col);
                final Integer firstRowWithVal = currentColumnMap.get(value);

                if (firstRowWithVal != null) {
                    dsu.union(rowIndex, firstRowWithVal);
                } else {
                    currentColumnMap.put(value, rowIndex);
                }
            }
        }

        final int[] groupSizes = new int[validCount];
        for (int i = 0; i < validCount; i++) {
            groupSizes[dsu.find(i)]++;
        }

        final Map<Integer, List<String>> groupsMap = new HashMap<>();
        for (int i = 0; i < validCount; i++) {
            final int root = dsu.find(i);
            if (groupSizes[root] > 1) {
                groupsMap.computeIfAbsent(root, k -> new ArrayList<>()).add(validLines.get(i));
            }
        }

        final List<List<String>> sortedGroups = new ArrayList<>(groupsMap.values());
        sortedGroups.sort((l1, l2) -> Integer.compare(l2.size(), l1.size()));
        return sortedGroups;
    }
}
