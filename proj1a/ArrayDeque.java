public class ArrayDeque<Item> {
    private int size;
    private int sent;
    private Item[] things;
    private int factor = 2;
    private int cap = 8;
    private int tempnext;
    private int templast;

    @SuppressWarnings("unchecked")
    public ArrayDeque() {
        this.things = (Item[]) new Object[size];
        this.sent = 0;
        this.size = 0;
        this.tempnext = cap / 2;
        this.templast = cap / 2 + 1;
    }

    @SuppressWarnings("unchecked")
    private void resize(int amount, int shift) {
        if (size == 0) {
            amount = 1;
        } 
        /* Referenced 'http://stackoverflow.com/questions/
           529085/how-to-create-a-generic-array-in-java'
           to see how to cast the Item array type. */
        Item[] newlist = (Item[]) new Object[amount];
        System.arraycopy(things, sent, newlist, shift, size);
        things = newlist;
    }

    @SuppressWarnings("unchecked")
    private void downsize(int amount) {
        Item[] downlist = (Item[]) new Object[amount];
        int smaller = amount / 2 - cap / 2;
        for (int i = 0; i < size; i++) {
            downlist[smaller] = things[(tempnext + i + 1) % cap];
            cap = cap + 1;
        }
        tempnext = (amount / 2) - (cap / 2) + 1;
        templast = smaller;
        cap = amount;
        things = downlist;
    }

    /** Adds an item to the front of the Deque. */
    public void addFirst(Item addf) {
        resize(size * factor, 1);
        things[0] = addf;
        size = size + 1;
        sent = 0;
    }

    /** Adds an item to the back of the Deque. */
    public void addLast(Item addl) {
        resize(size * factor, 0);
        things[size] = addl;
        size = size + 1;
        sent = 0;
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return (size == 0);
    }

    /** Returns the number of items in the Deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the Deque from first to last, separated by a space. */
    public void printDeque() {
        int marker = sent;
        while (marker < size) {
            System.out.print(things[marker] + " ");
            marker += 1;
        }
    }

    /** Removes and returns the item at the front of the Deque. 
    // If no such item exists, returns null. */
    public Item removeFirst() {
        if (size == 0) {
            return null;
        }
        Item a = things[sent];
        size = size - 1;
        sent = sent + 1;
        return a;
    }

    /** Removes and returns the item at the back of the Deque. 
    // If no such item exists, returns null. */
    public Item removeLast() {
        if (size == 0) {
            return null;
        }
        Item b = things[sent + size - 1];
        size = size - 1; 
        return b;
    }

    /** Gets the item at the given index, where 0 is the front, 
    1 is the next item, and so forth. If no such item exists, 
    returns null. Must not alter the deque! */
   
    public Item get(int index) {
        if (index >= size) {
            return null;
        } else {
            int marker = sent;
            while (index > 0) {
                marker = marker + 1;
                index = index - 1;
            }
            return things[marker];
        }
    }

    /* public static void main(String[] args) {

    } */
}
