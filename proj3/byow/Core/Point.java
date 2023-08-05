package byow.Core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class represent a x,y coordinate system.
 */
public class Point implements Serializable {
    private final int x;
    private final int y;
    public static final int WIDTH_FACTOR = (int) Math.pow(10, String.valueOf(Engine.WIDTH).length());
    private static final int[][] NEIGHBOUR_SHIFTS = {{-1, 0}, {0, -1}, {1, 0}, {0 ,1}};

    /** Create an instance by given X and Y. */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Create an instance by random x and y.
     *
     * @param rand the random.
     * @param xBound the bound of x.
     * @param yBound the bound of y.
     */
    public Point(Random rand, int xBound, int yBound) {
        this.x = rand.nextInt(xBound);
        this.y = rand.nextInt(yBound);
    }

    /** return the X */
    public int getX() {
        return this.x;
    }

    /** return the Y */
    public int getY() {
        return this.y;
    }

    /** return the point by shift value. */
    public Point getShiftPoint(int dx, int dy) {
        return new Point(this.x + dx, this.y + dy);
    }

    /** return the index value (xy) of the point. */
    public int parseIndex() {
        return this.getX() * WIDTH_FACTOR + this.getY();
    }

    /** return the point by the index value. */
    public static Point parseFromIndex(int index) {
        return new Point(index / WIDTH_FACTOR, index % WIDTH_FACTOR);
    }

    /** return the neighbours of a point. */
    public static ArrayList<Integer> getNeighboursFromIndex(Point point) {
        ArrayList<Integer> neighbours = new ArrayList<>();
        if (!point.checkBound()) {
            throw new IndexOutOfBoundsException("The given point"
                    + "(" + point.getX() + "," + point.getY() + ")" +
                    " is out of bound.");
        }
        for (int[] neighbourShift : NEIGHBOUR_SHIFTS) {
            Point neighbour = point.getShiftPoint(neighbourShift[0], neighbourShift[1]);
            if (neighbour.checkBound()) {
                neighbours.add(neighbour.parseIndex());
            }
        }
        return neighbours;
    }

    /** return the neighbours of a point by its int index. */
    public static ArrayList<Integer> getNeighboursFromIndex(int iPoint) {
        return getNeighboursFromIndex(parseFromIndex(iPoint));
    }

    /** Check if the point is in the frame. */
    public boolean checkBound() {
        if (getX() < 0 || getX() >= Engine.WIDTH || getY() < 0 || getY() > Engine.HEIGHT) {
            return false;
        }
        return true;
    }
}
