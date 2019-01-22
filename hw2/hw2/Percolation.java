package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Percolation {
    private WeightedQuickUnionUF connector;
    private int[][] grid;
    private int N;
    private int openNum;    // number of open sites

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("invalid argument to Percolation constructor");
        }
        this.N = N;
        grid = new int[N][N];
        connector = new WeightedQuickUnionUF(N*N + 2);     // last 2 index for top and bottom row
        openNum = 0;
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new IndexOutOfBoundsException("calls open() with invalid row or column");
        }
        if (!isOpen(row, col)) {
            grid[row][col] = 1;
            if (row == 0) {
                connector.union(cIndex(row, col), N*N);
            }
            if (row == N - 1 && connector.connected(cIndex(row, col), N*N)) {
                connector.union(N * N, N*N + 1);
            }
            connect(row, col);
            openNum++;
        }
    }

    private void connect(int row, int col) {
        int[][] adjacent = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};
        for (int i = 0; i < 4; i++) {
            int r = row + adjacent[i][0];
            int c = col + adjacent[i][1];
            if (r < 0 || r >= N || c < 0 || c >= N) {
                continue;
            }
            if (grid[r][c] == 1) {
                connector.union(cIndex(row, col), cIndex(r, c));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new IndexOutOfBoundsException("calls isOpen() with invalid row or column");
        }
        return grid[row][col] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new IndexOutOfBoundsException("calls isFull() with invalid row or column");
        }
        return connector.connected(cIndex(row, col), N*N);
    }

    // return the index of the site in connector disjoint set
    private int cIndex(int row, int col) {
        return row * N + col;
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openNum;
    }

    // does the system percolate?
    public boolean percolates() {
        return connector.connected(N * N, N * N + 1);
    }

    // use for unit testing (not required)
    public static void main(String[] args) {
        try {
            BufferedReader r = new BufferedReader(new FileReader("inputFiles/input4.txt"));
            String line;
            line = r.readLine();
            int N = Integer.parseInt(line);
            Percolation p = new Percolation(N);
            while ((line = r.readLine()) != null && !line.equals("")) {
                String[] num = line.split(" ");
                int a = Integer.parseInt(num[0]);
                int b = Integer.parseInt(num[1]);
                p.open(a, b);
                System.out.println(a + " " + b);
            }
            System.out.println("Percolates? " + p.percolates());
            r.close();
        } catch (IOException e) {
            System.out.println("IO exception");
        }

//        In in = new In("inputFiles/input4.txt");
//        int N = in.readInt();
//        Percolation p = new Percolation(N);
//        while (in.hasNextLine()) {
//            int a = in.readInt();
//            int b = in.readInt();
//            p.open(a, b);
//            System.out.println(a + " " + b);
//        }
//        System.out.println("Percolates? " + p.percolates());
//        in.close();
    }
}
