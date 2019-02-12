package hw4.puzzle;

public class SearchNode<Key extends WorldState> implements Comparable<SearchNode> {
    private Key ws;
    private int moves;
    private int estimatedMoves;
    private SearchNode prev;

    public SearchNode(Key w, int m, SearchNode p) {
        ws = w;
        moves = m;
        prev = p;
        estimatedMoves = ws.estimatedDistanceToGoal();
    }

    public int compareTo(SearchNode other) {
        return Integer.compare(this.moves + this.estimatedMoves, other.moves + other.estimatedMoves);
    }

    public Key worldState(){
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
