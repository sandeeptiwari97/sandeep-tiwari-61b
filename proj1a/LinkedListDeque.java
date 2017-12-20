public class LinkedListDeque<Item> {
    
    private LNode sent;
    private int size;

    private class LNode {
        private Item item; 
        private LNode previous;
        private LNode next;

        LNode(Item structure, LNode p, LNode n) {
            this.item = structure;
            this.previous = p;
            this.next = n;
        }
    }

    public LinkedListDeque() {
        sent = new LNode(null, null, null);
        sent.next = sent;
        sent.previous = sent;
        size = 0;
    }

    private Item recursor(int index, LNode marker) {
        if (index == 0) {
            return marker.item;
        } else {
            marker = marker.next;
            /***** TRY INPUTTING marker.next FOR marker *****/
            return recursor(index - 1, marker);
        }
    }

    /** Like get() but uses recursion! */
    public Item getRecursive(int index) {
        if (index == 0) {
            return sent.next.item;
        }
        LNode marker = sent.next;
        /***** TRY INPUTTING sent.next FOR MARKER *****/
        return recursor(index, marker);
    }

    /** Adds an item to the front of the Deque. */
    public void addFirst(Item structure) {
        LNode node1 = new LNode(structure, sent, sent.next);
        sent.next.previous = node1;
        sent.next = node1;
        size = size + 1;
    }

    /** Adds an item to the back of the Deque. */
    public void addLast(Item structure) {
        LNode addnode = new LNode(structure, sent.previous, sent);
        sent.previous.next = addnode;
        sent.previous = addnode;
        size = size + 1;
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    /** Returns the number of items in the Deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the Deque from first to last, separated by a space. */
    public void printDeque() {
        LNode marker = sent;
        while (true) {
            System.out.print(marker + " ");
            System.out.print(marker.item + " ");
            /***** TRY DOING System.out.print(marker + " " + marker.item + " "); INSTEAD *****/
            if (marker == sent.previous) {
                break;
            }
            marker = marker.next;
        }
    }

    /** Removes and returns the item at the front of the Deque. 
        If no such item exists, returns null. */
    public Item removeFirst() {
        if (size == 0) {
            return null;
        } else {
            size = size - 1;
            LNode remFirst = sent.next;
            sent.next = sent.next.next;
            sent.next.previous = sent;
            remFirst.previous = remFirst.next = null;
            return remFirst.item;
        }
    }

    /** Removes and returns the item at the back of the Deque. 
        If no such item exists, returns null. */
    public Item removeLast() {
        if (size == 0) {
            return null;
        } else {
            size = size - 1;
            LNode remLast = sent.previous;
            sent.previous = sent.previous.previous;
            sent.previous.next = sent;
            remLast.previous = remLast.next = null;
            return remLast.item;
        } 
    }

    /** Gets the item at the given index, where 0 is the front, 
        1 is the next item, and so forth. If no 
        such item exists, returns null. Must not alter the deque! */
    public Item get(int index) {
        LNode marker = sent.next;
        if (size == 0) {
            return null;
        } else {
            while (index > 0) {
                if (marker.next == sent) {
                    return null;
                }
                marker = marker.next;
                index = index - 1;
            }
            return marker.item;
        }
    } 
}
