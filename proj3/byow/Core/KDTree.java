package byow.Core;

import java.io.Serializable;

/**
 * This class is an implementation of KD Tree.
 * A K-D Tree is a binary tree in which each node represents a k-dimensional point.
 * Every non-leaf node in the tree acts as a hyperplane, dividing the space into two partitions.
 * This hyperplane is perpendicular to the chosen axis, which is associated with one of
 * the K dimensions.
 */
public class KDTree {

    /** Root of the KD Tree. */
    private Tree root;

    /** A nested class to save a data structure of the Tree. */
    private static class Tree implements Serializable {

        /** The key of a room. */
        private String key;
        /** The X point of the northwest corner of a room. */
        private int x;
        /** The Y point of the northwest corner of a room. */
        private int y;
        /** The left child of a room. */
        private Tree left;
        /** The right child of a room. */
        private Tree right;

        public Tree(String key, Point p) {
            this.key = key;
            this.x = p.getX();
            this.y = p.getY();
            this.left = null;
            this.right = null;
        }

    }

    public KDTree() {
        this.root = null;
    }

    /** Add a Room to a KD Tree. */
    public void add(Room room) {
        Tree tree = new Tree(room.getKey(), room.getPoint());
        tree.left = null;
        tree.right = null;
        if (this.root == null) {
            this.root = tree;
            return;
        }
        add(root, tree);
    }

    /** Insert a tree to a KD Tree. */
    public void add(Tree root, Tree tree) {
        //TODO
        return;
    }

    /** Get a Room from the KD Tree by the point. */
    public String getRoomByPoint(Point p) {
        //TODO
        return null;
    }

    /** Get the Nearest Room from the KD Tree by the key of a room. */
    public String getNearestRoom(Rooms rooms, String key) {
        return getNearestRoom(rooms.getRoom(key).getPoint());
    }

    /** Get the Nearest Room from the KD Tree by  a room. */
    public String getNearestRoom(Room room) {
        return getNearestRoom(room.getPoint());
    }

    /** Get the Nearest Room from the KD Tree by a point. */
    public String getNearestRoom(Point p) {
        //TODO
        return null;
    }

}
