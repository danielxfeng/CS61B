package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

public class MaxArrayDequeTest {


    private static class IntCompare implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            // return o1 % 10 - o2 % 10;
            return o1 - o2;
        }
    }

    private static class StringCompare implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o1.charAt(o1.length() - 1) - o2.charAt(o2.length() - 1);
            // return o1.compareTo(o2);
        }
    }

    private Comparator<Integer> getIntCompare() {
        return new IntCompare();
    }

    private Comparator<String> getStringCompare() {
        return new StringCompare();
    }


    @Test
    public void testInt() {
        MaxArrayDeque<Integer> maxArrayDeque = new MaxArrayDeque<>();
        assertNull(maxArrayDeque.max(getIntCompare()));
        maxArrayDeque.addLast(31);
        maxArrayDeque.addLast(22);
        maxArrayDeque.addLast(13);
        assertEquals(31, (int) maxArrayDeque.max(getIntCompare()));
    }

    @Test
    public void testString() {
        MaxArrayDeque<String> maxArrayDeque = new MaxArrayDeque<>(getStringCompare());
        assertNull(maxArrayDeque.max());
        maxArrayDeque.addLast("aaa");
        maxArrayDeque.addLast("bbb");
        maxArrayDeque.addLast("aac");
        assertEquals("aac", (String) maxArrayDeque.max());
    }
}
