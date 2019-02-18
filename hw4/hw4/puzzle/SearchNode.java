package hw4.puzzle;

public class SearchNode implements Comparable<SearchNode> {
    private WorldState ws;
    private int moves;
    private int estimatedMoves;
    private SearchNode prev;

    public SearchNode(WorldState w, int m, SearchNode p) {
        ws = w;
        moves = m;
        prev = p;
        estimatedMoves = ws.estimatedDistanceToGoal();
    }

    public int compareTo(SearchNode other) {
        return Integer.compare(this.moves + this.estimatedMoves, other.moves + other.estimatedMoves);
    }

    public WorldState worldState(){
        return ws;
    }

    public int moves() {
        return moves;
    }

    public int estimatedMoves() {
        return estimatedMoves;
    }

    public SearchNode prev() {
        return prev;
    }
}
