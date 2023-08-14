package byow.Core;

/**
 * This class is a simple DisjointSet data structure implementation.
 */
public class DisjointSet {

    /**
     * An array of all the points with parent point.
     */
    private final int[] points;

    /**
     * Build an array to save the DJS, and set the value to their index.
     */
    public DisjointSet() {
        points = new int[Frame.VOLUME];
        for (int i = 0; i < Frame.VOLUME; i++) {
            points[i] = i;
        }
    }

    /**
     * Return the root point of the point.
     */
    public int find(int iPoint) {
        if (points[iPoint] == iPoint) {
            return iPoint;
        }
        return find(points[iPoint]);
    }

    /**
     * Return if the point1 is connected with point2 by checking if they have same root point.
     */
    public boolean isConnected(int iPoint1, int iPoint2) {
        return find(iPoint1) == find(iPoint2);
    }

    /**
     * Connect the 2 points.
     */
    public void connect(int iPoint1, int iPoint2) {
        points[find(iPoint2)] = points[find(iPoint1)];
    }
}
