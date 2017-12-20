package hw4.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* TODO: Write a utility function that returns true if the given oomages 
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
<<<<<<< HEAD
        int buckets;
        int[] position = new int[M];
        for (Oomage oos : oomages) {
            buckets = (oos.hashCode() & 0x7FFFFFF) % M;
            position[buckets] += 1;
        }
        for (int i = 0; i < M; i++) {
            if (position[i] < oomages.size() / 50 || position[i] > oomages.size() / 2.5) {
                return false;
            }
        }
        return true;
=======
        return false;
>>>>>>> ea721d79cb8f8c9d8fb51240be7a069e543ed26b
    }
}
