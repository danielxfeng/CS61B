package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents a room.
 * The room is a square.
 */
public class Room implements Serializable {

    /** The point of northwest corner. */
    private Point nw;
    /** The point of northeast corner. */
    private Point ne;
    /** The point of southwest corner. */
    private Point sw;
    /** The point of southeast corner. */
    private Point se;
    /** The width of the room. */
    private final List<Point> gates;
    /** The name of a room. */
    private String key;
    private int width;
    /** The height of the room. */
    private int height;
    /** The max side length of the room. */
    protected static final int MAX_SIDE_LENGTH = 7;
    /** The min side length of the room. */
    protected static final int MIN_SIDE_LENGTH = 2;
    /** The min numbers of rooms. */
    protected static final int MIN_ROOMS = 4;
    /** The max rooms density. */
    protected static final double MAX_ROOM_DENSITY = 0.3;

    /** Create a room by given the random generator, northwest point, width and height. */
    public Room(Random rand, Point nw, int width, int height) {
        this.nw = nw;
        this.width = width;
        this.height = height;
        this.ne = nw.getShiftPoint(width, 0);
        this.sw = nw.getShiftPoint(0, -height);
        this.se = nw.getShiftPoint(width, -height);
        this.key = Frame.getRandomUUID();
        this.gates = new ArrayList<>();
    }

    /** Create a room by a given random generator and lBound which is the bound of width and height. */
    public Room(Random rand) {
        this(rand, new Point(rand.nextInt(), rand.nextInt()), Room.getRandomSide(rand), Room.getRandomSide(rand));
    }

    /** Return the point of a room. */
    public Point getPoint() {
        //TODO
        return null;
    }

    /** Return the key of a room. */
    public String getKey() {
        return this.key;
    }

    /** Draw a border in a tiles. */
    public void draw(TETile[][] tiles){
        drawBorder(tiles, nw, nw);
        drawBorder(tiles, se, ne);
        drawBorder(tiles, sw, nw);
        drawBorder(tiles, sw, se);
        if (!this.gates.isEmpty()) {
            for (Point gate : this.gates) {
                tiles[gate.getX()][gate.getY()] = Tileset.NOTHING;
            }
        }
    }

    /** Draw a border with 2 points. */
    private void drawBorder(TETile[][] tiles, Point start, Point end) {
        int sv; // start point of traveler.
        int ev; // end point of traveler.
        boolean isX; // if X is the traveller.
        int fixed; // the value of the one who do NOT move.

        if (start.getX() == end.getX()) {
            sv = start.getY();
            ev = end.getY();
            isX = false;
            fixed = start.getX();
        } else {
            sv = start.getX();
            ev = end.getY();
            isX = true;
            fixed = start.getY();
        }

        for (int i = sv; i <= ev; i++) {
            if (isX) {
                tiles[i][fixed] = Tileset.NOTHING;
            } else {
                tiles[fixed][i] = Tileset.NOTHING;
            }
        }
    }

    /** Set the point of gate by the given point. */
    public void setGate(Point p) {
        this.gates.add(p);
    }

    /** Return a side length by a given random generator. */
    private static int getRandomSide(Random rand) {
        return rand.nextInt(MAX_SIDE_LENGTH - MIN_SIDE_LENGTH) + MIN_SIDE_LENGTH;
    }

    /** Return the points of 4 corners of a room. */
    public Point[] getFourCorners() {
        return new Point[]{this.nw, this.ne, this.sw, this.se};
    }
    /** Return if the points is in this room. */
    public boolean isInRoom(Point[] points) {
        for (Point p : points) {
            if (1 == 1) { //TODO
                return false;
            }
        }
        return true;
    }
}
