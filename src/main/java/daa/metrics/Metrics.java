package daa.metrics;

public class Metrics {
    public long steps;        // массив ұяшығын оқу / келесі түйінге өту
    public long moves;        // элементті жылжыту / сілтемені жаңарту
    public long comparisons;  // екі элементті салыстыру

    public void reset() {
        steps = 0;
        moves = 0;
        comparisons = 0;
    }
}