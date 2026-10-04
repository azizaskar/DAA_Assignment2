package daa.structures;

import daa.metrics.Metrics;

public class DynamicArray implements MyCollection {
    private int[] data = new int[10];
    private int size = 0;
    private final Metrics metrics = new Metrics();

    @Override
    public void add(int x) {
        if (size == data.length) grow();
        data[size++] = x;
        metrics.moves++;
    }

    @Override
    public void add(int index, int x) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Invalid index: " + index);
        if (size == data.length) grow();
        for (int i = size; i > index; i--) {
            metrics.steps++;          // data[i-1] оқу
            data[i] = data[i - 1];
            metrics.moves++;          // элемент жылжыды
        }
        data[index] = x;
        metrics.moves++;
        size++;
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Invalid index: " + index);
        for (int i = index; i < size - 1; i++) {
            metrics.steps++;
            data[i] = data[i + 1];
            metrics.moves++;
        }
        size--;
    }

    @Override
    public int get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Invalid index: " + index);
        metrics.steps++;
        return data[index];
    }

    @Override
    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            metrics.steps++;
            metrics.comparisons++;
            if (data[i] == x) return true;
        }
        return false;
    }

    private void grow() {
        int[] bigger = new int[data.length * 2];
        for (int i = 0; i < size; i++) {
            metrics.steps++;
            bigger[i] = data[i];
            metrics.moves++;
        }
        data = bigger;
    }

    @Override public int size() { return size; }
    @Override public Metrics metrics() { return metrics; }
}