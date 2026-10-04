package daa.structures;

import daa.metrics.Metrics;

public class MyLinkedList implements MyCollection {
    private static class Node {
        int value;
        Node next;
        Node(int value) { this.value = value; }
    }

    private Node head, tail;
    private int size = 0;
    private final Metrics metrics = new Metrics();

    @Override
    public void add(int x) {
        Node n = new Node(x);
        if (head == null) {
            head = n;
            tail = n;
            metrics.moves += 2;       // head және tail жаңарды
        } else {
            tail.next = n;
            tail = n;
            metrics.moves += 2;       // tail.next және tail жаңарды
        }
        size++;
    }

    @Override
    public void add(int index, int x) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Invalid index: " + index);
        if (index == size) { add(x); return; }
        Node n = new Node(x);
        if (index == 0) {
            n.next = head;
            head = n;
            metrics.moves += 2;
        } else {
            Node prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
                metrics.steps++;      // келесі түйінге өту
            }
            n.next = prev.next;
            prev.next = n;
            metrics.moves += 2;
        }
        size++;
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Invalid index: " + index);
        if (index == 0) {
            head = head.next;
            metrics.moves++;
            if (size == 1) tail = null;
        } else {
            Node prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
                metrics.steps++;
            }
            prev.next = prev.next.next;
            metrics.moves++;
            if (index == size - 1) tail = prev;
        }
        size--;
    }

    @Override
    public int get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Invalid index: " + index);
        Node cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
            metrics.steps++;
        }
        return cur.value;
    }

    @Override
    public boolean contains(int x) {
        Node cur = head;
        while (cur != null) {
            metrics.comparisons++;
            if (cur.value == x) return true;
            cur = cur.next;
            metrics.steps++;
        }
        return false;
    }

    @Override public int size() { return size; }
    @Override public Metrics metrics() { return metrics; }
}