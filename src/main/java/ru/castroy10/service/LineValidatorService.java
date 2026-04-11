package ru.castroy10.service;

public class LineValidatorService {
    public boolean isValid(final String line) {
        int start = 0;
        int next;
        while ((next = line.indexOf(';', start)) != -1) {
            if (hasOddQuotes(line, start, next)) {
                return false;
            }
            start = next + 1;
        }
        return !hasOddQuotes(line, start, line.length());
    }

    private boolean hasOddQuotes(final String s, final int start, final int end) {
        int count = 0;
        for (int i = start; i < end; i++) {
            if (s.charAt(i) == '"') {
                count++;
            }
        }
        return count % 2 != 0;
    }
}
