package byow.Core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * This class represents a hallway that implement the A* algorithm for finding the way.
 */
public class Hallway extends Construction implements Serializable {
    /** A list of points of the hallway. */
    private final Point start;
    private final Point end;

    /** Create a hallway by a start point and a end point. */
    public Hallway(Point start, Point end, DisjointSet djs) {
        this.start = start;
        this.end = end;
        build(djs, start, end);
    }

    /** Create a hallway by a vertex. */
    public Hallway(VertexOfRooms vertex, DisjointSet djs) {
        this(vertex.getRoom1().getCentralPoint(), vertex.getRoom2().getCentralPoint(), djs);
    }

    @Override
    TreeMap<Integer, Point[]> getPoints() {
        TreeMap<Integer, Point[]> hallwayPoints = new TreeMap<>();
        hallwayPoints.put(Construction.WALLS, walls);
        hallwayPoints.put(Construction.BRICKS, bricks);
        hallwayPoints.put(Construction.GATES, gates);
        return hallwayPoints;
    }

    /** Extend the hallway to the end point by A+. */
    public boolean build(DisjointSet djs, Point s, Point e) {
        ArrayList<Point> wallList = new ArrayList<>();
        ArrayList<Point> bricksList = new ArrayList<>();
        wallList.add(s);
        bricksList.add(e);
        this.walls = wallList.toArray(new Point[0]);
        this.bricks = bricksList.toArray(new Point[0]);
        this.gates = new Point[]{s, e};
        return true;
    }
}
