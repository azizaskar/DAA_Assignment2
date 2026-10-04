package daa.structures;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MinHeapTest {
    @Test
    void emptyHeapThrows() {
        MinHeap h = new MinHeap();
        assertThrows(IllegalStateException.class, h::peekMin);
        assertThrows(IllegalStateException.class, h::extractMin);
    }

    @Test
    void singleElement() {
        MinHeap h = new MinHeap();
        h.insert(5);
        assertEquals(5, h.peekMin());
        assertEquals(5, h.extractMin());
        assertEquals(0, h.size());
    }

    @Test
    void heapPropertyAndSortedOutput() {
        Random r = new Random(7);
        int n = 2000;
        int[] vals = new int[n];
        MinHeap h = new MinHeap();
        for (int i = 0; i < n; i++) {
            vals[i] = r.nextInt(100);          // қайталанатын мәндер бар
            h.insert(vals[i]);
            assertTrue(h.isValidHeap());       // әр insert-тен кейін
        }
        Arrays.sort(vals);
        for (int i = 0; i < n; i++) {
            assertEquals(vals[i], h.extractMin());
            assertTrue(h.isValidHeap());       // әр extractMin-нен кейін
        }
        assertEquals(0, h.size());
    }

    @Test
    void buildHeapWorks() {
        Random r = new Random(3);
        int[] arr = new int[1000];
        for (int i = 0; i < arr.length; i++) arr[i] = r.nextInt(100);
        MinHeap h = new MinHeap();
        h.buildHeap(arr);
        assertTrue(h.isValidHeap());
        int[] sorted = arr.clone();
        Arrays.sort(sorted);
        for (int v : sorted) assertEquals(v, h.extractMin());
    }
}