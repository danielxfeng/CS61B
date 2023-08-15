package byow.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * This class is a Kruskal Algorithm implementation to solve the MST problem.
 */
public class KruskalForMst {
    /**
     * A priority queue of vertexes.
     */
    private final PriorityQueue<VertexOfPoints> queue;
    /**
     * The rooms
     */
    private final List<Room> rooms;

    /**
     * This class respects a vertex of 2 points or rooms.
     */
    private static class VertexOfPoints implements Comparable<VertexOfPoints> {
        /**
         * The Manhattan distance between two points.
         */
        private final int dist;
        /**
         * The room 1 in the vertex.
         */
        private Room room1;
        /**
         * The room 2 in the vertex.
         */
        private Room room2;

        /**
         * Create a vertex by 2 rooms.
         */
        public VertexOfPoints(Room room1, Room room2) {
            this(room1.getCentralPoint(), room2.getCentralPoint());
            this.room1 = room1;
            this.room2 = room2;
        }

        /**
         * Create a vertex by 2 points.
         */
        public VertexOfPoints(Point point1, Point point2) {
            this.room1 = null;
            this.room2 = null;
            this.dist = Utils.getDistance(point1.getX(), point1.getY(), point2.getX(), point2.getY());
        }

        /**
         * Use the distance to compare 2 vertexes.
         */
        @Override
        public int compareTo(VertexOfPoints v) {
            return this.dist - v.dist;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || o.getClass() != VertexOfPoints.class) {
                return false;
            }
            return ((room1.equals(((VertexOfPoints) o).room1)
                    && room2.equals(((VertexOfPoints) o).room2))
                    || (room2.equals(((VertexOfPoints) o).room1)
                    && room1.equals(((VertexOfPoints) o).room2)));
        }

        @Override
        public int hashCode() {
            return room1.hashCode() + room2.hashCode();
        }
    }

    /**
     * Create a Priority Queue for Kruskal Algorithm.
     */
    public KruskalForMst(List<Room> rooms) {
        this.queue = new PriorityQueue<>();
        this.rooms = rooms;
        ArrayList<VertexOfPoints> visited = new ArrayList<>();
        for (Room currRoom : rooms) {
            for (Room targetRoom : rooms) {
                if (!currRoom.equals(targetRoom)) {
                    VertexOfPoints vertex = new VertexOfPoints(currRoom, targetRoom);
                    if (!visited.contains(vertex)) {
                        queue.add(vertex);
                        visited.add(vertex);
                    }
                }
            }
        }
    }

    /**
     * Return an Array of the routes.
     */
    public List<Room[]> generateVertexes() {
        DisjointSet djs = buildDjs();
        ArrayList<Room[]> vertexList = new ArrayList<>();
        while (!this.queue.isEmpty()) {
            VertexOfPoints vertex = queue.poll();
            int room1 = vertex.room1.getSwIndex();
            int room2 = vertex.room2.getSwIndex();
            if (!djs.isConnected(room1, room2)) {
                djs.connect(room1, room2);
                vertexList.add(new Room[]{vertex.room1, vertex.room2});
            }
        }
        return vertexList;
    }

    /**
     * Build a DisjointSet for Kruskal Algorithm.
     */
    private DisjointSet buildDjs() {
        DisjointSet djs = new DisjointSet();
        for (Room room : rooms) {
            int sw = room.getSw().parseIndex();
            List<Point> walls = room.getWalls();
            connectDjs(walls, sw, djs);
            List<Point> bricks = room.getBricks();
            connectDjs(bricks, sw, djs);
        }
        return djs;
    }

    /**
     * Connect the bricks to the room.
     */
    private void connectDjs(List<Point> points, int sw, DisjointSet djs) {
        for (Point point : points) {
            djs.connect(sw, point.parseIndex());
        }
    }
}
