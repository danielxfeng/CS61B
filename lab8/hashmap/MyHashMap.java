package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Daniel Feng
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    protected class MyHashMapIterator implements Iterator<K> {

        private int idx;
        private final K[] keysArr;

        private MyHashMapIterator() {
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

    /* Instance Variables */
    final private static int EXPAND_FACTOR = 2;
    private Collection<Node>[] buckets;
    private int size;
    private final int initialSize;
    private double maxLoad;
    private Set<K> keySet;


    /**
     * Constructors
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.maxLoad = maxLoad;
        clear();
    }
    public MyHashMap(int initialSize) {
        this(initialSize, 1.5); // initMaxLoad = 1.5;
    }
    public MyHashMap() {
        this(8); // init size = 8;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] buckets = new Collection[tableSize];
        return buckets;
    }

    /**
     * Clear the buckets and the size.
     */
    @Override
    public void clear() {
        buckets = createTable(this.initialSize);
        size = 0;
        keySet = new HashSet<>();
    }

    /**
     * Return if the map contains the key.
     */
    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Return value of the key that stores in the map, return null if not found.
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Cannot get null");
        }

        Collection<Node> bucket = buckets[getIdx(key, this.buckets)];

        if (bucket == null || bucket.isEmpty()) { // if Empty
            return null;
        }

        for (Node n : bucket) {  // for Collisions
            if (n.key.equals(key)) {
                return n.value;
            }
        }
        return null;
    }

    /**
     * Return the size of the map.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Put the key value data into the map.
     */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Cannot put null");
        }

        expandSize();
        put(key, value, this.buckets, true);
    }

    /**
     * A helper method to put value to the buckets .
     * @param key The key of data.
     * @param value The value of data.
     * @param buckets The target buckets that the data put.
     * @param sizeAdd If update the size when add node, set to false when expand the size.
     */
    private void put(K key, V value, Collection<Node>[] buckets, boolean sizeAdd) {
        int idx = getIdx(key, buckets);

        if (buckets[idx] == null) {
            buckets[idx] = createBucket();
        }
        put(key, value, buckets[idx], sizeAdd);
    }

    /**
     * A helper method to get the idx of buckets.
     * @param key The key of data.
     * @param buckets The target buckets that the data put.
     */
    private int getIdx(K key, Collection<Node>[] buckets) {
        int idx = key.hashCode() % buckets.length;
        if (idx < 0) {
            idx += buckets.length;
        }
        return idx;
    }

    /**
     * A helper method to put value to the SINGLE bucket .
     * @param key The key of data.
     * @param value The value of data.
     * @param bucket The target bucket that the data put.
     * @param sizeAdd If update the size when add node.
     */
    private void put(K key, V value, Collection<Node> bucket, boolean sizeAdd) {

        if (!bucket.isEmpty()) {  // Modify the value if the key exists.
            for (Node n : bucket) {
                if (n.key.equals(key)) {
                    n.value = value;
                    return;
                }
            }
        }

        // Add new node otherwise;
        Node n = createNode(key, value);
        bucket.add(n);
        if (sizeAdd) {
            size++;
            keySet.add(key);
        }
    }

    /**
     * Expand the buckets size to current size * EXPAND_FACTOR.
     */
    private void expandSize(){
        if (((double) size()) / this.buckets.length <= maxLoad) {
            return;
        }

        int targetSize = this.buckets.length * EXPAND_FACTOR;
        Collection<Node>[] newBuckets = createTable(targetSize);
        for (K key : this) {
            put(key, get(key), newBuckets, false);
        }
        buckets = newBuckets;
    }

    /**
     * Return a hashSet of keys.
     */
    @Override
    public Set<K> keySet() {
        return this.keySet;
    }

    /**
     * Return the value of removed item with the same key, return null if not found.
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Cannot remove null");
        }

        return remove(false, key, null);
    }

    /**
     * Return the value of removed item with the same key and value, return null if not found.
     */
    @Override
    public V remove(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Cannot remove null");
        }

        return remove(true, key, value);
    }

    /**
     * A helper method for the public remove method
     * @param matchValue if the value should be matched.
     */
    private V remove(boolean matchValue, K key, V value) {
        Collection<Node> bucket = buckets[getIdx(key, this.buckets)];

        if (bucket == null || bucket.isEmpty()) { // if Empty
            return null;
        }

        for (Node n : bucket) {  // for Collisions
            if (n.key.equals(key) && (!matchValue || n.value.equals(value))) {
                // key MATCHED && (NOT check value || value MATCHED)
                V v = n.value;
                bucket.remove(n);
                keySet.remove(key);
                size--;
                return v;
            }
        }
        return null;
    }

    /**
     * Return an iterator of keys.
     */
    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }

}
