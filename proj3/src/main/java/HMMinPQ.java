import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class HMMinPQ<Key extends Comparable<Key>> implements Iterable<Long> {
//    private long maxN;
    private int n;
    private Map<Integer, Long> pq;
    private Map<Long, Integer> qp;
    private Map<Long, Key> keys;

    public HMMinPQ() {
//        if (maxN < 0) throw new IllegalArgumentException();
//        this.maxN = maxN;
        n = 0;
        pq = new HashMap<Integer, Long>();
        qp = new HashMap<Long, Integer>();
        keys = new HashMap<Long, Key>();


    }

    public boolean isEmpty() {
        return n == 0;
    }

    public boolean contains(long i) {
        return qp.containsKey(i);
    }

    public int size() {
        return n;
    }

    public void insert(long i, Key key) {
        if (contains(i)) throw new IllegalArgumentException("id already in pq");
        pq.put(++n, i);
        qp.put(i, n);
        keys.put(i, key);
        swim(n);
    }

    public long delMin() {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        long min = pq.get(1);
        exch(1, n--);
        sink(1);
        qp.remove(min);
        keys.remove(min);
        pq.remove(n + 1);
        return min;
    }

    public void decreaseKey(long i, Key key) {
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        if (keys.get(i).compareTo(key) <= 0)
            throw new IllegalArgumentException("Calling decreaseKey() with given argument would not strictly decrease the key");
        keys.put(i, key);
        swim(qp.get(i));
    }

    private boolean greater(int i, int j) {
        return keys.get(pq.get(i)).compareTo(keys.get(pq.get(j))) > 0;
    }

    private void exch(int i, int j) {
        long swap = pq.get(i);
        pq.put(i, pq.get(j));
        pq.put(j, swap);
        qp.put(pq.get(i), i);
        qp.put(pq.get(j), j);
    }

    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    public Iterator<Long> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Long> {
        private HMMinPQ<Key> copy;

        public HeapIterator() {
            copy = new HMMinPQ<Key>();
            for (int i = 1; i <= n; i++) {
                copy.insert(pq.get(i), keys.get(pq.get(i)));
            }
        }

        public boolean hasNext() {
            return !copy.isEmpty();
        }

        public Long next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }
    }

//    public static void main(String[] args) {
//        // insert a bunch of strings
//        String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };
//
//        HMMinPQ<String> pq = new HMMinPQ<String>();
//        for (int i = 0; i < strings.length; i++) {
//            pq.insert((long)i, strings[i]);
//        }
//
//        // delete and print each key
//        while (!pq.isEmpty()) {
//            long i = pq.delMin();
//            System.out.println(i + " " + strings[(int)i]);
//        }
//        System.out.println();
//
//        // reinsert the same strings
//        for (int i = 0; i < strings.length; i++) {
//            pq.insert((long)i, strings[i]);
//        }
//
//        // print each key using the iterator
//        for (long i : pq) {
//            System.out.println(i + " " + strings[(int)i]);
//        }
//        while (!pq.isEmpty()) {
//            pq.delMin();
//        }
//
//    }
}
