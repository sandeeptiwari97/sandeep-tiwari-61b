/**
 * Created by sandeeptiwari on 4/19/17.
 */
public class SearchNode {
    private Node node;
    private int moves = 0;
    private SearchNode lastNode;
    private double mypriority = -1;

    public SearchNode(Node b, SearchNode s) {
        node = b;
        lastNode = s;
    }

    public int compareTo(SearchNode p) {
        if (this.mypriority < p.mypriority) {
            return -1;
        }
        if (this.mypriority > p.mypriority) {
            return 1;
        }
        return 0;

    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public SearchNode getLastNode() {
        return lastNode;
    }

    public void setLastNode(SearchNode lastNode) {
        this.lastNode = lastNode;
    }

    public double getMypriority() {
        return mypriority;
    }

    public void setMypriority(double mypriority) {
        this.mypriority = mypriority;
    }
}
