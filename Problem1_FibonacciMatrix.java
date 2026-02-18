/**
 * Problem 1: Fibonacci Matrix
 * Create a matrix where each row is a shifted Fibonacci sequence.
 * For example, if size is 4:
 * Row 0: 0 1 1 2
 * Row 1: 1 1 2 3
 * Row 2: 1 2 3 5
 * Row 3: 2 3 5 8
 */
public class Problem1_FibonacciMatrix {
    public static int[][] createFibonacciMatrix(int size) {
        if (size <= 0) return new int[0][0];
        
        int[][] matrix = new int[size][size];
        
        // Helper function to generate Fibonacci numbers
        for (int i = 0; i < size; i++) {
            // Each row starts with fibonacci numbers at position i and i+1
            int fib1 = getFibonacci(i);
            int fib2 = getFibonacci(i + 1);
            
            matrix[i][0] = fib1;
            matrix[i][1] = fib2;
            
            // Fill the rest of the row using Fibonacci rule
            for (int j = 2; j < size; j++) {
                matrix[i][j] = matrix[i][j-1] + matrix[i][j-2];
            }
        }
        
        return matrix;
    }
    
    private static int getFibonacci(int n) {
        if (n <= 1) return n;
        
        int prev = 0, curr = 1;
        for (int i = 2; i <= n; i++) {
            int temp = curr;
            curr = prev + curr;
            prev = temp;
        }
        return curr;
    }
    
    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Fibonacci Matrix of size 5:");
        int[][] matrix = createFibonacciMatrix(5);
        printMatrix(matrix);
    }
}