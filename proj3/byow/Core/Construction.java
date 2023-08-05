package byow.Core;

import java.util.TreeMap;

/**
 * This class represents a construction in a frame such as a room or a hallway.
 */
public abstract class Construction {
    /** The points of walls. */
    protected Point[] walls;
    /** The points of bricks which also means the interior space of a room. */
    protected Point[] bricks;
    /** The points of the room. */
    protected Point[] gates;
    /** The name of the construction. */
    protected final String key;
    protected Point central;
    protected static final int WALLS = 0;
    protected static final int BRICKS = 1;
    protected static final int GATES = 2;

    protected Construction() {
        this.key = Utils.getRandomUUID();
    }

    /** Return the key of a room. */
    public String getKey() {
        return this.key;
    }

    /**
     * Return a TreeMap of a construction with:
     * key "walls" : a point[] of walls;
     * key "bricks" : a point[] of bricks, which also means the interior of a construction.
     * key "gates"(if exist) : a point[4] of gates, example: gates[FRAME.NORTH] = null means
     * there is not a gate on the north of the construction.
     */
    abstract TreeMap<Integer, Point[]> getPoints();
}
