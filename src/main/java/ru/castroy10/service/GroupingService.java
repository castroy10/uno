package ru.castroy10.service;

import ru.castroy10.model.GroupDto;
import ru.castroy10.model.RowDto;
import ru.castroy10.util.DisjointSetUnion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupingService {
    public List<GroupDto> process(final List<RowDto> allRows) {
        final int rowCount = allRows.size();
        final DisjointSetUnion dsu = new DisjointSetUnion(rowCount);
        final List<Map<String, Integer>> columnMapsList = new ArrayList<>();

        for (int i = 0; i < rowCount; i++) {
            final String[] values = allRows.get(i).getValues();
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
                    dsu.union(i, firstRowWithVal);
                } else {
                    currentColumnMap.put(value, i);
                }
            }
        }

        final Map<Integer, List<RowDto>> groupsMap = new HashMap<>();
        for (int i = 0; i < rowCount; i++) {
            final int root = dsu.find(i);
            groupsMap.computeIfAbsent(root, k -> new ArrayList<>()).add(allRows.get(i));
        }

        return groupsMap.entrySet().stream()
                        .map(entry -> new GroupDto(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparingInt(GroupDto::size).reversed())
                        .collect(Collectors.toList());
    }
}
