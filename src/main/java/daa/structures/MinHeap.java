package daa.structures;

import daa.metrics.Metrics;

public class MinHeap {
    private int[] data = new int[10];
    private int size = 0;
    private final Metrics metrics = new Metrics();

    public void insert(int x) {
        if (size == data.length) grow();
        data[size] = x;
        metrics.moves++;
        bubbleUp(size);
        size++;
    }

    public int peekMin() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        metrics.steps++;
        return data[0];
    }

    public int extractMin() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        int min = data[0];
        metrics.steps++;
        size--;
        if (size > 0) {
            data[0] = data[size];
            metrics.moves++;
            bubbleDown(0);
        }
        return min;
    }

    private void bubbleUp(int i) {
        while (i > 0) {
            int p = (i - 1) / 2;
            metrics.steps += 2;
            metrics.comparisons++;
            if (data[i] < data[p]) {
                swap(i, p);
                i = p;
            } else {
                break;
            }
        }
    }

    private void bubbleDown(int i) {
        while (2 * i + 1 < size) {
            int l = 2 * i + 1, r = l + 1, s = l;
            metrics.steps++;
            if (r < size) {
                metrics.steps++;
                metrics.comparisons++;
                if (data[r] < data[l]) s = r;
            }
            metrics.steps += 2;
            metrics.comparisons++;
            if (data[i] > data[s]) {
                swap(i, s);
                i = s;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        int t = data[i];
        data[i] = data[j];
        data[j] = t;
        metrics.moves += 2;
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

    /** Тестке арналған: a[parent] <= a[child] тексереді */
    public boolean isValidHeap() {
        for (int i = 1; i < size; i++) {
            if (data[(i - 1) / 2] > data[i]) return false;
        }
        return true;
    }

    public int size() { return size; }
    public Metrics metrics() { return metrics; }
}