package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * This class respects the frame.
 */
public class Frame implements Serializable{

    /** The frame is filled by Tiles. */
    private final TETile[][] tiles;
    /** The rooms in the frame. */
    private final Rooms rooms;
    /** The hallways in the frame. */
    private final ArrayList<Hallway> hallways;
    /** The disjoint set of the frame. */
    private final DisjointSet djs;
    /** The file for serialize and save the instance to disk. */
    private static final File OBJ_FILE = Utils.join(new File(System.getProperty("user.dir")), "my_world.obj");
    /** The random. */
    private final Random rand;
    /** Define the 4 directions. */
    private static final int NORTH = 0;
    private static final int SOUTH = 1;
    private static final int WEST = 2;
    private static final int EAST = 3;
    private static final TETile WALL_TILE = Tileset.WALL;
    private static final TETile BRICK_TILE = Tileset.FLOOR;
    private static final TETile GATE_TILE = Tileset.FLOWER;

    /** Create a empty frame. */
    public Frame(int width, int height, long seed) {
        tiles = new TETile[width][height];
        this.djs = new DisjointSet();
        this.rooms = new Rooms();
        this.hallways = new ArrayList<>();
        this.rand = new Random(seed);
    }

    /** Fills the given 2D array of tiles with NOTHING tiles. */
    public void fillWithNothing() {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    /** Fill the Constructions. */
    public void fillConstructions() {
        for (String key : this.rooms.getRoomsKeySet()) {
            Room room = this.rooms.getRoom(key);
            fillAConstruction(room);
            // TODO need to be cleared after debug
            Point central = room.getCentralPoint();
            tiles[central.getX()][central.getY()] = Tileset.MOUNTAIN;
        }
        for (Hallway hallway : this.hallways) {
            fillAConstruction(hallway);
        }
    }

    /** Fill the frame and return the tiles */
    public TETile[][] getTiles() {
        fillWithNothing();
        fillConstructions();
        return tiles;
    }

    /** Return the DJS. */
    protected DisjointSet getDjs() {
        return djs;
    }

    /** Read the saved instance variables. */
    public static Frame readFromFile() {
        return Utils.readObject(OBJ_FILE, Frame.class);
    }

    /** Fill a construction. */
    private void fillAConstruction(Construction construction) {
        TreeMap<Integer, Point[]> points = construction.getPoints();
        for (Point p : points.get(Construction.WALLS)) {
            tiles[p.getX()][p.getY()] = WALL_TILE;
        }
        for (Point p : points.get(Construction.BRICKS)) {
            tiles[p.getX()][p.getY()] = BRICK_TILE;
        }
        if (points.get(Construction.GATES) != null) {
            for (Point p : points.get(Construction.GATES)) {
                if (p != null) {
                    tiles[p.getX()][p.getY()] = GATE_TILE;
                }
            }
        }
    }

    /** Generate new rooms. */
    public void generateRooms() {
        int nRooms = Rooms.getRoomCounts(this.rand);
        generateRooms(nRooms);
    }

    /** Generate new rooms with given room numbers. */
    public void generateRooms(int nRooms) {
        for (int i = 0; i < nRooms; i++) {
            int j = 0;
            while(j < 20) {
                Room newRoom = new Room(this.rand);
                if (this.rooms.addRoom(newRoom, this.getDjs())) {
                    break;
                }
                j++;
            }
        }
    }

    /** Generate new hallways by MST. */
    public void generateHallways() {
        KruskalForMst kfm = new KruskalForMst(this.rooms);
        VertexOfRooms[] vertexes = kfm.generateVertexes(this.djs.exportArray());
        for (VertexOfRooms vertex: vertexes) {
            this.hallways.add(new Hallway(vertex, this.djs));
        }
    }

    /** Save the instance variables to disk. */
    public void saveToFile() {
        Utils.writeObject(OBJ_FILE, this);
    }
}
