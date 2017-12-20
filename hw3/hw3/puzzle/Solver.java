package hw3.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;
import java.util.Stack;
/**
 * Created by sandeeptiwari on 3/21/17.
 */
public class Solver {
    private boolean canSolve = true;
    private MinPQ<SearchNode> priQ;
    //    private MinPQ<SearchNode> priQ2;
    private SearchNode dest = null;
    private Comparator<SearchNode> comp = new CompareNode();

    public Solver(WorldState initial) {
        priQ = new MinPQ<SearchNode>(comp);
        SearchNode sNode = new SearchNode(initial, 0, null);
        priQ.insert(sNode);

        boolean isSolved = false;
        while (!isSolved) {
            isSolved = solveThis();
        }
    }

    private boolean solveThis() {
        SearchNode searchnode = priQ.delMin();
        if (searchnode.board.isGoal()) {
            dest = searchnode;
            return true;
        }
        for (WorldState ws : searchnode.board.neighbors()) {
            SearchNode s = new SearchNode(ws, searchnode.moves + 1, searchnode);
            if (s.equals(searchnode.previousNode)) {
                continue;
            }
            priQ.insert(s);
        }
//        SearchNode tNode = priQ2.delMin();
//        if (tNode.board.isGoal()) {
//            canSolve = false;
//            return true;
//        }
//
//        for (WorldState b : searchnode.board.neighbors()) {
//            SearchNode t = new SearchNode(b, searchnode.moves + 1, searchnode);
//            if (t.equals(tNode.previousNode)) {
//                continue;
//            }
//            priQ2.insert(t);
//        }
////        WorldState board = ((Board) searchnode.board).duplicate();
//        WorldState board = searchnode.board;
//        tNode = new SearchNode(board, 0, null);
//        priQ2.insert(tNode);
        return false;
    }

//    private class Ham implements Comparator<SearchNode> {
//        public int compare(SearchNode s, SearchNode s2) {
//            return s.board.hamming() - s2.board.hamming();
//        }
//    }
//
//    private class Man implements Comparator<SearchNode> {
//        public int compare(SearchNode s, SearchNode s2) {
//            return s.moves + s.board.manhattan() - s2.moves - s2.board.manhattan();
//        }
//    }

    public int moves() {
        if (!canSolve) {
            return -1;
        }
        return dest.moves;
    }

    public Iterable<WorldState> solution() {
        if (dest == null) {
            return null;
        }
        Stack<WorldState> nodes = new Stack<WorldState>();
        SearchNode search = dest;
        nodes.push((WorldState) search.board);
        while (search.previousNode != null) {
            nodes.push((WorldState) search.previousNode.board);
            search = search.previousNode;
        }
        return nodes;
    }

    private class CompareNode implements Comparator<SearchNode> {
        public int compare(SearchNode s, SearchNode s2) {
            int one = s.distanceMo + s.moves;
            int two = s2.distanceMo + s2.moves;
            return one - two;
        }
    }

    public class SearchNode implements Comparable<SearchNode> {
        private WorldState board;
        //        private Board b;
        private int moves;
        private int distanceMo;
        private int h = 1;
        private SearchNode previousNode;
        private int myPriority = -1;

        private SearchNode(WorldState board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previousNode = previous;
//            if (myPriority == -1) {
//                myPriority = ((Board) board).manhattan() + moves;
//            }
            distanceMo = this.board.estimatedDistanceToGoal();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (o.getClass() != this.getClass()) {
                return false;
            }

            SearchNode search = (SearchNode) o;
//            return sear2ch.board.equals(board);
            if (search.board.equals(board)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            if (h != 0) {
                return h;
            }
            h = board.toString().hashCode();
            return h;
        }

        private int priority() {
            if (myPriority == -1) {
                myPriority = moves + ((Board) board).manhattan();
            }
            return myPriority;
        }

        public int compareTo(SearchNode p) {
            if (this.myPriority < p.myPriority) {
                return -1;
            }
            if (this.myPriority > p.myPriority) {
                return 1;
            }
            return 0;
        }
    }
}



