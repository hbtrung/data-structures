package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

import java.util.HashSet;
import java.util.Set;

public class Board implements WorldState{
    private static final int BLANK = 0;

    private int[][] board;
    private int n;
    private int estimate;

    public Board(int[][] tiles) {
        n = tiles.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = tiles[i][j];
            }
        }
        estimate = manhattan();
    }

    public int tileAt(int i, int j){
        if (!inBounds(i, j))
            throw new IndexOutOfBoundsException("indexes are not inbound");
        return board[i][j];
    }

    public int size(){
        return n;
    }

    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> nbs = new Queue<WorldState>();
        int bRow = -1;
        int bCol = -1;
        int[][] tmpBoard = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tmpBoard[i][j] = board[i][j];
                if(board[i][j] == BLANK) {
                    bRow = i;
                    bCol = j;
                }
            }
        }
        int[][] nbTiles = {{bRow + 1, bCol}, {bRow - 1, bCol}, {bRow, bCol + 1}, {bRow, bCol - 1}};
        for(int[] tile : nbTiles) {
            if (inBounds(tile[0], tile[1])) {
                exch(tmpBoard, tile[0], tile[1], bRow, bCol);
                nbs.enqueue(new Board(tmpBoard));
                exch(tmpBoard, tile[0], tile[1], bRow, bCol);
            }
        }
        return nbs;
    }

    private void exch(int[][] tmpBoard, int i1, int j1, int i2, int j2) {
        int x = tmpBoard[i1][j1];
        tmpBoard[i1][j1] = tmpBoard[i2][j2];
        tmpBoard[i2][j2] = x;
    }

    public int hamming() {
        int wrong = 0;
        int size = size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tileAt(i, j) == 0) continue;
                if (i * size + j + 1 != tileAt(i, j)) {
                    wrong++;
                }
            }
        }
        return wrong;
    }

    public int manhattan() {
        int wrong = 0;
        int size = size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tileAt(i, j) == 0) continue;
                if (i * size + j + 1 != tileAt(i, j)) {
                    int r = (tileAt(i, j) - 1) / size;
                    int c = (tileAt(i, j) - 1) % size;
                    wrong += Math.abs(r - i) + Math.abs(c - j);
                }
            }
        }
        return wrong;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return estimate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Board tmp = (Board) o;
        if (size() != tmp.size()) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tileAt(i, j) != tmp.tileAt(i, j)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                hash *= 16;
                hash += tileAt(i, j);
            }
        }
        return hash;
    }

    private boolean inBounds(int i, int j) {
        return i >= 0 && i < n && j >= 0 && j < n;
    }

    @Override
    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

//    public static void main(String[] args) {
//        int[][] tiles = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
//        Board b = new Board(tiles);
//        System.out.println(b.hamming());
//        System.out.println(b.manhattan());
//        System.out.println(b.hashCode());
//        System.out.println(b.hashCode());
//        System.out.println(b.hashCode());
//    }
}
