public interface Deque<Item> {
    void addFirst(Item x);
    void addLast(Item x);
    Item removeFirst();
    Item removeLast();
    boolean isEmpty();
    int size();
    void printDeque();
    Item get(int index);
}
