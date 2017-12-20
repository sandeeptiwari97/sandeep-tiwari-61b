package synthesizer;
//import java.util.Iterator;
/**
 * Created by sandeeptiwari on 2/17/17.
 */
public interface BoundedQueue<T> extends Iterable<T> {
    int capacity();
    int fillCount();
    void enqueue(T x);
    T dequeue();
    T peek();
    boolean isEmpty();
    boolean isFull();

}

