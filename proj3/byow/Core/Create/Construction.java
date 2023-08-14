package byow.Core.Create;

import byow.Core.Point;
import byow.Core.TileBrick;
import byow.Core.Utils;
import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * This class represents a construction in a frame such as a room or a hallway.
 */
public abstract class Construction {
    public static final int WALLS = 0;
    public static final int BRICKS = 1;
    public static final int GATES = 2;
    public static final int NOTHING = 3;
    public static TETile WALL_TILE;
    public static TETile BRICK_TILE;
    public static TETile GATE_TILE;
    /**
     * The name of the construction.
     */
    protected final String key;
    /**
     * The points of walls.
     */
    protected List<Point> walls;
    /**
     * The points of bricks which also means the interior space of a room.
     */
    protected List<Point> bricks;
    /**
     * The points of gates.
     */
    protected List<Point> gates;
    /**
     * The properties of the frame.
     */
    protected transient TileBrick[] tileBricks;
    protected Point central;

    protected Construction(TileBrick[] tileBricks) {
        this.tileBricks = tileBricks;
        this.key = Utils.getRandomUUID();
        this.bricks = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.gates = new ArrayList<>();
    }

    /**
     * Return the key of a room.
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Return a TreeMap of a construction with:
     * key "walls" : a point[] of walls;
     * key "bricks" : a point[] of bricks, which also means the interior of a construction.
     * key "gates" : a point[] of gates
     */
    public TreeMap<Integer, List<Point>> getPoints() {
        TreeMap<Integer, List<Point>> points = new TreeMap<>();
        points.put(Construction.WALLS, walls);
        points.put(Construction.BRICKS, bricks);
        points.put(Construction.GATES, gates);
        return points;
    }

    /**
     * Return a List of wall points.
     */
    public List<Point> getWalls() {
        return this.walls;
    }

    /**
     * Return a List of brick points.
     */
    public List<Point> getBricks() {
        return this.bricks;
    }

    /**
     * Insert the construction to the tileBricks.
     */
    protected void insertToFrameFields () {
        int constructionType = this.getClass() == Room.class ?
                TileBrick.CONSTRUCTION_TYPE_ROOM : TileBrick.CONSTRUCTION_TYPE_HALLWAY;
        for (Point brick : bricks) {
            tileBricks[brick.parseIndex()].setValue(Construction.BRICKS, constructionType, getKey());
        }
        for (Point wall : walls) {
            tileBricks[wall.parseIndex()].setValue(Construction.WALLS, constructionType, getKey());
        }
    }
}
