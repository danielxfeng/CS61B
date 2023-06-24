package deque;

import java.util.Iterator;

/**
 * A Linked List implementation .
 * The Linked List Deque is a kind of double ended queue.
 * The definition is here: <a href="http://www.cplusplus.com/reference/deque/deque/">...</a>
 * This implementation is for the project 1 of CS61A, the website of project is here:
 * <a href="https://sp21.datastructur.es/materials/proj/proj1/proj1">...</a>
 *
 * @author Daniel Feng
 */
public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    private Link<T> sentinel;
    private int size;

    /**
     * The raw recursive Linked List Node class.
     * eg: Link(1, Link(2, Link(3, null)))
     */
    private static class Link<T> {
        public T value;
        public Link<T> prev;
        public Link<T> next;
        public Link(T value, Link<T> prev, Link<T> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    /**
     * the helper Class for support the Iterator;
     */
    private class LinkedListDequeIterator implements Iterator<T> {

        private int index;

        public LinkedListDequeIterator() {
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

    public LinkedListDeque() {
        sentinel = new Link<T>(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    /**
     * Add the item as the first node of the Deque.
     */
    @Override
    public void addFirst(T item) {
        assert null != item : "Cannot add null to a LinkedListDeque!";

        sentinel.next.prev = new Link<T>(item, sentinel, sentinel.next);
        sentinel.next = sentinel.next.prev;
        size++;
    }

    /**
     * Add the item as the last node of the Deque.
     */
    @Override
    public void addLast(T item) {
        assert null != item : "Cannot add null to a LinkedListDeque!";

        sentinel.prev.next = new Link<T>(item, sentinel.prev, sentinel);
        sentinel.prev = sentinel.prev.next;
        size++;
    }

    /**
     * return if the Deque ONLY has SENTINEL node.

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
     */

    /**
     * return the numbers of nodes, exclude SENTINEL.
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
     * Remove the first node from the Deque.
     * @return the value of deleted node.
     */
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T value = sentinel.next.value;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size--;
        return value;
    }

    /**
     * Remove the last node from the Deque.
     * @return the value of deleted node.
     */
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        T value = sentinel.prev.value;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size--;
        return value;
    }

    /**
     * Return the value of index-th node in the Deque.
     */
    @Override
    public T get(int index) {
        if (index < 0) {
            return null;
        }
        Link<T> link = sentinel.next;
        int i = 0;
        while (i < index) {
            link = link.next;
            i++;
        }
        return link.value;
    }

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    /**
     * Return if the Object o is equals to the Deque.
     *
     * @return true when:
     *      1. the Object o is also a Deque.
     *      2. the items value in Object o are same with the values in this Deque.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> other = (Deque<T>) o;
        if (this == other) {
            return true;
        }
        if (other.size() != this.size()) {
            return false;
        }
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return the value of index-th node in the Deque with Recursive Call.
     */
    public T getRecursive(int index) {
        return getRecursive(index, sentinel.next);
    }

    /**
     * The help method of the above getRecursive method to implement the recursive call.
     */
    private T getRecursive(int index, Link<T> link) {
        if (index < 0) {
            return null;
        }
        if (link.value == null) {
            return null;
        }
        if (index == 0) {
            return link.value;
        }
        return getRecursive(index - 1, link.next);
    }
}
