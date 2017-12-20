import java.util.*;

/**
 * Class with 2 ways of doing Counting sort, one naive way and one "better" way
 * 
 * @author 	Akhil Batra
 * @version	1.1 - April 16, 2016
 * 
**/
public class CountingSort {
    
    /**
     * Counting sort on the given int array. Returns a sorted version of the array.
     *  does not touch original array (non-destructive method)
     * DISCLAIMER: this method does not always work, find a case where it fails
     *
     * @param arr int array that will be sorted
     * @return the sorted array
    **/
    public static int[] naiveCountingSort(int[] arr) {
        // find max
        int max = Integer.MIN_VALUE;
        for (int i : arr) {
            if (i > max) {
                max = i;
            }
        }

        // gather all the counts for each value
        int[] counts = new int[max + 1];
        for (int i : arr) {
            counts[i] += 1;
        }

        // put the value count times into a new array
        int[] sorted = new int[arr.length];
        int k = 0;
        for (int i = 0; i < counts.length; i += 1) {
            for (int j = 0; j < counts[i]; j += 1, k += 1) {
                sorted[k] = i;
            }
        }

        // return the sorted array
        return sorted;
    }

    /**
     * Counting sort on the given int array, must work even with negative numbers.
     * Note, this code does not need to work for ranges of numbers greater
     * than 2 billion.
     *  does not touch original array (non-destructive method)
     * 
     * @param toSort int array that will be sorted
    **/
    public static int[] betterCountingSort(int[] toSort) {
        //TODO make it work with arrays containing negative numbers.
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int i : toSort) {
            if (i > max) {
                max = i;
            } else if (i < min) {
                min = i;
            }
        }

        // -2, 4, 2, 3, 0, 5, -1
        int[] intCounts = new int[max + 1 - min];
        for (int i : toSort) {
            intCounts[i - min] += 1;
        }

        // put the value count times into a new array
        int[] sortedInts = new int[toSort.length];
        int k = 0;
        for (int i = 0; i < intCounts.length; i++) {
            for (int j = 0; j < intCounts[i]; j++) {
                sortedInts[k] = i;
                k += 1;
            }
        }
        return sortedInts;
    }
}
