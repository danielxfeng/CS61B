package byow.Core;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represent a x,y coordinate system.
 */
public class Point implements Serializable {
    private final int x;
    private final int y;
    /**
     * Follows are the 4 directions.
     */
    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int WEST = 2;
    public static final int EAST = 3;
    public static final int DIRECTION_INIT = 4;
    public static final int WIDTH_FACTOR = (int) Math.pow(10, String.valueOf(Engine.WIDTH).length());

    /**
     * Create an instance by given X and Y.
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * return the X
     */
    public int getX() {
        return this.x;
    }

    /**
     * return the Y
     */
    public int getY() {
        return this.y;
    }

    /**
     * return the neighbours of a point.
     */
    public static ArrayList<Integer> getNeighboursFromIndex(Point point) {
        ArrayList<Integer> neighbourList = new ArrayList<>();
        if (!point.checkBound()) {
            throw new IndexOutOfBoundsException("The given point"
                    + "(" + point.getX() + "," + point.getY() + ")"
                    + " is out of bound.");
        }

        Point[] neighbours = getNeighbours(point);

        for (Point neighbour : neighbours) {
            if (neighbour.checkBound()) {
                neighbourList.add(neighbour.parseIndex());
            }
        }
        return neighbourList;
    }

    /**
     * Return the next point of the given point and the given direction.
     */
    public static Point getNextPoint(Point point, int direction) {
        return switch (direction) {
            case NORTH -> point.getShiftPoint(0, 1);
            case SOUTH -> point.getShiftPoint(0, -1);
            case WEST -> point.getShiftPoint(-1, 0);
            case EAST -> point.getShiftPoint(1, 0);
            default -> throw new IndexOutOfBoundsException("No such direction.");
        };
    }

    /**
     * Return the neighbours of the given point by the sequence NSWE.
     */
    public static Point[] getNeighbours(Point point) {
        return new Point[]{getNextPoint(point, NORTH), getNextPoint(point, SOUTH),
                getNextPoint(point, WEST), getNextPoint(point, EAST)};
    }

    /**
     * Return the neighbours of the given int point.
     */
    public static Point[] getNeighbours(int iPoint) {
        return getNeighbours(Point.parseFromIndex(iPoint));
    }

    /**
     * return the direction of the given point.
     */
    public static int getDirection(int iPoint, int prevIPoint) {
        Point point = parseFromIndex(iPoint);
        Point prevPoint = parseFromIndex(prevIPoint);
        if (point.getX() == prevPoint.getX()) {
            if (point.getY() > prevPoint.getY()) {
                return NORTH;
            } else {
                return SOUTH;
            }
        } else {
            if (point.getX() > prevPoint.getX()) {
                return EAST;
            } else {
                return WEST;
            }
        }
    }

    /**
     * return the point by the index value.
     */
    public static Point parseFromIndex(int index) {
        return new Point(index / WIDTH_FACTOR, index % WIDTH_FACTOR);
    }

    /**
     * return the point by shift value.
     */
    public Point getShiftPoint(int dx, int dy) {
        return new Point(this.x + dx, this.y + dy);
    }

    /**
     * return the index value (xy) of the point.
     */
    public int parseIndex() {
        return this.getX() * WIDTH_FACTOR + this.getY();
    }

    /**
     * return the index value (xy) of the x and y.
     */
    public static int parseIndex(int x, int y) {
        return x * WIDTH_FACTOR + y;
    }

    /**
     * Check if the point is in the frame.
     */
    public boolean checkBound() {
        return getX() >= 0 && getX() < Engine.WIDTH && getY() >= 0 && getY() <= Engine.HEIGHT;
    }

    @Override
    public String toString() {
        return String.valueOf(parseIndex());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != Point.class) {
            return false;
        }
        Point p = (Point) o;
        return this.getX() == p.getX() && this.getY() == p.getY();
    }

    @Override
    public int hashCode() {
        return parseIndex();
    }
}
