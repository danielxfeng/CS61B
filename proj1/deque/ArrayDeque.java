package deque;

import java.util.Arrays;
import java.util.Iterator;

/**
 * An Array implementation .
 * This implementation is for the project 1 of CS61A, the website of project is here:
 * <a href="https://sp21.datastructur.es/materials/proj/proj1/proj1">...</a>
 *
 * @author Daniel Feng
 */
public class ArrayDeque<T> implements Deque<T>, Iterable<T> {

    private final static int START_SIZE = 8;
    private T[] array;
    private int size;

    /**
     * This is a Doubly Deque that is implemented by Array.
     * For speeding up the methods of addFirst() and removeFirst(),
     * we should use the "circle array".
     *
     * eg:
     *      for a Deque which is [1, 2, 3, 4], the array is:
     *      items in Array:  Tail| 4, 5, 1, 2 |Head
     *      arr index:             0  1  2  3
     *
     *      before insert items: nextFirst: 1 nextLast: 2 size: 0
     *      after addLast 1:     nextFirst: 1 nextLast: 3 size: 1
     *      after addLast 2:     nextFirst: 1 nextLast: 0 size: 2
     *
     */
    private int nextFirst;
    private int nextLast;

    private class ArrayDequeIterator implements Iterator<T> {

        private int index;

        public ArrayDequeIterator() {
            index = 0;
        }

        /** Return if there is item left. */
        @Override
        public boolean hasNext() {
            return index < size();
        }

        /** Return the next item. */
        @Override
        public T next() {
            T res = get(index);
            index++;
            return res;
        }
    }

    public ArrayDeque() {
        array = (T[]) new Object[START_SIZE];
        size = 0;
        initArrPos();
    }

    private void initArrPos(){
        nextFirst = array.length - 1;
        nextLast = size();
    }

    /**
     * Insert the item to the first of the Deque.
     * Just create a new array because of the limitation of Array.
     */
    @Override
    public void addFirst(T item) {
        adjustSize();
        array[nextFirst] = item;
        nextFirst = getArrIndex(nextFirst, -1);
        size++;
    }

    /**
     * Returns the array index of the deque WITH shift support.
     * For the circle array.
     *
     * @param index deque index;
     * @param shift the shift value;
     *
     * @return array index;
     */
    private int getArrIndex(int index, int shift) {
        return getArrIndex(index + shift);
    }

    /**
     * Returns the array index of the deque WITHOUT shift support.
     * For the circle array.
     *
     * @param index deque index;
     *
     * @return array index;
     */
    private int getArrIndex(int index) {
        if (index < 0) {
            return array.length + index;
        } else if (index >= array.length) {
            return index - array.length;
        }
        return index;
    }

    /**
     * Adjust the array size when it is full or has too much space.
     * Just create a new array because of the limitation of Array.
     *
     */
    private void adjustSize() {
        int newSize = getProperSize();
        if (newSize == 0) { // size is OK, no adjustment.
            return;
        }

        // adjustment
        T[] newArr = (T[]) new Object[newSize];

        // part a: |123| of |456 123|
        int aStart = getArrIndex(nextFirst, 1);
        int aLen = Math.min(array.length - aStart, size());
        System.arraycopy(array, aStart, newArr, 0, aLen);

        // part b
        System.arraycopy(array, 0, newArr, aLen, size() - aLen);

        array = newArr;
        initArrPos();
    }

    /**
     * Return the value of the proper array size.
     * If the array size is too small or too large, return the suggest value.
     * Return 0 if the current size is OK.
     */
    private int getProperSize() {
        int expandFactor = 2; // expand the array by * expandFactor when necessary;
        int shrinkFactor = 2; // shrink the array by / expandFactor when necessary;

        if (size() == array.length) { // expand
            return array.length * expandFactor;
        } else if (array.length > START_SIZE && size() < (array.length / 4)) { // shrink
            return Math.max(START_SIZE, array.length / shrinkFactor);
        }
        return 0; // no adjustment
    }

    /**
     * Append the item to the end of the Deque.
     */
    @Override
    public void addLast(T item) {
        adjustSize();
        array[nextLast] = item;
        nextLast = getArrIndex(nextLast, 1);
        size++;
    }

    /**
     * Return if the Deque is empty.

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
     */

    /**
     * Return if the size of Deque.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * The method to print the LinkedList.
     */
    @Override
    public void printDeque() {
        System.out.println(this);
    }

    /**
     * Return the string of the Deque.
     * --> 1 2 3
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size() - 1; i++) {
            sb.append(get(i));
            sb.append(" ");
        }
        sb.append(get(size() - 1));
        return sb.toString();
    }

    /**
     * Remove the first item of the Deque.
     * Just create a new array because of the limitation of Array.
     *
     * @return the deleted item.
     */
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        int arrIdx = getArrIndex(nextFirst, 1);
        nextFirst = arrIdx;
        return remove(arrIdx);
    }

    /**
     * Remove the last item of the Deque.
     *
     * @return the deleted item.
     */
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        int arrIdx = getArrIndex(nextLast, -1);
        nextLast = arrIdx;
        return remove(arrIdx);
    }

    /**
     * Return the arrIdx-th value from array.
     */
    private T remove(int arrIdx) {
        T value = array[arrIdx];
        array[arrIdx] = null;
        size--;
        adjustSize();
        return value;
    }

    /**
     * Return the index-th item from the Deque.
     */
    @Override
    public T get(int index) {
        if (index >= size() || index < 0) {
            return null;
        }
        int arrIdx = getArrIndex(index, nextFirst + 1);
        return array[arrIdx];
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Deque)) {
            System.out.println("instance diff!");
            return false;
        }
        Deque<T> other = (Deque<T>) o;
        if (this == other) {
            return true;
        }
        if (other.size() != this.size()) {
            System.out.println("size diff!");
            return false;
        }
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).equals(other.get(i))) {
                System.out.println("content diff!" + i);
                return false;
            }
        }
        return true;
    }
}
