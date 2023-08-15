package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is used to represent the game.
 */
public class Game implements Serializable {

    /**
     * The renderer is used to render the frame.
     */
    transient TERenderer ter;
    /**
     * The file for serialize and save the instance to disk.
     */
    private static final File OBJ_FILE = Utils.join(new File(System.getProperty("user.dir")), "my_world.obj");
    /**
     * The frame is filled by Tiles.
     */
    private transient TETile[][] tiles;
    /**
     * Save the tileBrick of every tile.
     */
    private final TileBrick[] tileBricks;
    /**
     * The random of the game.
     */
    private final transient Random rand;
    /**
     * The style of player.
     */
    public static final TETile PLAYER_TILE = Tileset.AVATAR;
    /**
     * The code of player.
     */
    public static final int PLAYER = 100;
    /**
     * The position of the player.
     */
    private int iPointOfPlayer;
    /**
     * The scope of player version, set to 0 for infinity.
     */
    private int visionScope;

    public Game(long seed) {
        this.rand = new Random(seed);
        this.tileBricks = new TileBrick[Frame.VOLUME];
        iPointOfPlayer = -1;
        this.visionScope = Engine.VISION_SCOPE;
        for (int i = 0; i < Frame.VOLUME; i++) {
            this.tileBricks[i] = new TileBrick();
        }
        init();
    }

    /**
     * Return the vision scope.
     */
    public int getVisionScope() {
        return visionScope;
    }

    /**
     * Return the vision scope.
     */
    public void setVisionScope(int visionScope) {
        this.visionScope = visionScope;
    }

    /**
     * Start a new game with render a frame.
     */
    public void newWorld() {
        Frame frame = new Frame(rand, tileBricks);
        frame.create();
        fillAllTiles();
        render();
    }

    /**
     * Read the saved instance variables.
     */
    public static Game readFromFile() {
        Game game = Utils.readObject(OBJ_FILE, Game.class);
        game.init();
        game.fillAllTiles(true);
        game.render();
        return game;
    }

    /**
     * Initialise some fields when create and recovery.
     */
    private void init() {
        tiles = new TETile[Engine.WIDTH][Engine.HEIGHT];
        ter = new TERenderer();
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);
    }

    /**
     * Fill a tile.
     */
    private void fillATile(int x, int y, boolean isHide) {
        if (isHide) {
            tiles[x][y] = Tileset.NOTHING;
        } else {
            tiles[x][y] = tileBricks[Point.getIPointFromXY(x, y)].getStyle();
        }
    }

    /**
     * Fill all the tiles with the limit of the vision scope.
     */
    public void fillAllTiles(int iPoint, boolean isHide) {
        System.out.println("iPoint: " + iPoint);
        List<Integer> pointList = new ArrayList<>();
        if (iPoint > 0) {
            pointList = Point.getVision(iPoint, this.visionScope);
        }
        boolean tileIsHide;
        for (int x = 0; x < Engine.WIDTH; x++) {
            for (int y = 0; y < Engine.HEIGHT; y++) {
                tileIsHide = isHide && (visionScope > 0) && (!pointList.contains(Point.getIPointFromXY(x, y))) ?
                        true : false;
                fillATile(x, y, tileIsHide);
            }
        }
    }

    /**
     * Fill all the tiles without limit the vision scope.
     */
    public void fillAllTiles() {
        fillAllTiles(-1, false);
    }

    /**
     * Fill all the tiles without limit the vision scope.
     */
    public void fillAllTiles(boolean isHide) {
        fillAllTiles(this.iPointOfPlayer, isHide);
    }

    /**
     * Render the tiles.
     */
    private void render(String info) {
        ter.renderFrame(tiles, info);
    }

    /**
     * Render the tiles.
     */
    public void render() {
        String info = "";
        if (iPointOfPlayer > 0) {
            TileBrick player = tileBricks[iPointOfPlayer];
            info = "   I am at " + iPointOfPlayer + ", "
                    + "a " + player.getTypeString()
                    + " of a " + player.getConstructionTypeString() + ". "
                    + "Tip: Press 'v' to switch the vision scope, press ':q' to save and quit.";
        }
        render(info);
    }

    /**
     * Save the instance variables to disk.
     */
    public void saveToFile() {
        Utils.writeObject(OBJ_FILE, this);
    }

    /**
     * Start to interactiveGame.
     */
    public void interactiveGame() {
        int randomPosition = this.rand.nextInt(Frame.VOLUME);
        int iPoint = -1;
        while (randomPosition >= 0) {
            if (tileBricks[randomPosition].getType() == Construction.BRICKS) {
                iPoint = randomPosition;
                break;
            }
            randomPosition--;
        }
        while (randomPosition < Frame.VOLUME) {
            if (tileBricks[randomPosition].getType() == Construction.BRICKS) {
                iPoint = randomPosition;
                break;
            }
            randomPosition++;
        }
        setPlayer(iPoint);
    }

    /**
     * Set the player to a position.
     */
    private void setPlayer(int iPoint) {
        if (iPoint < 0 || iPoint >= Frame.VOLUME || !Point.checkBound(iPoint)) {
            return;
        }
        iPointOfPlayer = iPoint;
        tileBricks[iPoint] = TileBrick.setPlayer(tileBricks[iPoint]);
        fillAllTiles(iPoint, true);
        render();
    }

    /**
     * Move the player and render the frame.
     */
    public void move(char c) {
        switch (c) {
            case 'w' -> move(Point.NORTH);
            case 's' -> move(Point.SOUTH);
            case 'a' -> move(Point.WEST);
            case 'd' -> move(Point.EAST);
            default -> throw new IllegalArgumentException("Invalid direction.");
        }
    }

    /**
     * Move the player and render the frame.
     */
    private void move(int direction) {
        int nextPoint = Point.getNextPoint(Point.parseFromIndex(iPointOfPlayer), direction).parseIndex();
        int type = tileBricks[nextPoint].getType();
        if (type == Construction.GATES) { // unlock the gate
            tileBricks[nextPoint].setType(Construction.UNLOCKED_GATES);
            fillAllTiles(nextPoint, true);
            render();
        } else if (type == Construction.BRICKS || type == Construction.UNLOCKED_GATES) { // move to the next point
            tileBricks[iPointOfPlayer] = tileBricks[iPointOfPlayer].getHideOne();
            setPlayer(nextPoint);
        }
    }
}
