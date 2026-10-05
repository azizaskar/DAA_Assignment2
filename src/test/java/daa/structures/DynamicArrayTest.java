package daa.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {

    @Test
    void growsBeyondInitialCapacityAndKeepsOrder() {
        DynamicArray a = new DynamicArray();
        for (int i = 0; i < 1000; i++) a.add(i);
        assertEquals(1000, a.size());
        for (int i = 0; i < 1000; i++) assertEquals(i, a.get(i));
    }

    @Test
    void addAtIndexShiftsRight() {
        DynamicArray a = new DynamicArray();
        a.add(1); a.add(2); a.add(3);
        a.add(1, 99);                       // [1, 99, 2, 3]
        assertEquals(4, a.size());
        assertEquals(99, a.get(1));
        assertEquals(2, a.get(2));
        assertEquals(3, a.get(3));
    }

    @Test
    void removeShiftsLeft() {
        DynamicArray a = new DynamicArray();
        a.add(1); a.add(2); a.add(3);
        a.remove(0);                        // [2, 3]
        assertEquals(2, a.size());
        assertEquals(2, a.get(0));
        assertEquals(3, a.get(1));
    }

    @Test
    void addAtSizeAppends() {
        DynamicArray a = new DynamicArray();
        a.add(5);
        a.add(a.size(), 6);                 // соңғы индекс
        assertEquals(6, a.get(1));
    }

    @Test
    void invalidIndexThrows() {
        DynamicArray a = new DynamicArray();
        assertThrows(IndexOutOfBoundsException.class, () -> a.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> a.remove(0));
        a.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> a.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> a.add(2, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> a.add(-1, 5));
    }

    @Test
    void containsWithDuplicates() {
        DynamicArray a = new DynamicArray();
        a.add(7); a.add(7); a.add(7);
        assertTrue(a.contains(7));
        a.remove(0); a.remove(0); a.remove(0);
        assertFalse(a.contains(7));
    }

    @Test
    void getCostsOneStepAndHeadInsertMovesAll() {
        DynamicArray a = new DynamicArray();
        for (int i = 0; i < 100; i++) a.add(i);
        a.metrics().reset();
        a.get(50);
        assertEquals(1, a.metrics().steps);
        a.metrics().reset();
        a.add(0, -1);
        assertEquals(100, a.metrics().steps);   // 100 элемент жылжыды
    }
}