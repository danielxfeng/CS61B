package bstmap;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{

    private int size;
    private Node<K, V> root;


    /** A helper class BST tree that actually save the BST node to
     *  avoid the naked recursive call in BSTMap class.
     */
    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> left;
        private Node<K, V> right;

        private Node (K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    /**
     * Implement an Iterator.
     */
    private class BSTMapIterator implements Iterator<K> {

        private int idx;
        private final K[] keysArr;

        private BSTMapIterator() {
            keysArr = (K[]) keySet().toArray();
            idx = 0;
        }

        @Override
        public boolean hasNext() {
            return keysArr.length > idx;
        }

        @Override
        public K next() {
            K key = keysArr[idx];
            idx++;
            return key;
        }
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    /**
     * Return the Set of keys in the tree.
     */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        // return keySet(root, keys); // if you want to call a Recursion version.
        return keySet(keys); // if you want to call an Iteration version.
    }

    /**
     * Return the Set of keys in the tree. Iteration version.
     */
    private Set<K> keySet(Node<K, V> node, Set<K> keys) {
        if (node == null) {
            return null;
        }
        keys.add(node.key);
        keySet(node.left, keys);
        keySet(node.right, keys);
        return keys;
    }

    /**
     * Return the Set of keys in the tree. Iteration version.
     */
    private Set<K> keySet(Set<K> keys) {

        if (root == null) {
            return null;
        }

        // set a queue and add the nodes that have not been iterated to the queue.
        Deque<Node<K, V>> queue = new ArrayDeque<>();
        queue.addLast(root);

        // iterate when there is node that has not been iterated.
        while (!queue.isEmpty()) {
            Node<K, V> curr = queue.pollFirst();
            keys.add(curr.key);
            if (curr.left != null) {
                queue.addLast(curr.left);
            }
            if (curr.right != null) {
                queue.addLast(curr.right);
            }
        }
        return keys;
    }

    /** Removes the entry for the specified key only if it is currently mapped to
     *  Return the value of removed node or null if the node is not exist.
     */
    @Override
    public V remove(K key, V value) {

        if (key == null) {
            throw new IllegalArgumentException("argument to remove is null");
        }

        return remove(true, key, value);
    }

    /** Removes the mapping for the specified key from this map if present.
     *  Return the value of removed node or null if the node is not exist.
     */
    @Override
    public V remove(K key) {

        if (key == null) {
            throw new IllegalArgumentException("argument to remove is null");
        }

        return remove(false, key, null);
    }

    /** Removes the mapping for the specified key from this map if present.
     *  Return the value of removed node or null if the node is not exist.
     *
     * @param matchValue if you want to also check the value before remove.
     * @param key the key that need be removed;
     * @param value if matchValue is true, fill the param with the value.
     */
    private V remove(boolean matchValue, K key, V value) {

        // Find the Node that need to be removed.
        Node<K, V>[] nodes = getNode(root, matchValue, key, value, null);
        if (nodes == null) {   // Not found
            return null;
        }
        Node<K, V> targetNode = nodes[0];
        Node<K, V> parent = nodes[1];
        
        return remove(parent, targetNode);
    }

    /** Removes the mapping for the specified key from this map if present.
     *  Return the value of removed node or null if the node is not exist.
     *
     * @param parent the targetNode's parent Node.
     * @param targetNode the node that need be removed;
     */
    private V remove(Node<K, V> parent, Node<K, V> targetNode) {
        // Preparation
        int direction;
        V v = targetNode.value;
        if (parent == null) {
            direction = 0;
        } else {
            direction = parent.key.compareTo(targetNode.key);
        }

        // Get children
        int children = 0;
        if (targetNode.left != null) {
            children++;
        }
        if (targetNode.right != null) {
            children++;
        }

        // Remove
        switch (children) {
            case 0 -> pointToNode(parent, direction, null);
            case 1 -> pointToNode(parent, direction, getSingleChild(targetNode));
            case 2 -> {
                K replacementKey = findMax(targetNode.left, targetNode.left.key);  // find the replacement node
                V replacementValue = remove(replacementKey); // remove the replacement node
                targetNode.key = replacementKey; // set the remove node to the replacement
                targetNode.value = replacementValue;
            }
            default -> throw new RuntimeException("Should not be here!");
        }

        return v;
    }

    private Node<K, V>[] getNode (Node<K, V> node, boolean matchValue, K key, V value, Node<K, V> parent) {
        if (node == null) { // Under the leaf, not find;
            return null;
        }

        int comp = node.key.compareTo(key);

        if (comp > 0) { // Choose the direction;
            return getNode(node.left, matchValue, key, value, node);
        } else if (comp < 0) {
            return getNode(node.right, matchValue, key, value, node);
        }
        if (!matchValue || node.value == value) {
            return new Node[]{node, parent};
        }
        return null;
    }
    
    /**
     * A helper method to point node's child to the target node .
     * @param node the parent node.
     * @param direction go right if < 0, go left if > 0, root if 0.
     * @param replacementNode the node to be pointed to.             
     */
    private void pointToNode(Node<K, V> node, int direction, Node<K, V> replacementNode) {
        if (direction == 0) {
            root = replacementNode;
        } else if (direction > 0) {
            node.left = replacementNode;
        } else {
            node.right = replacementNode;
        }
        size--;
    }
    
    private Node<K, V> getSingleChild(Node<K, V> node) {
        if (node.left != null) {
            return node.left;
        }
        return node.right;
    }
    
    private K findMax(Node<K, V>node, K maxKey) {
        if (node == null) {
            return maxKey;
        }
        if (maxKey == null || node.key.compareTo(maxKey) > 0) {
            maxKey = node.key;
        }
        return findMax(node.right, maxKey);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("argument to put is null");
        }

        root = put(root, key, value);
    }

    /**
     *  A helper method for avoiding the naked recursive calls.
     */
    private Node<K, V> put(Node<K, V> node, K key, V value) {
        if (node == null) { // Under the leaf, not find;
            size++;
            return new Node<>(key, value);
        }

        int comp = node.key.compareTo(key);

        if (comp > 0) { // Choose the direction;
            node.left = put(node.left, key, value);
        } else if (comp < 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
        }
        return node;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {

        if (key == null) {
            throw new IllegalArgumentException("argument to get is null");
        }

        return get(root, key);
    }

    /* A helper method to avoid naked the recursive call.
     */
    private V get(Node<K, V> node, K key) {
        if (node == null) { // Under the leaf, not find;
            return null;
        }

        int comp = node.key.compareTo(key);

        if (comp > 0) { // Choose the direction;
            return get(node.left, key);
        } else if (comp < 0) {
            return get(node.right, key);
        }
        return node.value;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to containsKey is null");
        }

        return containsKey(root, key);
    }

    private boolean containsKey(Node<K, V> node, K key) {
        if (node == null) { // Under the leaf, not find;
            return false;
        }

        int comp = node.key.compareTo(key);

        if (comp > 0) { // Choose the direction;
            return containsKey(node.left, key);
        } else if (comp < 0) {
            return containsKey(node.right, key);
        }
        return true;
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }

    /**
     * Print the tree in-order;
     */
    public void printInOrder() {
        StringBuilder sb = new StringBuilder();
        printInOrder(root, sb);
        System.out.println(sb);

    }

    /**
     * Print the tree in-order;
     */
    private void printInOrder(Node<K, V> node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        printInOrder(node.left, sb);
        sb.append(node.key);
        sb.append(", ");
        printInOrder(node.right, sb);
    }
}
