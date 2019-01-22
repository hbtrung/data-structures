package byog.lab5;

public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position p) {
        x = p.x;
        y = p.y;
    }

}
