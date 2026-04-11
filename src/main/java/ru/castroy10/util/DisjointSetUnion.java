package ru.castroy10.util;

public class DisjointSetUnion {
    private final int[] parent;

    public DisjointSetUnion(final int n) {
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    public int find(int i) {
        while (parent[i] != i) {
            parent[i] = parent[parent[i]];
            i = parent[i];
        }
        return i;
    }

    public void union(final int i, final int j) {
        final int rootI = find(i);
        final int rootJ = find(j);
        if (rootI != rootJ) {
            parent[rootI] = rootJ;
        }
    }
}
