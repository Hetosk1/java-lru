import java.util.HashMap;

class LRUCache {
    private class Node {
        int key, value;
        Node prev, next;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final HashMap<Integer, Node> cache;
    private final Node head, tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.head = new Node(-1, -1); // Dummy head
        this.tail = new Node(-1, -1); // Dummy tail
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        if (!cache.containsKey(key)) return -1; // Not found

        Node node = cache.get(key);
        moveToHead(node); // Move accessed node to the front (most recently used)
        return node.value;
    }

    public void put(int key, int value) {
        if (cache.containsKey(key)) {
            // Update value and move to head
            Node node = cache.get(key);
            node.value = value;
            moveToHead(node);
        } else {
            // Create a new node
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            addToHead(newNode);

            if (cache.size() > capacity) {
                removeLRU(); // Remove least recently used item
            }
        }
    }

    private void moveToHead(Node node) {
        removeNode(node);
        addToHead(node);
    }

    private void addToHead(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void removeLRU() {
        Node lru = tail.prev;
        removeNode(lru);
        cache.remove(lru.key);
    }

    public void printCache() {
        Node current = head.next;
        System.out.print("Cache: ");
        while (current != tail) {
            System.out.print("[" + current.key + ":" + current.value + "] ");
            current = current.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        LRUCache lru = new LRUCache(3);
        lru.put(1, 10);
        lru.put(2, 20);
        lru.put(3, 30);
        lru.printCache(); // [3:30] [2:20] [1:10]
        
        lru.get(1);
        lru.printCache(); // [1:10] [3:30] [2:20]
        
        lru.put(4, 40); // Removes least recently used (key=3)
        lru.printCache(); // [4:40] [1:10] [2:20]
    }
}
