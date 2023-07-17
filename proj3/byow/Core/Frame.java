package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/** This class respects the frame. */
public class Frame implements Serializable{

    /** The frame is filled by Tiles. */
    private final TETile[][] tiles;
    /** The rooms in the frame. */
    private final Rooms rooms;
    /** The hallways in the frame. */
    private final List<Hallway> hallways;
    /** The file for serialize and save the instance to disk. */
    private static final File OBJ_FILE = Utils.join(new File(System.getProperty("user.dir")), "my_world.obj");
    /** The random. */
    private final Random rand;

    /** Create a frame and filled with NOTHING tiles. */
    public Frame(int width, int height, long seed) {
        tiles = new TETile[width][height];
        fillWithNothing();
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

    /** Draw the frame. */
    public void draw() {
        for (String key : this.rooms.getRooms()) {
            this.rooms.getRoom(key).draw(tiles);
        }
        for (Hallway hallway : hallways) {
            hallway.draw(tiles);
        }
    }

    /** Generate new rooms. */
    public void generateRooms() {
        int nRooms = Rooms.getRoomNumbers(this.rand);
        for (int i = 0; i < nRooms; i++) {
            while(true) {
                Room newRoom = new Room(this.rand);
                if (this.rooms.addRoom(newRoom)) {
                    break;
                }
            }
        }
    }

    /** Generate new hallways by MST. */
    public void generateHallways() {
        //TODO
    }

    /** Save the instance variables to disk. */
    public void saveToFile() {
        Utils.writeObject(OBJ_FILE, this);
    }

    /** Read the saved instance variables. */
    public static Frame readFromFile() {
        return Utils.readObject(OBJ_FILE, Frame.class);
    }

    /** Return a random UUID. */
    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }
}
