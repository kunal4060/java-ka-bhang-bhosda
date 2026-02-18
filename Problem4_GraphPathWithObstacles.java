/**
 * Problem 4: Graph Path Finding with Obstacles and Weighted Edges
 * Find the shortest path in a grid where some cells are blocked and edges have different weights.
 * Use Dijkstra's algorithm to find the optimal path from top-left to bottom-right.
 */
import java.util.*;

public class Problem4_GraphPathWithObstacles {
    
    static class Cell implements Comparable<Cell> {
        int x, y;
        int distance; // Distance/cost to reach this cell
        
        public Cell(int x, int y, int distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }
        
        @Override
        public int compareTo(Cell other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
    
    // Directions: up, down, left, right
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};
    
    public static int findShortestPath(int[][] grid) {
        int rows = grid.length;
        if (rows == 0) return -1;
        int cols = grid[0].length;
        if (cols == 0) return -1;
        
        // Check if start or end is blocked
        if (grid[0][0] == -1 || grid[rows-1][cols-1] == -1) {
            return -1; // Start or end is blocked
        }
        
        // Distance matrix to store minimum distances
        int[][] dist = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }
        
        // Priority queue for Dijkstra's algorithm
        PriorityQueue<Cell> pq = new PriorityQueue<>();
        
        // Start from top-left corner
        dist[0][0] = grid[0][0]; // Cost to enter starting cell
        pq.offer(new Cell(0, 0, dist[0][0]));
        
        while (!pq.isEmpty()) {
            Cell current = pq.poll();
            int x = current.x;
            int y = current.y;
            int currentDist = current.distance;
            
            // If we reached the destination
            if (x == rows - 1 && y == cols - 1) {
                return currentDist;
            }
            
            // Skip if we've found a better path already
            if (currentDist > dist[x][y]) {
                continue;
            }
            
            // Explore all 4 directions
            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];
                
                // Check bounds
                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols) {
                    // Check if the cell is not blocked (-1 means blocked)
                    if (grid[newX][newY] != -1) {
                        int newDist = currentDist + grid[newX][newY];
                        
                        // If we found a shorter path to this cell
                        if (newDist < dist[newX][newY]) {
                            dist[newX][newY] = newDist;
                            pq.offer(new Cell(newX, newY, newDist));
                        }
                    }
                }
            }
        }
        
        // If destination is unreachable
        return dist[rows-1][cols-1] == Integer.MAX_VALUE ? -1 : dist[rows-1][cols-1];
    }
    
    // Method to print the path as well
    public static List<int[]> findShortestPathWithSteps(int[][] grid) {
        int rows = grid.length;
        if (rows == 0) return null;
        int cols = grid[0].length;
        if (cols == 0) return null;
        
        if (grid[0][0] == -1 || grid[rows-1][cols-1] == -1) {
            return null;
        }
        
        int[][] dist = new int[rows][cols];
        int[][] parent = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            Arrays.fill(parent[i], -1);
        }
        
        PriorityQueue<Cell> pq = new PriorityQueue<>();
        dist[0][0] = grid[0][0];
        pq.offer(new Cell(0, 0, dist[0][0]));
        
        while (!pq.isEmpty()) {
            Cell current = pq.poll();
            int x = current.x;
            int y = current.y;
            int currentDist = current.distance;
            
            if (x == rows - 1 && y == cols - 1) {
                // Reconstruct path
                List<int[]> path = new ArrayList<>();
                int px = rows - 1, py = cols - 1;
                
                while (px != -1 && py != -1) {
                    path.add(new int[]{px, py});
                    if (px == 0 && py == 0) break;
                    
                    // Find parent - this is a simplified approach
                    // In a complete implementation, we'd track parents differently
                    boolean foundParent = false;
                    for (int i = 0; i < 4; i++) {
                        int pX = px - dx[i];
                        int pY = py - dy[i];
                        if (pX >= 0 && pX < rows && pY >= 0 && pY < cols && 
                            dist[pX][pY] < dist[px][py]) {
                            px = pX;
                            py = pY;
                            foundParent = true;
                            break;
                        }
                    }
                    if (!foundParent) break;
                }
                
                Collections.reverse(path);
                return path;
            }
            
            if (currentDist > dist[x][y]) {
                continue;
            }
            
            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];
                
                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && grid[newX][newY] != -1) {
                    int newDist = currentDist + grid[newX][newY];
                    
                    if (newDist < dist[newX][newY]) {
                        dist[newX][newY] = newDist;
                        pq.offer(new Cell(newX, newY, newDist));
                    }
                }
            }
        }
        
        return null;
    }
    
    public static void main(String[] args) {
        // Example grid where -1 represents obstacles/blocked cells
        // Positive numbers represent the cost to enter that cell
        int[][] grid = {
            {1, 3, 1, 2},
            {1, -1, 1, 3},  // -1 is a blocked cell
            {4, 2, 1, 1},
            {2, 1, 3, 1}
        };
        
        System.out.println("Grid:");
        for (int[] row : grid) {
            for (int val : row) {
                if (val == -1) System.out.print("X ");  // X represents blocked
                else System.out.print(val + " ");
            }
            System.out.println();
        }
        
        int shortestDistance = findShortestPath(grid);
        System.out.println("\nShortest distance: " + shortestDistance);
        
        List<int[]> path = findShortestPathWithSteps(grid);
        if (path != null) {
            System.out.println("Path from (0,0) to (" + (grid.length-1) + "," + (grid[0].length-1) + "):");
            for (int[] pos : path) {
                System.out.print("(" + pos[0] + "," + pos[1] + ") ");
            }
            System.out.println();
        } else {
            System.out.println("No path exists!");
        }
    }
}