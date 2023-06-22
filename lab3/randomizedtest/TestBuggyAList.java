package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {

    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> aListNoResizing = new AListNoResizing<>();
        BuggyAList<Integer> buggyAList = new BuggyAList<>();

        int[] testArr = {4, 5, 6};
        for (int v : testArr) {
            aListNoResizing.addLast(v);
            buggyAList.addLast(v);
        }
        assertEquals(aListNoResizing.size(), buggyAList.size());
        for (int i = 0; i < testArr.length; i++) {
            assertEquals(aListNoResizing.removeLast(), buggyAList.removeLast());
        }
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> buggyAList = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                buggyAList.addLast(randVal);
                assertEquals(L.getLast(), buggyAList.getLast());
            } else if (operationNumber == 1) {
                int size = L.size();
                int sizeBuggy = buggyAList.size();
                assertEquals(size, sizeBuggy);
                if (L.size() > 0) {
                    assertEquals(L.getLast(), buggyAList.getLast());
                }
            } else {
                if (L.size() > 0) {
                    assertEquals(L.removeLast(), buggyAList.removeLast());
                }
            }
        }
    }
}
