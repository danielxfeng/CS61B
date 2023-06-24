package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());
		lld1.printDeque();

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {


        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());


    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }

    @Test
    public void testAddFirstAndRemoveFirst(){
        LinkedListDeque<Integer> linkedListDeque = new LinkedListDeque<>();
        assertTrue(linkedListDeque.isEmpty());
        for (int i = 0; i < 3; i++) {
            linkedListDeque.addFirst(i);
            assertFalse(linkedListDeque.isEmpty());
            assertEquals(i + 1, linkedListDeque.size());
            assertEquals(i, (int) linkedListDeque.get(0));
            assertEquals(i, (int) linkedListDeque.getRecursive(0));
        }
        assertEquals("2 1 0", linkedListDeque.toString());
        linkedListDeque.printDeque();
        for (int i = 2; i >= 0; i--) {
            assertEquals(i, (int) linkedListDeque.removeFirst());
            assertEquals(i, linkedListDeque.size());
        }
        assertNull(linkedListDeque.removeFirst());
        assertNull(linkedListDeque.get(0));
        assertNull(linkedListDeque.getRecursive(0));
        assertEquals("null", linkedListDeque.toString());
        linkedListDeque.printDeque();
    }

    @Test
    public void testAddLastAndRemoveLast(){
        LinkedListDeque<Integer> linkedListDeque = new LinkedListDeque<>();
        assertTrue(linkedListDeque.isEmpty());
        for (int i = 0; i < 3; i++) {
            linkedListDeque.addLast(i);
            assertFalse(linkedListDeque.isEmpty());
            assertEquals(i + 1, linkedListDeque.size());
            assertEquals(i, (int) linkedListDeque.get(i));
            assertEquals(i, (int) linkedListDeque.getRecursive(i));
        }
        assertEquals("0 1 2", linkedListDeque.toString());
        linkedListDeque.printDeque();
        for (int i = 2; i >= 0; i--) {
            assertEquals(i, (int) linkedListDeque.removeLast());
            assertEquals(i, linkedListDeque.size());
        }
        assertNull(linkedListDeque.removeLast());
        assertNull(linkedListDeque.get(0));
        assertNull(linkedListDeque.getRecursive(0));
        assertEquals("null", linkedListDeque.toString());
        linkedListDeque.printDeque();
    }

    @Test
    public void testMixedAddAndRemove(){
        LinkedListDeque<Integer> linkedListDeque = new LinkedListDeque<>();
        assertTrue(linkedListDeque.isEmpty());
        linkedListDeque.addLast(1);
        linkedListDeque.addLast(2);
        assertEquals(1, (int) linkedListDeque.removeFirst());
        linkedListDeque.addFirst(0);
        assertEquals(2, (int) linkedListDeque.removeLast());
        linkedListDeque.addLast(3);
        assertEquals(0, (int) linkedListDeque.get(0));
        assertEquals(3, (int) linkedListDeque.get(1));
        assertNull(linkedListDeque.get(2));
        assertEquals("0 3", linkedListDeque.toString());
        int i = 0;
        System.out.println(linkedListDeque);
        for (int n : linkedListDeque) {
            assertEquals((int) linkedListDeque.get(i), n);
            i++;
        }
        assertEquals(linkedListDeque.size(), i);
        linkedListDeque.printDeque();
    }

    @Test
    public void testTimeConsumption() {
        int end = 1280000;
        int times = 1000;
        int t = 0;
        long tm = System.currentTimeMillis();
        while (t < times) {
            LinkedListDeque<Integer> linkedListDeque = new LinkedListDeque<>();
            for (int i = 0; i < end; i++) {
                linkedListDeque.addLast(i);
            }
            t++;
        }

        System.out.println("addLast for LinkedListDeque: " + (System.currentTimeMillis() - tm));

        t = 0;
        tm = System.currentTimeMillis();
        while (t < times) {
            LinkedListDeque<Integer> linkedListDeque = new LinkedListDeque<>();
            for (int i = 0; i < end; i++) {
                // linkedListDeque.get(i); // too slow!!!
            }
            t++;
        }

        System.out.println("get for LinkedListDeque: " + (System.currentTimeMillis() - tm));

        t = 0;
        tm = System.currentTimeMillis();

        while (t < times) {
            LinkedListDeque<Integer> linkedListDeque = new LinkedListDeque<>();
            for (int i = 0; i < end; i++) {
                linkedListDeque.removeLast();
            }
            t++;
        }

        System.out.println("removeLast for LinkedListDeque: " + (System.currentTimeMillis() - tm));

        t = 0;
        tm = System.currentTimeMillis();

        while (t < times) {
            LinkedListDeque<Integer> linkedListDeque = new LinkedListDeque<>();
            for (int i = 0; i < end; i++) {
                linkedListDeque.addFirst(i);
            }
            t++;
        }

        System.out.println("addFirst for LinkedListDeque: " + (System.currentTimeMillis() - tm));

        t = 0;
        tm = System.currentTimeMillis();

        while (t < times) {
            LinkedListDeque<Integer> linkedListDeque = new LinkedListDeque<>();
            for (int i = 0; i < end; i++) {
                linkedListDeque.removeFirst();
            }
            t++;
        }

        System.out.println("removeFirst for LinkedListDeque: " + (System.currentTimeMillis() - tm));

    }
}
