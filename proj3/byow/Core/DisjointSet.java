package byow.Core;

import java.util.Arrays;

/**
 * This class is a simple DisjointSet data structure implementation.
 */
public class DisjointSet {

    /** The volume of the DJS. */
    private static final int VOLUME = Engine.WIDTH * Point.WIDTH_FACTOR + Engine.HEIGHT;
    /** An array of all the points with parent point. */
    private final int[] points;
    /** An array of all the point with the property of accessible */
    private final boolean[] accessible;


    /** Build an array to save the DJS, and set the value to their index. */
    public DisjointSet() {
        points = new int[VOLUME];
        accessible = new boolean[VOLUME];
        for (int i = 0; i < VOLUME; i++) {
            points[i] = i;
            accessible[i] = true;
        }
    }

    /** Build a DJS from an array, and set the value to their index. */
    public DisjointSet(int[] points) {
        this.points = points;
        accessible = new boolean[points.length];
    }

    /** Return the root point of the point. */
    public int find(int iPoint) {
        if (points[iPoint] == iPoint) {
            return iPoint;
        }
        return find(points[iPoint]);
    }

    /** Return the clone of the DJS array */
    public int[] exportArray() {
        return Arrays.copyOf(this.points, this.points.length);
    }

    /** Return if the point is accessible to other points. */
    public boolean isAccessible(int iPoint) {
        return accessible[iPoint];
    }

    /** Return if the point1 is connected with point2 by checking if they have same root point. */
    public boolean isConnected(int iPoint1, int iPoint2) {
        return find(iPoint1) == find(iPoint2);
    }

    /** Connect the 2 points. */
    public void connect(int iPoint1, int iPoint2) {
        points[find(iPoint2)] = points[find(iPoint1)];
    }

    /** Set inaccessible to a point */
    public void setInaccessiblePoint(int iPoint) {
        accessible[iPoint] = false;
    }

    /** Set all points to accessible. */
    public void resetAccessible() {
        for (int i = 0; i < this.points.length; i++) {
            accessible[i] = true;
        }
    }
}
