package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesReverse() {
        IntList lst = IntList.of(18, 17, 16, 15, 14);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("18 -> 289 -> 16 -> 15 -> 14", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesZero() {
        IntList lst = IntList.of(0, 1, 2, 3, 4);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("0 -> 1 -> 4 -> 9 -> 4", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesEmpty() {
        IntList lst = IntList.of(0);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("0", lst.toString());
        assertFalse(changed);
    }

    @Test
    public void testSquarePrimesLast() {
        IntList lst = IntList.of(0, 1, 2, 3);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("0 -> 1 -> 4 -> 9", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesFirst() {
        IntList lst = IntList.of(2, 3, 4);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 9 -> 4", lst.toString());
        assertTrue(changed);
    }
}
