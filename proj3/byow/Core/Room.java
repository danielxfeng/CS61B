package byow.Core;

import java.io.Serializable;
import java.util.*;

/**
 * This class represents a room.
 * The room is a square.
 */
public class Room extends Construction implements Serializable {

    /** The point of southwest corner. */
    private final Point sw;
    /** The width of the room. */
    private final int width;
    /** The height of the room. */
    private final int height;
    /** The max side length of the room. */
    protected static final int MAX_SIDE_LENGTH = 10;
    /** The min side length of the room. */
    protected static final int MIN_SIDE_LENGTH = 4;

    /** Create a room by a given random generator and lBound which is the bound of width and height. */
    public Room(Random rand) {
        this.width = Room.getRandomSide(rand);
        this.height = Room.getRandomSide(rand);
        this.sw = new Point(rand.nextInt(Engine.WIDTH - width),
                rand.nextInt(Engine.HEIGHT - height));
        this.central = new Point(this.sw.getX() + (this.width / 2),
                this.sw.getY() + (this.height / 2));
        this.gates = new Point[4];
        generateNewRoom();
    }

    /** Return a side length by a given random generator. */
    private static int getRandomSide(Random rand) {
        return rand.nextInt(MAX_SIDE_LENGTH - MIN_SIDE_LENGTH) + MIN_SIDE_LENGTH;
    }

    /** Return the central point of the room. */
    public Point getCentralPoint() {
        return this.central;
    }

    /** Return the width of the room. */
    public int getWidth() {
        return width;
    }

    /** Return the width of the room. */
    public int getHeight() {
        return height;
    }

    /** Return the int index of the southwest corner of the room. */
    public int getSw() {
        return this.sw.parseIndex();
    }

    /** Return a deque of points which are possible for gate. */
    public Deque<Point> getGate(Point targetPoint) {
        ArrayDeque<Point> deque= new ArrayDeque<>();
        //TODO
        return deque;
    }

    /**
     * Return a TreeMap of a construction with:
     * key "walls" : a point[] of walls;
     * key "bricks" : a point[] of bricks, which also means the interior of a construction.
     * key "gates" : a point[4] of gates, example: gates[FRAME.NORTH] = null means
     * there is not a gate on the north of the construction.
     */
    @Override
    public TreeMap<Integer, Point[]> getPoints() {
        TreeMap<Integer, Point[]> roomPoints = new TreeMap<>();
        roomPoints.put(Construction.WALLS, walls);
        roomPoints.put(Construction.BRICKS, bricks);
        roomPoints.put(Construction.GATES, gates);
        return roomPoints;
    }

    /** Create a room by the fields of the class */
    private void generateNewRoom() {
        ArrayList<Point> walls = new ArrayList<>();
        ArrayList<Point> bricks = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i == 0 || i == width - 1 || j == 0 || j == height - 1) {
                    walls.add(sw.getShiftPoint(i, j));
                } else {
                    bricks.add(sw.getShiftPoint(i, j));
                }
            }
        }
        this.walls = walls.toArray(new Point[0]);
        this.bricks = bricks.toArray(new Point[0]);
    }

    /** Set the point of gate by the given point. */
    public void setGate(Point p) {
        //TODO this.gates[direction] = p;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != Room.class) {
            return false;
        }
        return this.getKey().equals(((Room) o).getKey());
    }
}
