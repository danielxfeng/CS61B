package flik;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * To test the HorribleSteve class.
 * @author Daniel Feng
 */

public class HorribleSteveTest {

    @Test
    public void testSmall() {
        try {
            HorribleSteve.main(null);
        } catch (Exception e) {
            throw new RuntimeException("error in HorribleSteve class");
        }

    }

}
