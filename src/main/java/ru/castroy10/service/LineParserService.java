package ru.castroy10.service;

public class LineParserService {
    private static final int KEEP_EMPTY_VALUE = -1;

    public String[] parse(final String line) {
        final String[] parts = line.split(";", KEEP_EMPTY_VALUE);
        for (int i = 0; i < parts.length; i++) {
            final String value = parts[i].trim();
            if (value.isEmpty() || value.equals("\"\"")) {
                parts[i] = "";
            } else {
                parts[i] = value.intern();
            }
        }
        return parts;
    }
}
