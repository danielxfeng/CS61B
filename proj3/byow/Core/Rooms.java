package byow.Core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

/** This class represents the rooms in the frame. */
public class Rooms implements Serializable {

    /** Stores a TreeMap of rooms, key is the UUID of a room, value is a room. */
    private final TreeMap<String, Room> rooms;
    /** The max rooms density. */
    protected static final double MIN_ROOM_DENSITY = 0.005;
    /** The max rooms density. */
    protected static final double MAX_ROOM_DENSITY = 0.02;

    public Rooms() {
        this.rooms = new TreeMap<>();
    }

    /** Return how many rooms in this frame by a give random generator. */
    public static int getRoomCounts(Random rand) {
        int roomsLimit = (int) Math.round(Engine.HEIGHT * Engine.WIDTH * MAX_ROOM_DENSITY);
        int minRooms = (int) Math.round(Engine.HEIGHT * Engine.WIDTH * MIN_ROOM_DENSITY);
        return rand.nextInt(roomsLimit - minRooms) + minRooms;
    }

    /** Get the list of rooms. May return null. */
    public String[] getRoomsKeySet() {
        if (this.rooms.isEmpty()) {
            return null;
        }
        return this.rooms.keySet().toArray(new String[0]);
    }

    /** Get the list of rooms. May return null. */
    public Room[] getRooms() {
        if (this.rooms.isEmpty()) {
            return null;
        }
        return this.rooms.values().toArray(new Room[0]);
    }

    /** Get the room from tree map. May return null. */
    public Room getRoom(String key) {
        return this.rooms.get(key);
    }

    /** Check if the given room will be conflict with exist rooms. */
    private boolean isConflictByRoom(Room room, DisjointSet djs) {
        var roomMap = room.getPoints();
        return isConflictByPoints(roomMap.get(Construction.WALLS), djs)
                || isConflictByPoints(roomMap.get(Construction.BRICKS), djs);
    }

    /** Check if the given points will be conflict with exist rooms. */
    private boolean isConflictByPoints(Point[] points, DisjointSet djs) {
        for (Point p : points) {
            if (!djs.isAccessible(p.parseIndex())) {
                return true;
            }
        }

        return false;
    }

    /** Add a room to the rooms list after checking. */
    public boolean addRoom(Room room, DisjointSet djs) {
        if (isConflictByRoom(room, djs)) {
            return false;
        }
        this.rooms.put(room.getKey(), room);
        insertToDjs(room, djs);
        return true;
    }

    /** Try to add a brick to the DJS.
     *  Return true when:
     *      1. Two points are the same.
     *      2. The point2 or one of its neighbours is connected with point1.
     *      3. Set all points in the room inaccessible.
     */
    public boolean addRoomBricks(DisjointSet djs, int iPoint1, int iPoint2) {
        if (djs.isConnected(iPoint1, iPoint2)) {
            djs.setInaccessiblePoint(iPoint2);
            return true;
        }

        ArrayList<Integer> pointsList = Point.getNeighboursFromIndex(iPoint2);

        for (int p: pointsList) {
            if (djs.isConnected(p, iPoint1)) {
                djs.connect(iPoint1, iPoint2);
                djs.setInaccessiblePoint(iPoint2);
                return true;
            }
        }
        return false;
    }

    /** Insert the points of room to the djs. */
    private void insertToDjs(Room room, DisjointSet djs) {
        TreeMap<Integer, Point[]> map = room.getPoints();
        int sw = room.getSw();
        insertToDjs(map.get(Construction.WALLS), djs, sw);
        insertToDjs(map.get(Construction.BRICKS), djs, sw);
    }

    /** Insert the points to the djs. */
    private void insertToDjs(Point[] points, DisjointSet djs, int sw) {
        for (Point p : points) {
            boolean isSuccess = addRoomBricks(djs, sw, p.parseIndex());
            if (!isSuccess) {
                throw new RuntimeException("Error to add a point of a room to the DisjointSet.");
            }
        }
    }
}
