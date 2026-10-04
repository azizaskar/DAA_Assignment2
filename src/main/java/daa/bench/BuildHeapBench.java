package daa.bench;

import daa.structures.MinHeap;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class BuildHeapBench {
    static final int[] NS = {100, 1000, 10000, 100000};
    static final int RUNS = 6;   // 1 warm-up + 5

    public static void main(String[] args) throws IOException {
        new File("results").mkdirs();
        try (PrintWriter out = new PrintWriter("results/buildheap.csv")) {
            out.println("method,n,time_ms,steps,moves,comparisons");
            for (int n : NS) {
                int[] arr = new int[n];
                Random r = new Random(42);
                for (int i = 0; i < n; i++) arr[i] = r.nextInt();
                write(out, "n_inserts", n, arr, false);
                write(out, "buildHeap", n, arr, true);
            }
        }
        System.out.println("Done: results/buildheap.csv");
    }

    static void write(PrintWriter out, String method, int n, int[] arr, boolean floyd) {
        double[] t = new double[RUNS - 1];
        MinHeap last = null;
        for (int run = 0; run < RUNS; run++) {
            MinHeap h = new MinHeap();
            long s = System.nanoTime();
            if (floyd) h.buildHeap(arr);
            else for (int x : arr) h.insert(x);
            double ms = (System.nanoTime() - s) / 1e6;
            if (run > 0) t[run - 1] = ms;
            last = h;
        }
        Arrays.sort(t);
        out.println(String.format(Locale.US, "%s,%d,%.3f,%d,%d,%d", method, n,
                t[t.length / 2], last.metrics().steps, last.metrics().moves, last.metrics().comparisons));
    }
}