package hw4.puzzle;

import edu.princeton.cs.algs4.StdOut;

public class BoardPuzzleSolver {
    public static void main(String[] args) {
        int[][] tiles = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board b = new Board(tiles);
        Solver solver = new Solver(b);
        System.out.println("Number of moves: " + solver.moves());
        for (WorldState ws : solver.solution()) {
            StdOut.println(ws);
        }
    }
}
