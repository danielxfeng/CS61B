package byow.Core.Create;

import byow.Core.Point;
import byow.Core.TileBrick;
import byow.TileEngine.Tileset;

import java.util.List;

/**
 * This class represents a hallway that implement the A* algorithm for finding the way.
 */
public class Hallway extends Construction {

    /**
     * The room that the hallway starts from.
     */
    private final Room startRoom;
    /**
     * The room that the hallway ends at.
     */
    private final Room targetRoom;

    /**
     * Create a hallway by a vertex.
     */
    public Hallway(Room[] vertex, TileBrick[] properties) {
        super(properties);
        Hallway.WALL_TILE = Tileset.TREE;
        Hallway.BRICK_TILE = Tileset.GRASS;
        Room room1 = vertex[0];
        Room room2 = vertex[1];
        if (room1.getSwIndex() + room1.getNe().parseIndex() < room2.getSwIndex() + room2.getNe().parseIndex()) {
            this.startRoom = room1;
            this.targetRoom = room2;
        } else {
            this.startRoom = room2;
            this.targetRoom = room1;
        }
        buildHallway();
    }

    /**
     * Build the hallway by the A* algorithm.
     */
    private void buildHallway() {
        AStar aStar = new AStar(startRoom, targetRoom, tileBricks);
        List<Point> path = aStar.runAPlus();
        addPathToHallway(path);
        insertToFrameFields();
    }

    /**
     * Add the path to the hallway.
     */
    private void addPathToHallway(List<Point> path) {
        int lastDirection = Point.DIRECTION_INIT;
        for (int i = 0; i < path.size(); i++) {
            Point point = path.get(i);
            if (i == 0) {
                boolean isSetGate = startRoom.setGate(point);
                if (!isSetGate) {
                    throw new RuntimeException("Cannot set gate for the start room.");
                }
            } else if (i == path.size() - 1) {
                boolean isSetGate = targetRoom.setGate(point);
                if (!isSetGate) {
                    bricks.add(point);
                }
            } else {
                bricks.add(point);
                Point prev = path.get(i - 1);
                int direction = Point.getDirection(point.parseIndex(), prev.parseIndex());
                addHallwayWalls(path, point, prev, direction, lastDirection);
                if (direction != lastDirection) {
                    lastDirection = direction;
                }
            }
        }
    }

    /**
     * Add the walls of the hallway by a given point and its prev.
     */
    private void addHallwayWalls(List<Point> path, Point point, Point prev, int direction, int lastDirection) {
        Point[] neighbors = Point.getNeighbours(point);
        for (Point neighbor : neighbors) {
            addHallwayWalls(path, neighbor);
        }
        if (direction != lastDirection && lastDirection != Point.DIRECTION_INIT) {
            Point turn;
            if ((lastDirection == Point.NORTH && direction == Point.EAST)
                    || (lastDirection == Point.EAST && direction == Point.SOUTH)) {
                turn = prev.getShiftPoint(-1, 1); // turn right
            } else if (lastDirection == Point.SOUTH && direction == Point.EAST
                    || (lastDirection == Point.WEST && direction == Point.NORTH)) {
                turn = prev.getShiftPoint(-1, -1); // turn left
            } else if (lastDirection == Point.NORTH && direction == Point.WEST
                    || (lastDirection == Point.WEST && direction == Point.SOUTH)) {
                turn = prev.getShiftPoint(1, 1); // turn left
            } else {
                turn = prev.getShiftPoint(1, -1); // turn right
            }
            addHallwayWalls(path, turn);
        }
    }

    /**
     * Add the walls of the hallway by a given point after checking.
     */
    private void addHallwayWalls(List<Point> path, Point point) {
        if (!path.contains(point)
                && tileBricks[point.parseIndex()].getConstructionType() == TileBrick.CONSTRUCTION_TYPE_NOTHING) {
            walls.add(point);
        }
    }
}
