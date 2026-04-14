package ru.castroy10.service;

public class LineValidatorService {
    public boolean isValid(final String line) {
        int quoteCount = 0;
        for (int i = 0; i < line.length(); i++) {
            final char c = line.charAt(i);
            if (c == '"') {
                quoteCount++;
            } else if (c == ';' && quoteCount % 2 != 0) {
                return false;
            }
        }
        return quoteCount % 2 == 0;
    }
}
