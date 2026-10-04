package daa.bench;

import daa.metrics.Metrics;
import daa.structures.DynamicArray;
import daa.structures.MinHeap;
import daa.structures.MyCollection;
import daa.structures.MyLinkedList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class Benchmark {
    static final int[] NS = {100, 1000, 10000, 100000};
    static final int RUNS = 6;            // 1 warm-up (тасталады) + 5 өлшеу
    static final long SEED = 42;
    static final String[] NAMES = {"DynamicArray", "MyLinkedList"};
    static volatile long sink;            // JIT есептеуді өшіріп тастамауы үшін

    interface Case { Result run(); }

    static class Result {
        final double ms;
        final Metrics m;
        Result(double ms, Metrics m) { this.ms = ms; this.m = m; }
    }

    static PrintWriter out;

    static MyCollection make(int which) {
        return which == 0 ? new DynamicArray() : new MyLinkedList();
    }

    public static void main(String[] args) throws IOException {
        new File("results").mkdirs();
        out = new PrintWriter(new FileWriter("results/results.csv"));
        out.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");

        for (int n : NS) {
            System.out.println("n = " + n);
            w1(n);
            w2(n);
            w3(n, "head");
            w3(n, "middle");
            w4(n);
        }
        out.close();
        System.out.println("Done: results/results.csv");
    }

    // W1: n элемент толтыру, 10 000 кездейсоқ get(index)
    static void w1(int n) {
        for (int s = 0; s < 2; s++) {
            final int sid = s;
            record("W1", "-", NAMES[s], n, () -> {
                MyCollection c = make(sid);
                Random r = new Random(SEED);
                for (int i = 0; i < n; i++) c.add(r.nextInt());
                int[] idx = new int[10000];
                for (int i = 0; i < idx.length; i++) idx[i] = r.nextInt(n);
                c.metrics().reset();
                long t = System.nanoTime();
                long acc = 0;
                for (int i = 0; i < idx.length; i++) acc += c.get(idx[i]);
                double ms = (System.nanoTime() - t) / 1e6;
                sink = acc;
                return new Result(ms, c.metrics());
            });
        }
    }

    // W2: 1000 contains, жартысы бар (жұп сан), жартысы жоқ (тақ сан)
    static void w2(int n) {
        for (int s = 0; s < 2; s++) {
            final int sid = s;
            record("W2", "-", NAMES[s], n, () -> {
                MyCollection c = make(sid);
                Random r = new Random(SEED);
                int[] vals = new int[n];
                for (int i = 0; i < n; i++) {
                    vals[i] = r.nextInt(1_000_000) * 2;
                    c.add(vals[i]);
                }
                int[] q = new int[1000];
                for (int i = 0; i < q.length; i++) {
                    q[i] = (i % 2 == 0) ? vals[r.nextInt(n)] : r.nextInt(1_000_000) * 2 + 1;
                }
                c.metrics().reset();
                long t = System.nanoTime();
                int found = 0;
                for (int i = 0; i < q.length; i++) if (c.contains(q[i])) found++;
                double ms = (System.nanoTime() - t) / 1e6;
                sink = found;
                return new Result(ms, c.metrics());
            });
        }
    }

    // W3: 1000 add(idx) + 1000 remove(idx), idx = 0 немесе n/2
    static void w3(int n, String variant) {
        final int idx = variant.equals("head") ? 0 : n / 2;
        for (int s = 0; s < 2; s++) {
            final int sid = s;
            record("W3", variant, NAMES[s], n, () -> {
                MyCollection c = make(sid);
                for (int i = 0; i < n; i++) c.add(i);
                c.metrics().reset();
                long t = System.nanoTime();
                for (int i = 0; i < 1000; i++) c.add(idx, -1);
                for (int i = 0; i < 1000; i++) c.remove(idx);
                double ms = (System.nanoTime() - t) / 1e6;
                sink = c.size();
                return new Result(ms, c.metrics());
            });
        }
    }

    // W4: n insert, n extractMin, өспейтін емес реттілікті тексеру
    static void w4(int n) {
        record("W4", "-", "MinHeap", n, () -> {
            MinHeap h = new MinHeap();
            Random r = new Random(SEED);
            int[] vals = new int[n];
            for (int i = 0; i < n; i++) vals[i] = r.nextInt();
            long t = System.nanoTime();
            for (int i = 0; i < n; i++) h.insert(vals[i]);
            int prev = Integer.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                int v = h.extractMin();
                if (v < prev) throw new IllegalStateException("Heap output not sorted!");
                prev = v;
            }
            double ms = (System.nanoTime() - t) / 1e6;
            sink = prev;
            return new Result(ms, h.metrics());
        });
    }

    static void record(String wl, String variant, String structure, int n, Case c) {
        double[] times = new double[RUNS - 1];
        Result last = null;
        for (int run = 0; run < RUNS; run++) {
            Result res = c.run();
            if (run > 0) times[run - 1] = res.ms;   // 0-ші (warm-up) тасталады
            last = res;
        }
        Arrays.sort(times);
        double median = times[times.length / 2];
        out.println(String.format(Locale.US, "%s,%s,%s,%d,%.3f,%d,%d,%d",
                wl, variant, structure, n, median,
                last.m.steps, last.m.moves, last.m.comparisons));
    }
}