package byow.Core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
     * Return the Vision points of the given int point and the vision scope that defined in Game.class.
     */
    private static List<Integer> getVision(int x, int y, int visionScope) {
        List<Integer> pointList = new ArrayList<>();
        Point point = new Point(x, y);
        Point sw = point.getShiftPoint(-visionScope, -visionScope);
        int startX = sw.getX();
        int startY = sw.getY();
        for (int i = startX; i < startX + visionScope * 2 + 1; i++) {
            for (int j = startY; j < startY + visionScope * 2 + 1; j++) {
                if (Point.checkBound(i, j)) {
                    pointList.add(Point.getIPointFromXY(i, j));
                }
            }
        }
        return pointList;
    }

    public static List<Integer> getVision(int iPoint, int visionScope) {
        Point point = parseFromIndex(iPoint);
        return getVision(point.getX(), point.getY(), visionScope);
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
     * return the point by shift value.
     */
    public Point getShiftPoint(int dx, int dy) {
        return new Point(this.x + dx, this.y + dy);
    }

    /**
     * Return the iPoint from given x and y.
     */
    public static int getIPointFromXY(int x, int y) {
        return x * WIDTH_FACTOR + y;
    }

    /**
     * return the point by the index value.
     */
    public static Point parseFromIndex(int index) {
        return new Point(index / WIDTH_FACTOR, index % WIDTH_FACTOR);
    }

    /**
     * return the index value (xy) of the point.
     */
    public int parseIndex() {
        return this.getX() * WIDTH_FACTOR + this.getY();
    }

    /**
     * Check if the point is in the frame.
     */
    public static boolean checkBound(int x, int y) {
        return x >= 0 && x < Engine.WIDTH && y >= 0 && y <= Engine.HEIGHT;
    }

    /**
     * Check if the point is in the frame.
     */
    public static boolean checkBound(int iPoint) {
        Point p = Point.parseFromIndex(iPoint);
        return checkBound(p.getX(), p.getY());
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
