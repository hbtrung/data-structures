package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 *  Draws a world that is mostly empty except for a small region.
 */
public class BoringWorldDemo {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // fills in a block 14 tiles wide by 4 tiles tall
//        for (int x = 20; x < 35; x += 1) {
//            for (int y = 5; y < 10; y += 1) {
//                world[x][y] = Tileset.WALL;
//            }
//        }

//        for (int i = 40; i < 50; i++) {
//            for (int j = 0; j < 10; j++) {
//                world[i][j] = Tileset.TREE;
//            }
//        }

        drawTesselation(world, 4);

        // draws the world to the screen
        ter.renderFrame(world);
    }

    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        Position p1 = new Position(p);
        int num = s;
        for (int i = 0; i < s; i++) {
            drawHexLine(world, p1, num, t);
            p1.x -= 1;
            p1.y += 1;
            num += 2;
        }
        p1.x += 1;
        num -= 2;
        for (int i = 0; i < s; i++) {
            drawHexLine(world, p1, num, t);
            p1.x += 1;
            p1.y += 1;
            num -= 2;
        }
    }

    public static void drawHexLine(TETile[][] world, Position p, int num, TETile t) {
        int x = p.x;
        int y = p.y;
        for (int i = 0; i < num; i++) {
            world[x][y] = t;
            x++;
        }
    }

    public static Position addRightHexagon(TETile[][] world, Position p, int s, Random r) {
        Position p1 = new Position(p.x + s * 2 - 1, p.y + s);
        addHexagon(world, p1, s, Tileset.randomTETile(r));
        return p1;
    }

    public static Position addLeftHexagon(TETile[][] world, Position p, int s, Random r) {
        Position p1 = new Position(p.x - s * 2 + 1, p.y + s);
        addHexagon(world, p1, s, Tileset.randomTETile(r));
        return p1;
    }

    public static void duplicateHexagon(TETile[][] world, Position p, int s, Random r, int cnt) {
        for (int i = 0; i < cnt; i++) {
            p.y += s * 2;
            addHexagon(world, p, s, Tileset.randomTETile(r));
        }
    }

    public static void drawTesselation(TETile[][] world, int s) {
        Position pStart = new Position(WIDTH / 2 - (s / 2), 0);
        Random r = new Random();
        addHexagon(world, pStart, s, Tileset.randomTETile(r));
        Position pLeft = addLeftHexagon(world, pStart, s, r);
        Position pLeftLeft = addLeftHexagon(world, pLeft, s, r);
        Position pRight = addRightHexagon(world, pStart, s, r);
        Position pRightRight = addRightHexagon(world, pRight, s, r);

        duplicateHexagon(world, pStart, s, r, 4);
        duplicateHexagon(world, pLeft, s, r, 3);
        duplicateHexagon(world, pRight, s, r, 3);
        duplicateHexagon(world, pLeftLeft, s, r, 2);
        duplicateHexagon(world, pRightRight, s, r, 2);
    }
}
