package byow.Core;

import java.io.Serializable;
import java.util.Random;
import java.util.TreeMap;

/** This class represents the rooms in the frame. */
public class Rooms implements Serializable {

    /** Stores a TreeMap of rooms, key is the UUID of a room, value is a room. */
    private final TreeMap<String, Room> rooms;
    /** Stores a KDTree of rooms for finding the nearest room*/
    private final KDTree tree;

    public Rooms() {
        this.rooms = new TreeMap<>();
        this.tree = new KDTree();
    }

    /** Get the list of rooms. May return null. */
    public String[] getRooms() {
        if (this.rooms.isEmpty()) {
            return null;
        }
        return this.rooms.keySet().toArray(new String[0]);
    }

    /** Get the room from tree map. May return null. */
    public Room getRoom(String key) {
        return this.rooms.get(key);
    }

    /** Add a room to the rooms list after checking. */
    public boolean addRoom(Room room) {
        for (String key : rooms.keySet()) {
            if (getRoom(key).isInRoom(room.getFourCorners())) {
                return false;
            }
        }
        this.rooms.put(room.getKey(), room);
        this.tree.add(room);
        return true;
    }

    /** Return how many rooms in this frame by a give random generator. */
    public static int getRoomNumbers(Random rand) {
        int roomsLimit = (int) Math.round(Engine.HEIGHT * Engine.WIDTH * Room.MAX_ROOM_DENSITY);
        return rand.nextInt(roomsLimit - Room.MIN_ROOMS) + Room.MIN_ROOMS;
    }
}
