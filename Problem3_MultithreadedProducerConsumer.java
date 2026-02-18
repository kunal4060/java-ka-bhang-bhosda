/**
 * Problem 3: Multithreaded Producer Consumer with Priority Queue
 * Implement a thread-safe producer-consumer system where consumers process higher priority items first.
 * Use synchronized collections and proper wait/notify mechanisms.
 */
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class Problem3_MultithreadedProducerConsumer {
    
    static class PriorityItem implements Comparable<PriorityItem> {
        private int priority;
        private String data;
        private long timestamp;
        
        public PriorityItem(int priority, String data) {
            this.priority = priority;
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }
        
        @Override
        public int compareTo(PriorityItem other) {
            // Higher priority number means higher priority
            int cmp = Integer.compare(other.priority, this.priority);
            if (cmp == 0) {
                // If priorities are equal, older items come first (FIFO)
                return Long.compare(this.timestamp, other.timestamp);
            }
            return cmp;
        }
        
        @Override
        public String toString() {
            return "[" + priority + "] " + data;
        }
    }
    
    static class SharedBuffer {
        private final Queue<PriorityItem> buffer;
        private final int capacity;
        
        public SharedBuffer(int capacity) {
            this.buffer = new PriorityQueue<>();
            this.capacity = capacity;
        }
        
        public synchronized void put(PriorityItem item) throws InterruptedException {
            while (buffer.size() >= capacity) {
                System.out.println("Buffer full, producer waiting...");
                wait(); // Wait until space is available
            }
            buffer.add(item);
            System.out.println("Produced: " + item + " (Size: " + buffer.size() + ")");
            notifyAll(); // Notify waiting consumers
        }
        
        public synchronized PriorityItem get() throws InterruptedException {
            while (buffer.isEmpty()) {
                System.out.println("Buffer empty, consumer waiting...");
                wait(); // Wait until item is available
            }
            PriorityItem item = buffer.poll();
            System.out.println("Consumed: " + item + " (Size: " + buffer.size() + ")");
            notifyAll(); // Notify waiting producers
            return item;
        }
    }
    
    static class Producer implements Runnable {
        private SharedBuffer buffer;
        private String name;
        private Random random;
        
        public Producer(SharedBuffer buffer, String name) {
            this.buffer = buffer;
            this.name = name;
            this.random = new Random();
        }
        
        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    int priority = random.nextInt(10); // Random priority 0-9
                    String data = name + "_item_" + i;
                    PriorityItem item = new PriorityItem(priority, data);
                    
                    buffer.put(item);
                    Thread.sleep(random.nextInt(500) + 100); // Random delay
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    static class Consumer implements Runnable {
        private SharedBuffer buffer;
        private String name;
        
        public Consumer(SharedBuffer buffer, String name) {
            this.buffer = buffer;
            this.name = name;
        }
        
        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    PriorityItem item = buffer.get();
                    System.out.println(name + " processing: " + item);
                    Thread.sleep(600); // Processing time
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        SharedBuffer buffer = new SharedBuffer(3); // Small buffer to demonstrate blocking
        
        Thread prod1 = new Thread(new Producer(buffer, "Producer1"));
        Thread prod2 = new Thread(new Producer(buffer, "Producer2"));
        Thread cons1 = new Thread(new Consumer(buffer, "Consumer1"));
        Thread cons2 = new Thread(new Consumer(buffer, "Consumer2"));
        
        System.out.println("Starting multithreaded producer-consumer simulation...");
        prod1.start();
        prod2.start();
        cons1.start();
        cons2.start();
        
        prod1.join();
        prod2.join();
        cons1.join();
        cons2.join();
        
        System.out.println("Simulation completed.");
    }
}