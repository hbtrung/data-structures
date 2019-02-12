package lab11.graphs;

import edu.princeton.cs.algs4.Stack;

import java.util.HashSet;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    public Maze maze
    */
    public int[] edgeTo2;   // functions as edgeTo[], used to draw edgeTo[] when has cycle

    public MazeCycles(Maze m) {
        super(m);
        edgeTo2 = new int[m.V()];
    }

    @Override
    public void solve() {
        dfs(0);
    }

    public void dfs(int v) {
        Stack<Integer> st = new Stack<Integer>();
        marked[v] = true;
        edgeTo[v] = v;
        distTo[v] = 0;
        announce();
        st.push(v);
        while (!st.isEmpty()) {
            int x = st.pop();
            for (int w : maze.adj(x)) {
                if (!marked[w]) {
                    edgeTo2[w] = x;
                    distTo[w] = distTo[x] + 1;
                    marked[w] = true;
                    announce();
                    st.push(w);
                } else {
                    // w != edgeTo[x] so we don't have to create the cycle again
                    // for the opposite node as (1)
                    if (w != edgeTo2[x] && w != edgeTo[x]) {
                        HashSet<Integer> hs = new HashSet<Integer>();
                        int i;

                        // check converging node
                        for (i = w; i != v; i = edgeTo2[i]) {
                            hs.add(i);
                        }
                        hs.add(i);
                        for (i = x; !hs.contains(i); i = edgeTo2[i]){}
                        System.out.println(i);
                        hs.clear();

                        // draw the cycle
                        for (int j = x; j != i; j = edgeTo2[j]) {
                            edgeTo[j] = edgeTo2[j];
                            hs.add(j);
                        }
                        edgeTo[w] = x;      // (1)
                        hs.add(w);
                        for (int j = w; j != i; j = edgeTo2[j]) {
                            edgeTo[edgeTo2[j]] = j;
                            hs.add(edgeTo2[j]);
                        }
                        announce();
                        // delete the cycle
//                        for (int j : hs) {
//                            edgeTo[j] = Integer.MAX_VALUE;
//                        }
                    }
                }
            }
        }
    }
}

