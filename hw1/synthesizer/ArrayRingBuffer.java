package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        this.fillCount = 0;
        this.capacity = capacity;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (capacity() == fillCount()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        rb[last] = x;
        last = last == (capacity - 1) ? 0 : last + 1;
        fillCount++;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        if (capacity() == 0) {
            throw new RuntimeException("Ring buffer underflow");
        }
        if (isEmpty()) {
            throw new RuntimeException("Cannot dequeue empty queue");
        }
        T item = rb[first];
        rb[first] = null;
        first = first == (capacity - 1) ? 0 : first + 1;
        fillCount--;
        return item;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (capacity() == 0) {
            throw new RuntimeException("No item to return");
        }
        if (isEmpty()) {
            throw new RuntimeException("Cannot peek empty queue");
        }
        return rb[first];
    }

    public Iterator<T> iterator() {
        return new KeyIterator();
    }

    private class KeyIterator implements Iterator<T> {
        private int ptr;
        private int cnt;

        public KeyIterator() {
            ptr = first;
            cnt = 0;
        }

        public boolean hasNext() {
            return cnt != fillCount;
        }

        public T next() {
            T item = rb[ptr];
            ptr = ptr == (capacity - 1) ? 0 : ptr + 1;
            cnt++;
            return item;
        }
    }
}
