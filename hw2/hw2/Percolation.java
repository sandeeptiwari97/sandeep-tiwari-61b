package hw2;                       

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int side;
    private int first;
    private int last;
    private int opened;
    private int[][] squares;
    private WeightedQuickUnionUF table;
    private WeightedQuickUnionUF t;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("Out of bounds.");
        }
        int dims = N * N;
        side = N;
        first = dims;
        last = dims + 1;
        opened = 0;
        table = new WeightedQuickUnionUF(dims + 2);
        t = new WeightedQuickUnionUF(dims + 2);
        squares = new int[N][N];
    }

    public void open(int row, int col) {
        if (row >= side || col >= side) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen(row, col)) {
            int dP = converter(row, col);

            if (row > 0 && isOpen(row - 1, col)) {
                table.union(converter(row - 1, col), dP);
                t.union(converter(row - 1, col), dP);
            }

            if (row < side - 1 && isOpen(row + 1, col)) {
                table.union(converter(row + 1, col), dP);
                t.union(converter(row + 1, col), dP);

            }

            if (col > 0 && isOpen(row, col - 1)) {
                table.union(converter(row, col - 1), dP);
                t.union(converter(row, col - 1), dP);

            }

            if (col < side - 1 && isOpen(row, col + 1)) {
                table.union(converter(row, col + 1), dP);
                t.union(converter(row, col + 1), dP);
            }

            if (row == 0) {
                table.union(dP, first);
                t.union(dP, first);
            }
            if (row == side - 1 && isFull(row, col)) {
                table.union(dP, last);
                t.union(dP, last);
            } else if (row == side - 1 && !isFull(row, col)) {
                t.union(dP, last);
            }
            opened += 1;
            squares[row][col] = 1;
        }
    }

    public boolean connectedToTop(int spot) {
        return table.connected(spot, first);
    }

//    public void uni(int row, int col) {
//        int dP = converter(row, col);
//
//        //connect to above/below/side elements
//        if (row > 0 && isOpen(row - 1, col)) {
//            table.union(converter(row - 1, col), dP);
//            t.union(converter(row - 1, col), dP);
//        }
//
//        if (row < side - 1 && isOpen(row + 1, col)) {
//            table.union(converter(row + 1, col), dP);
//            t.union(converter(row + 1, col), dP);
//
//        }
//
//        if (col > 0 && isOpen(row, col - 1)) {
//            table.union(converter(row, col - 1), dP);
//            t.union(converter(row, col - 1), dP);
//
//        }
//
//        if (col < side - 1 && isOpen(row, col + 1)) {
//            table.union(converter(row, col + 1), dP);
//            t.union(converter(row, col + 1), dP);
//        }
//
//        //connecting to top/bottom sentinels
//        if (row == 0) {
//            table.union(dP, first);
//            t.union(dP, first);
//        }
//        if (row == side - 1 && isFull(row, col)) {
//            table.union(dP, last);
//            t.union(dP, last);
//        } else if (row == side - 1 && !isFull(row, col)) {
//            t.union(dP, last);
//        }
//    }

    public int converter(int row, int col) {
        return (row * side) + (col % side);
    }

    public boolean isOpen(int row, int col) {
        if (row >= side || col >= side) {
            throw new IndexOutOfBoundsException();
        }
        return squares[row][col] == 1;
    }

    public boolean isFull(int row, int col) {
        if (row >= side || col >= side) {
            throw new IndexOutOfBoundsException();
        }
        int dP = (row * side) + (col % side);
        return connectedToTop(dP);
    }

    public int numberOfOpenSites() {
        return opened;
    }

    public boolean percolates() {
        return t.connected(first, last);
    }
}                       
