package hw4.hash;

import org.junit.Test;
import static org.junit.Assert.*;

<<<<<<< HEAD
import java.util.*;
=======
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
>>>>>>> ea721d79cb8f8c9d8fb51240be7a069e543ed26b


public class TestSimpleOomage {

    @Test
    public void testHashCodeDeterministic() {
        SimpleOomage so = SimpleOomage.randomSimpleOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    @Test
    public void testHashCodePerfect() {
        /* TODO: Write a test that ensures the hashCode is perfect,
          meaning no two SimpleOomages should EVER have the same
          hashCode!
         */
<<<<<<< HEAD
        SimpleOomage ooA;
        Set<Integer> ooSet = new HashSet<Integer>();
        int count = 0;
        for (int r = 0; r < 256 & r % 5 == 0; r++) {
            for (int g = 0; g < 256 & g % 5 == 0; g++) {
                for (int b = 0; b < 256 & b % 5 == 0; b++) {
                    ooA = new SimpleOomage(r, g, b);
                    ooSet.add(ooA.hashCode());
                    count += 1;
                }
            }
        }
        assertEquals(ooSet.size(), count);
=======
>>>>>>> ea721d79cb8f8c9d8fb51240be7a069e543ed26b
    }

    @Test
    public void testEquals() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        SimpleOomage ooB = new SimpleOomage(50, 50, 50);
        assertEquals(ooA, ooA2);
        assertNotEquals(ooA, ooB);
        assertNotEquals(ooA2, ooB);
        assertNotEquals(ooA, "ketchup");
    }

    @Test
    public void testHashCodeAndEqualsConsistency() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        HashSet<SimpleOomage> hashSet = new HashSet<>();
        hashSet.add(ooA);
        assertTrue(hashSet.contains(ooA2));
    }

    /* TODO: Once you've finished haveNiceHashCodeSpread,
    in OomageTestUtility, uncomment this test. */
<<<<<<< HEAD
    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;
        for (int i = 0; i < N; i += 1) {
            oomages.add(SimpleOomage.randomSimpleOomage());
        }
        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }
=======
    /*@Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(SimpleOomage.randomSimpleOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }*/
>>>>>>> ea721d79cb8f8c9d8fb51240be7a069e543ed26b

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestSimpleOomage.class);
    }
}
