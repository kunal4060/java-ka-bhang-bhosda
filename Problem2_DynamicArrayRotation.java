/**
 * Problem 2: Dynamic Array Rotation with Multiple Queries
 * Given an array and multiple rotation operations, efficiently handle all rotations.
 * Each rotation can be left or right by a specific number of positions.
 * Optimize for multiple queries on the same array.
 */
import java.util.*;

public class Problem2_DynamicArrayRotation {
    
    public static class RotatableArray {
        private int[] arr;
        private int offset; // Represents virtual rotation
        
        public RotatableArray(int[] initialArray) {
            this.arr = Arrays.copyOf(initialArray, initialArray.length);
            this.offset = 0;
        }
        
        public void rotateLeft(int positions) {
            int n = arr.length;
            if (n == 0) return;
            positions = positions % n;
            offset = (offset + positions) % n;
        }
        
        public void rotateRight(int positions) {
            int n = arr.length;
            if (n == 0) return;
            positions = positions % n;
            offset = (offset - positions + n) % n;
        }
        
        public int getElement(int index) {
            int n = arr.length;
            if (n == 0) throw new IndexOutOfBoundsException("Array is empty");
            index = (index + offset + n) % n;
            return arr[index];
        }
        
        public int[] getCurrentArray() {
            int n = arr.length;
            int[] result = new int[n];
            for (int i = 0; i < n; i++) {
                result[i] = getElement(i);
            }
            return result;
        }
        
        public void printCurrentState() {
            System.out.println(Arrays.toString(getCurrentArray()));
        }
    }
    
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        RotatableArray rotArr = new RotatableArray(arr);
        
        System.out.println("Original array:");
        rotArr.printCurrentState(); // [1, 2, 3, 4, 5]
        
        System.out.println("After rotating left by 2:");
        rotArr.rotateLeft(2);
        rotArr.printCurrentState(); // [3, 4, 5, 1, 2]
        
        System.out.println("After rotating right by 1:");
        rotArr.rotateRight(1);
        rotArr.printCurrentState(); // [2, 3, 4, 5, 1]
        
        System.out.println("Element at index 0: " + rotArr.getElement(0)); // 2
        System.out.println("Element at index 2: " + rotArr.getElement(2)); // 4
    }
}