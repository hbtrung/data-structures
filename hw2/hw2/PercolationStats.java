package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int N;
    private int T;
    private double[] x;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Invalid argument(s) to constructor");
        }
        this.N = N;
        this.T = T;
        x = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation p = pf.make(N);
            while (!p.percolates()) {
                p.open(StdRandom.uniform(N), StdRandom.uniform(N));
            }
            x[i] = (double) p.numberOfOpenSites() / (N * N);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(x);
    }

//    public double mean2() {
//        double sum = 0.0;
//        for (int i = 0; i < T; i++) {
//            sum += x[i];
//        }
//        return sum / T;
//    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(x);
    }

//    public double stddev2() {
//        double mean = mean2();
//        double sum = 0.0;
//        for (int i = 0; i < T; i++) {
//            sum += (x[i] - mean) * (x[i] - mean);
//        }
//        sum /= (T - 1);
//        return Math.sqrt(sum);
//    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - (stddev() * 1.96 / Math.sqrt(T));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + (stddev() * 1.96 / Math.sqrt(T));
    }

//    public static void main(String[] args) {
//        PercolationStats test = new PercolationStats(20, 30, new PercolationFactory());
//        System.out.println("mean1: " + test.mean());
//        System.out.println("mean2: " + test.mean2());
//        System.out.println("stddev1: " + test.stddev());
//        System.out.println("stddev2: " + test.stddev2());
//    }
}
