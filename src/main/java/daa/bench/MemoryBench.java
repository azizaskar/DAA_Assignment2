package daa.bench;

import daa.structures.DynamicArray;
import daa.structures.MinHeap;
import daa.structures.MyLinkedList;
import org.openjdk.jol.info.GraphLayout;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class MemoryBench {
    public static void main(String[] args) throws IOException {
        new File("results").mkdirs();
        int[] ns = {100, 1000, 10000, 100000};
        try (PrintWriter out = new PrintWriter("results/memory.csv")) {
            out.println("structure,n,bytes,mb,bytes_per_element");
            for (int n : ns) {
                DynamicArray da = new DynamicArray();
                MyLinkedList ll = new MyLinkedList();
                MinHeap h = new MinHeap();
                for (int i = 0; i < n; i++) { da.add(i); ll.add(i); h.insert(i); }
                row(out, "DynamicArray", n, GraphLayout.parseInstance(da).totalSize());
                row(out, "MyLinkedList", n, GraphLayout.parseInstance(ll).totalSize());
                row(out, "MinHeap", n, GraphLayout.parseInstance(h).totalSize());
            }
        }
        System.out.println("Done: results/memory.csv");
    }

    static void row(PrintWriter out, String s, int n, long bytes) {
        out.println(String.format(Locale.US, "%s,%d,%d,%.4f,%.2f", s, n, bytes, bytes / 1048576.0, (double) bytes / n));
    }
}