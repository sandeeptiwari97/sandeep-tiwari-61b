import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Random;

public class TestArrayDeque1B {
    private static OperationSequence opSeq = new OperationSequence();
    private static Random random = new Random();

    /* Got this implementation from http://stackoverflow.com/questions/20389890/generating-
    *  a-random-number-between-1-and-10-java*/
    public static int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /* Randomly makes calls on addFirst, addLast, removeFirst, and removeLast for both
    *  the student input deque and the real deque. Then compares that the deques. */
    @Test
    public void randomTest() {
        for (int i = 0; i < 1000; i++) {
            StudentArrayDeque<Integer> testDeque = new StudentArrayDeque<Integer>();
            ArrayDequeSolution<Integer> realDeque = new ArrayDequeSolution<Integer>();
            for (int j = 0; j < 100; j++) {
                int n = randomInt(1, 4);
                if (n == 1) {
                    testDeque.addFirst(n);
                    realDeque.addFirst(n);
                    DequeOperation deqop = new DequeOperation("addFirst", n);
                    opSeq.addOperation(deqop);
                } else if (n == 2) {
                    testDeque.addLast(n);
                    realDeque.addLast(n);
                    DequeOperation deqop2 = new DequeOperation("addLast", n);
                    opSeq.addOperation(deqop2);
                } else if (n == 3) {
                    if ((testDeque.isEmpty()) || (realDeque.isEmpty())) {
                        continue;
                    } else {
                        DequeOperation deqop3 = new DequeOperation("get", 0);
                        opSeq.addOperation(deqop3);
                        assertEquals(opSeq.toString(), realDeque.get(0), testDeque.get(0));
                        int trf = testDeque.removeFirst();
                        int rrf = realDeque.removeFirst();
                        DequeOperation remFirst = new DequeOperation("removeFirst");
                        opSeq.addOperation(remFirst);
                        assertEquals(opSeq.toString(), rrf, trf);
                    }
                } else {
                    if ((testDeque.isEmpty()) || (realDeque.isEmpty())) {
                        continue;
                    } else {
                        DequeOperation deqop4 = new DequeOperation("get", realDeque.size() - 1);
                        opSeq.addOperation(deqop4);
                        assertEquals(opSeq.toString(), realDeque.get(realDeque.size() - 1), 
                            testDeque.get(testDeque.size() - 1));
                        int trl = testDeque.removeLast();
                        int rrl = realDeque.removeLast();
                        DequeOperation remLast = new DequeOperation("removeLast");
                        opSeq.addOperation(remLast);
                        assertEquals(opSeq.toString(), rrl, trl);
                    }
                }
                DequeOperation deqop5 = new DequeOperation("size");
                opSeq.addOperation(deqop5);
                assertEquals(opSeq.toString(), realDeque.size(), testDeque.size());

                for (int k = 0; k < realDeque.size(); k++) {
                    DequeOperation deqop6 = new DequeOperation("get", k);
                    opSeq.addOperation(deqop6);
                    assertEquals(opSeq.toString(), testDeque.get(k), realDeque.get(k));
                }
            }
        }
    }
}

