/** circular array */

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
    }

    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = item;
        nextFirst = nextFirst == 0 ? items.length - 1 : nextFirst - 1;
        size++;
    }

    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = item;
        nextLast = nextLast == (items.length - 1) ? 0 : nextLast + 1;
        size++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    private void printOneItem(int i) {
        System.out.print(items[i] + " ");
    }

    @Override
    public void printDeque() {
        // "start" handles the case when nextFirst is at the end of the array
        // or when array is full, which would create complexity
        int start = nextFirst + 1;
        if (start == items.length) {
            start = 0;
        }
        // start == nextLast when array is full or empty
        // so need to eliminate the case when array is empty
        if (!isEmpty()) {
            if (start < nextLast) {
                for (int i = start; i < nextLast; i++) {
                    printOneItem(i);
                }
            } else {
                for (int i = start; i < items.length; i++) {
                    printOneItem(i);
                }
                for (int i = 0; i < nextLast; i++) {
                    printOneItem(i);
                }
            }

        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        nextFirst = nextFirst == (items.length - 1) ? 0 : nextFirst + 1;
        T result = items[nextFirst];
        items[nextFirst] = null;
        size--;
        if ((double) size / items.length <= 0.25) {
            if (items.length > 16) {
                resize(items.length / 2);
            }
        }
        return result;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        nextLast = nextLast == 0 ? items.length - 1 : nextLast - 1;
        T result = items[nextLast];
        items[nextLast] = null;
        size--;
        if ((double) size / items.length <= 0.25) {
            if (items.length > 16) {
                resize(items.length / 2);
            }
        }
        return result;
    }

    /**
     * allocate a new array and copy the content of the old array
     * into the middle of the new array, regardless of the position
     * of nextFirst and nextLast in the old array, also readjust the position
     * of nextFirst and nextLast to match the new array
     */
    private void resize(int cap) {
        T[] a = (T[]) new Object[cap];
        // "start" handles the case when nextFirst is at the end of the array
        // or when array is full, which would create complexity
        int start = nextFirst + 1;
        if (start == items.length) {
            start = 0;
        }
        // start position in the new array where we paste the old array into
        int startPosition = (cap - size) / 2;
        if (start < nextLast) {
            System.arraycopy(items, start, a, startPosition, size);
        } else {
            int firstPart = items.length - start;
            System.arraycopy(items, start, a, startPosition, firstPart);
            System.arraycopy(items, 0, a, startPosition + firstPart, size - firstPart);
        }
        nextFirst = startPosition - 1; // startPosition will not be 0
        nextLast = startPosition + size; // nextLast will not >= new array length
        items = a;
    }

    @Override
    public T get(int index) {
        if (isEmpty()) {
            return null;
        }
        int position = nextFirst + 1 + index;
        if (position >= items.length) {
            position -= items.length;
        }
        return items[position];
    }

//    public void printHardCore(){
//        for (int i = 0; i < items.length; i++) {
//            System.out.print(items[i] + " ");
//        }
//        System.out.println();
//    }
//
//    public static void printCombo(ArrayDeque p) {
//        p.printHardCore();
//        System.out.println(p.size());
//        System.out.println(p.get(2));
//        p.printDeque();
//    }
//
//
//    public static void main(String[] args) {
//        ArrayDeque<Integer> tester = new ArrayDeque<>();
//        System.out.println(tester.size());
//        System.out.println(tester.removeFirst());
//        System.out.println(tester.removeLast());
//        System.out.println(tester.get(0));
//        tester.printDeque();
//
//        tester.addFirst(4);
//        tester.addFirst(3);
//        tester.addFirst(2);
//        tester.addLast(5);
//        tester.addLast(6);
//        tester.addLast(7);
//        tester.addFirst(1);
//        tester.addLast(8);
//        tester.addFirst(11);
//        tester.addLast(12);
//        tester.addLast(13);
//        tester.addLast(14);
//        tester.addLast(15);
//        tester.addLast(16);
//        tester.addLast(17);
//        for (int i = 20; i < 30; i++) {
//            tester.addFirst(i);
//        }
//
//        ArrayDeque.printCombo(tester);
//
//        for (int i = 0; i < 22; i++) {
//            tester.removeLast();
//        }
//
//        ArrayDeque.printCombo(tester);
//
//        for (int i = 7; i >= 1; i--) {
//            tester.addFirst(i);
//        }
//        for (int i = 10; i <= 16 ; i++) {
//            tester.addLast(i);
//        }
//
//        ArrayDeque.printCombo(tester);
//
//        for (int i = 0; i < 5; i++) {
//            tester.removeFirst();
//            tester.removeLast();
//        }
//
//        ArrayDeque.printCombo(tester);
//
//        /*
//        for (int i = 0; i < tester.items.length; i++) {
//            System.out.print(tester.items[i] + " ");
//        }
//        System.out.println();
//        */
//    }


}
