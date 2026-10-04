package daa.structures;

import daa.metrics.Metrics;

public interface MyCollection {
    void add(int x);
    void add(int index, int x);
    void remove(int index);
    int get(int index);
    boolean contains(int x);
    int size();
    Metrics metrics();
}