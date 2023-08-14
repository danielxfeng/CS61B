package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.io.File;
import java.io.Serializable;

public class Engine implements Serializable {

    /**
     * The file for serialize and save the instance to disk.
     */
    private static final File OBJ_FILE = Utils.join(new File(System.getProperty("user.dir")), "my_world.obj");
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    /**
     * The frame is filled by Tiles.
     */
    private final TETile[][] tiles;
    /** The seed used to generate the world. */
    private long seed;
    /**
     * Save the tileBrick of every tile.
     */
    private final TileBrick[] tileBricks;

    public Engine() {
        this.tiles = new TETile[WIDTH][HEIGHT];
        this.tileBricks = new TileBrick[Frame.VOLUME];
        for (int i = 0; i < Frame.VOLUME; i++) {
            this.tileBricks[i] = new TileBrick(i);
        }
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        input = input.toLowerCase();
        char command = input.charAt(0);
        return switch (command) {
            case 'n' -> newGame(input);
            default -> throw new IllegalArgumentException("Invalid input string.");
        };
    }

    /**
     * Start a new game with render a frame.
     */
    private TETile[][] newGame(String input) {
        for (int i = 1; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == 's') {
                try {
                    this.seed = Long.parseLong(input.substring(1, i));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid seed.");
                }
                Frame frame = new Frame(seed, tileBricks);
                frame.create();
                return this.tiles;
            }
        }
        throw new IllegalArgumentException("Invalid input string.");
    }

    /**
     * Read the saved instance variables.
     */
    public static Engine readFromFile() {
        return Utils.readObject(OBJ_FILE, Engine.class);
    }

    /**
     * Fill the tiles.
     */
    private void fillTiles(TETile[][] tiles) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = tileBricks[Point.parseIndex(x, y)].getStyle();
            }
        }
    }

    /**
     * Fill the tiles.
     */
    private void fillTiles() {
        fillTiles(this.tiles);
    }

    /**
     * Render the tiles.
     */
    private void render() {
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(tiles);
    }

    /**
     * Save the instance variables to disk.
     */
    public void saveToFile() {
        Utils.writeObject(OBJ_FILE, this);
    }

    /* For test only */
    public static void main(String[] args) {
        Engine engine = new Engine();
        TETile[][] tiles = engine.interactWithInputString("n5197880843569031643s");
        engine.fillTiles(tiles);
        engine.render();
    }
}
