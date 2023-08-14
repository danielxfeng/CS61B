package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;

/**
 * This class respects a TileBrick of a frame.
 */
public class TileBrick implements Serializable {
    private final Point point;
    private final int iPoint;
    private int type;
    private int constructionType;
    private String constructionKey;
    private TETile style;
    public static final int CONSTRUCTION_TYPE_ROOM = 0;
    public static final int CONSTRUCTION_TYPE_HALLWAY = 1;
    public static final int CONSTRUCTION_TYPE_NOTHING = 2;

    public TileBrick(int iPoint) {
        this.iPoint = iPoint;
        this.point = Point.parseFromIndex(iPoint);
        this.type = Construction.NOTHING;
        this.constructionType = CONSTRUCTION_TYPE_NOTHING;
        this.constructionKey = "None";
        this.style = setStyle();
    }

    /**
     * Set the value of a tile brick.
     */
    public void setValue(int inType, int inConstructionType, String inConstructionKey) {
        this.type = inType;
        this.constructionType = inConstructionType;
        this.constructionKey = inConstructionKey;
        this.style = setStyle();
    }

    /**
     * Return the constructionType of a tile brick.
     */
    public int getConstructionType() {
        return this.constructionType;
    }

    /**
     * Return the constructionKey of a tile brick.
     */
    public String getKey() {
        return this.constructionKey;
    }

    /**
     * Return the type of a tile brick.
     */
    public int getType() {
        return this.type;
    }

    /**
     * Return the point of a tile brick.
     */
    public TETile getStyle() {
        return this.style;
    }

    /**
     * Return the style of a tile brick.
     */
    public TETile setStyle() {
        return switch (this.getType()) {
            case Construction.WALLS -> Room.WALL_TILE;
            case Construction.BRICKS -> Room.BRICK_TILE;
            case Construction.GATES -> Room.GATE_TILE;
            default -> Tileset.NOTHING;
        };
    }
}
