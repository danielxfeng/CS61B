package flik;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * To test the Flik class.
 * @author Daniel Feng
 */

public class FlikTest {

    @Test
    public void testSmall() {
        assertTrue("Same 0", Flik.isSameNumber(0, 0));
        assertFalse("Same 1", Flik.isSameNumber(1, 0));
        assertFalse("Not same 0 and 1", Flik.isSameNumber(0, 1));
        assertTrue("Same null", Flik.isSameNumber(null, null));
        assertFalse("Not same null and 1", Flik.isSameNumber(null, 1));
        assertFalse("Not same 0 and null", Flik.isSameNumber(0, null));
        assertTrue("Same -1073", Flik.isSameNumber(-1073, -1073));
        int i = 0;
        while (i < 1000) {
            int r = StdRandom.uniform(-2000,1000);
            assertTrue("Random test No " + i + " , test value "  + r + ".", Flik.isSameNumber(r, r));
            i++;
        }
    }


}
