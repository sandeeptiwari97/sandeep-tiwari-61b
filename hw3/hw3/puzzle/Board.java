package hw3.puzzle;

import edu.princeton.cs.algs4.StdRandom;
import static java.lang.Math.abs;
import java.util.Arrays;
import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {
    private int size;
    private int[] tiles;
    private int[][] board;
    private int[][] goal;
    private int zRow;
    private int zCol;
    private int hammy = 0;
    private int manny = 0;
//    private int h = 0;

    /** Returns the string representation of the board.
     * Uncomment this method. */

    public Board(int[][] tiles) {
        board = copy2D(tiles);
        size = tiles.length;
        goal = new int[size][size];
        int num = 1;
        int dims = size * size;
        this.tiles = new int[dims];
        for (int i = 0; i < tiles.length; i++) {
            int[] row = tiles[i];
            for (int j = 0; j < row.length; j++) {
                this.tiles[(size * i) + j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    zRow = 0;
                    zCol = 0;
                }
            }
        }
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                goal[r][c] = num % dims;
                num += 1;
            }
        }
    }

    private int[][] copy2D(int[][] original) {
        if (original == null) {
            return null;
        }
        final int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

    public int tileAt(int i, int j) {
        if (i >= size || j >= size) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int value = board[i][j];
        return value;
    }

    public int size() {
        return size;
    }

    /* Got this implementations of neighbors() from the provided solution
     * http://joshh.ug/neighbors.html
     * */

    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == 0) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = 0;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = 0;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        if (hammy != 0) {
            return hammy;
        }
        hammy = 0;
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] == 0) {
                continue;
            }
            if (tiles[i] != (i + 1)) {
                hammy += 1;
            }
        }
//        for (int r = 0; r < size; r++) {
//            for (int c = 0; c < size; c++) {
//                if (board[r][c] != goal[r][c]) {
//                    hammy += 1;
//                }
//            }
//        }
        return hammy;
//        int wrong = 0;
//        for (int i = 0; i < size(); i++) {
//            for (int j = 0; j < size(); j++) {
//                if (board[i][j] != goal[i][j]) {
//                    wrong++;
//                }
//            }
//        }
//        wrong = wrong - 1;
//        return wrong;
    }

    public int manhattan() {
        if (manny != 0) {
            return manny;
        }
        manny = 0;
        for (int j = 0; j < tiles.length; j++) {
            if (tiles[j] == 0) {
                continue;
            }
            int r = abs((j % size) - ((tiles[j] - 1) % size));
            int c = abs((j / size) - ((tiles[j] - 1) / size));
            manny += (r + c);
        }
        return manny;
//        int spacesOff = 0;
//        for (int i = 0; i < size(); i++) {
//            for (int j = 0; j < size(); j++) {
//                int num = board[i][j];
//                if (num != goal[i][j] && num != 0) {
//                    int[] goalRC = toRC(num);
//                    spacesOff += abs(i - goalRC[0]) + abs(j - goalRC[1]);
//                }
//            }
//        }
//        return spacesOff;
    }

    public int[] toRC(int num) {
        int[] rc = new int[2];
        rc[0] = (num - 1) / size();
        rc[1] = (num - 1) % size();
        return rc;
    }

    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }
        if (y == this) {
            return true;
        }

        Board x = (Board) y;
        if (x.size != this.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.tileAt(i, j) != x.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return Arrays.equals(tiles, x.tiles);
    }

    public int hashCode() {
//        if (h != 0) {
//            return h;
//        }
//        h = board.toString().hashCode();
//        return h;
        return 0;
    }

    /** Returns the string representation of the board.
     * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    public WorldState duplicate() {
        boolean good = false;
        int[][] tilesNShit = new int[size][size];

        for (int a = 0; a < size; a++) {
            for (int b = 0; b < size; b++) {
                int loc = (a * size) + b;
                tilesNShit[a][b] = tiles[loc];
            }
        }

        while (!good) {
            int r = StdRandom.uniform(size);
            int c = StdRandom.uniform(size - 1);
            if (size == 2) {
                c = 1;
            }

            if (c < 1) {
                continue;
            }
            if (tilesNShit[r][c] == 0) {
                continue;
            }

            if (tilesNShit[r][c - 1] == 0) {
                continue;
            }

            int tile = tilesNShit[r][c];
            tilesNShit[r][c] = tilesNShit[r][c - 1];
            tilesNShit[r][c] = tile;
            good = true;
        }
        return new Board(tilesNShit);
    }
}
