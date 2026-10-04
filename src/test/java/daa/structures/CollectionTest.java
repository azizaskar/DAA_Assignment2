package daa.structures;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CollectionTest {
    static Stream<Supplier<MyCollection>> impls() {
        return Stream.<Supplier<MyCollection>>of(DynamicArray::new, MyLinkedList::new);
    }

    @ParameterizedTest @MethodSource("impls")
    void randomOperationsMatchArrayList(Supplier<MyCollection> f) {
        MyCollection c = f.get();
        ArrayList<Integer> ref = new ArrayList<>();
        Random r = new Random(1);
        for (int step = 0; step < 3000; step++) {
            int op = r.nextInt(3);
            if (op == 0 || ref.isEmpty()) {
                int v = r.nextInt(50);
                c.add(v); ref.add(v);
            } else if (op == 1) {
                int idx = r.nextInt(ref.size() + 1), v = r.nextInt(50);
                c.add(idx, v); ref.add(idx, v);
            } else {
                int idx = r.nextInt(ref.size());
                c.remove(idx); ref.remove(idx);
            }
            assertEquals(ref.size(), c.size());
        }
        for (int i = 0; i < ref.size(); i++) assertEquals(ref.get(i), c.get(i));
        for (int v = -1; v < 52; v++) assertEquals(ref.contains(v), c.contains(v));
    }

    @ParameterizedTest @MethodSource("impls")
    void emptyStructure(Supplier<MyCollection> f) {
        MyCollection c = f.get();
        assertEquals(0, c.size());
        assertFalse(c.contains(5));
        assertThrows(IndexOutOfBoundsException.class, () -> c.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> c.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> c.add(1, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> c.add(-1, 5));
    }

    @ParameterizedTest @MethodSource("impls")
    void oneElementAndInvalidIndex(Supplier<MyCollection> f) {
        MyCollection c = f.get();
        c.add(7);
        assertEquals(7, c.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> c.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> c.get(-1));
        c.remove(0);
        assertEquals(0, c.size());
        c.add(9);
        assertEquals(9, c.get(0));
    }

    @ParameterizedTest @MethodSource("impls")
    void duplicatesFirstAndLastIndex(Supplier<MyCollection> f) {
        MyCollection c = f.get();
        c.add(3); c.add(3); c.add(3);
        c.add(0, 1);          // бастапқы индекс
        c.add(c.size(), 9);   // соңғы индекс
        assertEquals(1, c.get(0));
        assertEquals(9, c.get(c.size() - 1));
        assertTrue(c.contains(3));
        c.remove(c.size() - 1);
        c.remove(0);
        assertEquals(3, c.size());
        assertEquals(3, c.get(2));
    }

    @ParameterizedTest @MethodSource("impls")
    void countersAreCounted(Supplier<MyCollection> f) {
        MyCollection c = f.get();
        for (int i = 0; i < 100; i++) c.add(i);
        c.metrics().reset();
        c.add(0, -1);
        assertTrue(c.metrics().moves > 0);   // W3-те 0 болмауы керек
        c.metrics().reset();
        c.contains(-12345);
        assertEquals(101, c.metrics().comparisons);
    }

    @Test
    void growthKeepsData() {
        DynamicArray a = new DynamicArray();
        for (int i = 0; i < 1000; i++) a.add(i);
        for (int i = 0; i < 1000; i++) assertEquals(i, a.get(i));
    }
}