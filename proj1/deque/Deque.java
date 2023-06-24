package deque;


/**
 * A Deque implementation .
 * The definition of deque is here:
 * <a href="http://www.cplusplus.com/reference/deque/deque/">...</a>
 * This implementation is for the project 1 of CS61A, the website of project is here:
 * <a href="https://sp21.datastructur.es/materials/proj/proj1/proj1">...</a>
 *
 * @author Daniel Feng
 */
public interface Deque<T> {
    void addFirst(T item);
    void addLast(T item);
    default boolean isEmpty() {
        return size() == 0;
    }
    int size();
    void printDeque();
    T removeFirst();
    T removeLast();
    T get(int index);
}
