package deque;

import java.util.Comparator;

/**
 * An Array implementation .
 * The MAX Array Deque is a kind of Array Deque.
 * The definition of deque is here: <a href="http://www.cplusplus.com/reference/deque/deque/">...</a>
 * This implementation is for the project 1 of CS61A, the website of project is here:
 * <a href="https://sp21.datastructur.es/materials/proj/proj1/proj1">...</a>
 *
 * @author Daniel Feng
 */
public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator<T> cp;

    public MaxArrayDeque() {
    }
    public MaxArrayDeque(Comparator<T> c) {
        this.cp = c;
    }

    public T max() {
        return max(this.cp);
    }

    public T max(Comparator<T> c) {
        if (size() == 0) {
            return null;
        }
        int maxIdx = 0;
        for (int i = 1; i < size(); i++) {
            if (c.compare(get(i), get(maxIdx)) > 0) {
                maxIdx = i;
            }
        }
        return get(maxIdx);
    }
}
