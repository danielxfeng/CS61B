package byow.Core;

/**
 * This class respects a vertex of 2 rooms.
 */
public class VertexOfRooms implements Comparable<VertexOfRooms> {
    /** The room 1 in the vertex. */
    private final Room room1;
    /** The room 2 in the vertex. */
    private final Room room2;
    /** The distance of the vertex. */
    private final double dist;

    /** Create a vertex by 2 rooms. */
    public VertexOfRooms(Room room1, Room room2) {
        this.room1 = room1;
        this.room2 = room2;
        Point point1 = room1.getCentralPoint();
        Point point2 = room2.getCentralPoint();
        this.dist = Utils.getDistance(point1.getX(), point1.getY(), point2.getX(), point2.getY());
    }

    /** Return room1. */
    public Room getRoom1() {
        return this.room1;
    }

    /** Return room2. */
    public Room getRoom2() {
        return this.room2;
    }

    /** Return the distance of the central points of 2 rooms. */
    public double getDist() {
        return this.dist;
    }

    /** Use the distance to compare 2 vertexes. */
    @Override
    public int compareTo(VertexOfRooms v) {
        return (int) Math.round((this.dist - v.dist) * 1000);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != VertexOfRooms.class) {
            return false;
        }
        return ((this.getRoom1().equals(((VertexOfRooms) o).getRoom1()) && this.getRoom2().equals(((VertexOfRooms) o).getRoom2())))
                || (this.getRoom2().equals(((VertexOfRooms) o).getRoom1()) && this.getRoom1().equals(((VertexOfRooms) o).getRoom2()));
    }
}
