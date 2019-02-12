package hw4.puzzle;

import java.util.NoSuchElementException;

public class SpecialPQ<Key> {
    Key[] keys;
    int n;

    public SpecialPQ(int cap) {
        keys = (Key[]) new Object[cap + 1];
        n = 0;
    }

    public SpecialPQ(){
        this(1);
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return keys[1];
    }

    private void resize(int cap) {
        if (cap <= n)
            throw new IllegalArgumentException("capacity must be larger than size");
        Key[] tmp = (Key[]) new Object[cap + 1];
        for (int i = 1; i <= n; i++) {
            tmp[i] = keys[i];
        }
        keys = tmp;
    }

    public void insert(Key key) {
        if (n == keys.length - 1) resize(2 * keys.length);
        keys[++n] = key;
        swim(n);
    }

    public Key delMin() {
        if (isEmpty())
            throw new NoSuchElementException("priority queue is empty");
        Key k = keys[1];
        exch(1, n--);
        sink(1);
        keys[n + 1] = null;
        if ((n > 0) && (n == (keys.length - 1) / 4)) resize(keys.length / 2);
        return k;
    }

    private boolean greater(int i, int j) {
        return ((Comparable<Key>) keys[i]).compareTo(keys[j]) > 0;
    }

    private void exch(int i, int j) {
        Key tmp = keys[i];
        keys[i] = keys[j];
        keys[j] = keys[i];
    }

    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (k * 2 <= n) {
            int j = 2 * k;
            if (j < n && greater(j, j + 1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }
}
