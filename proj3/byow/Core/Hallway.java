package byow.Core;

import byow.TileEngine.TETile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** This class represents a hallway. */
public class Hallway implements Serializable {
    /** A list of points of the hallway. */
    private List<Point> points;

    /** Create a hallway by a start point */
    public Hallway(Point start) {
        this.points = new ArrayList<>();
        this.points.add(start);
    }

    /** Create a hallway between the start point and the end point. */
    public Hallway(Point start, Point end) {
        this.points = new ArrayList<>();
        this.points.add(start);
        extend(end);
    }

    /** Extend the hallway to the end point by A+. */
    public void extend(Point end) {
        // TODO
        return;
    }

    /** Draw the hallway to the tiles */
    public void draw(TETile[][] tiles) {
        //TODO
        return;
    }
}
