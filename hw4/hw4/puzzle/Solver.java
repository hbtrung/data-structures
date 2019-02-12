package hw4.puzzle;

import edu.princeton.cs.algs4.Stack;

public class Solver {
    private Stack<WorldState> solution;
    private int moves;

    public Solver(WorldState initial) {
        SearchNode<WorldState> init = new SearchNode<WorldState>(initial, 0, null);
        MinPQ<SearchNode<WorldState>> pq = new MinPQ<SearchNode<WorldState>>();
        pq.insert(init);
        bestfs(pq);
    }

    private void bestfs(MinPQ<SearchNode<WorldState>> pq) {
        while (!pq.isEmpty()) {
            SearchNode x = pq.delMin();

            if (x.estimatedMoves() == 0) {
                solution = new Stack<WorldState>();
                moves = x.moves();
                SearchNode i = null;
                for (i = x; i.prev() != null; i = i.prev()) {
                    solution.push(i.worldState());
                }
                solution.push(i.worldState());
                return;
            }

            for (WorldState w : x.worldState().neighbors()) {
                if (x.prev() != null && w.equals(x.prev().worldState())) continue;
                pq.insert(new SearchNode<WorldState>(w, x.moves() + 1, x));
            }
        }
    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        return solution;
    }
}
