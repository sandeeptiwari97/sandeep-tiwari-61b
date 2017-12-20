package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int size;
    private int trials;
    private Percolation proportions;
    private double[] props;
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Out of bounds.");
        }
        size = N;
        trials = T;
        props = new double[trials];
        for (int i = 0; i < trials; i++) {
            proportions = new Percolation(N);
            int opened = 0;
            while (!proportions.percolates()) {
                int x = StdRandom.uniform(0, size);
                int y = StdRandom.uniform(0, size);
                if (!proportions.isOpen(x, y)) {
                    proportions.open(x, y);
                    opened += 1;
                }
            }
            double prop = (double) opened / (N * N);
            props[i] = prop;
        }
    }

    public double mean() {
//        double total = 0;
//        for (int i = 0; i < trials; i++) {
//            Percolation proportions = new Percolation(size);
//            while (!proportions.percolates()) {
//                proportions.open(StdRandom.uniform(0, size), StdRandom.uniform(0, size));
//            }
//            total += proportions.numberOfOpenSites() / (size * size);
//        }
//        return total / trials;
        return StdStats.mean(props);
    }

    public double stddev() {
//        double total = 0;
//        double mean = mean();
//        for (int j = 0; j < trials; j++) {
//            Percolation proportions = new Percolation(size);
//            while (!proportions.percolates()) {
//                proportions.open(StdRandom.uniform(1, size + 1), StdRandom.uniform(1, size + 1));
//            }
//            double count = (proportions.numberOfOpenSites() / (size * size)) - mean;
//            total += (count * count);
//        }
//        return total / trials - 1;
        return StdStats.stddev(props);
    }

    public double confidenceLow() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(trials));
    }

    public double confidenceHigh() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(trials));
    }
}                       
