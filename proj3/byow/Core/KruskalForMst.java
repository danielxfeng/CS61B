package byow.Core;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * This class is a Kruskal Algorithm implementation to solve the MST problem.
 */
public class KruskalForMst {
    private final PriorityQueue<VertexOfRooms> queue;

    /** Create a Priority Queue for Kruskal Algorithm. */
    public KruskalForMst(Rooms rooms) {
        this.queue = new PriorityQueue<>();
        Room[] roomArray = rooms.getRooms();
        ArrayList<VertexOfRooms> visited = new ArrayList<>();
        System.out.println("There are " + roomArray.length + " rooms");
        if (roomArray != null) {
            for (Room currRoom : roomArray) {
                for (Room targetRoom : roomArray) {
                    if (!currRoom.equals(targetRoom)) {
                        VertexOfRooms vertex = new VertexOfRooms(currRoom, targetRoom);
                        if (!visited.contains(vertex)) {
                            queue.add(vertex);
                            visited.add(vertex);
                        }
                    }
                }
            }
            System.out.println("There are " + queue.size() + " vertexes in the queue.");
        }
    }

    /** Return an Array of the routes. */
    public VertexOfRooms[] generateVertexes(int[] djsArray) {
        DisjointSet djs = new DisjointSet(djsArray);
        ArrayList<VertexOfRooms> vertexList = new ArrayList<>();
        while (!this.queue.isEmpty()) {
            VertexOfRooms vertex = queue.poll();
            int room1 = vertex.getRoom1().getSw();
            int room2 = vertex.getRoom2().getSw();
            if (!djs.isConnected(room1, room2)) {
                djs.connect(room1, room2);
                vertexList.add(vertex);
            }
        }
        System.out.println("There are " + vertexList.size() + " vertexes in the list.");
        if (vertexList.isEmpty()) {
            return null;
        }
        return vertexList.toArray(new VertexOfRooms[0]);
    }
}
