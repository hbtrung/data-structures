import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    private static Map<Long, Double> distTo;
    private static Map<Long, GraphDB.Edge> edgeTo;
    private static HMMinPQ<Double> pq;

    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        Set<Long> marked = new HashSet<Long>();
        long stId = g.closest(stlon, stlat);
        long destId = g.closest(destlon, destlat);

        distTo = new HashMap<Long, Double>();
        edgeTo = new HashMap<Long, GraphDB.Edge>();
        pq = new HMMinPQ<Double>();

        for (long id : g.vertices()) {
            distTo.put(id, Double.POSITIVE_INFINITY);
        }
        distTo.put(stId, 0.0);
        edgeTo.put(stId, null);
        pq.insert(stId, distTo.get(stId) + g.distance(stId, destId));

        while (!pq.isEmpty()) {
            long v = pq.delMin();
            marked.add(v);

            if (v == destId) {
                Deque<Long> path = new ArrayDeque<Long>();
                for (long x = v; distTo.get(x) != 0; x = edgeTo.get(x).other(x)) {
                    path.addFirst(x);
                }
                path.addFirst(stId);
                return new ArrayList<Long>(path);
            }

            for (GraphDB.Edge e : g.node(v).adj()) {
                long w = e.other(v);
                if (!marked.contains(w)) {
                    if (distTo.get(v) + e.weight() < distTo.get(w)) {
                        distTo.put(w, distTo.get(v) + e.weight());
                        edgeTo.put(w, e);
                        if (!pq.contains(w)) pq.insert(w, distTo.get(w) + g.distance(w, destId));
                        else pq.decreaseKey(w, distTo.get(w) + g.distance(w, destId));
                    }
                }
            }
        }

        return null;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigationDirection objects corresponding to the input
     * route.
     */
    // can use computed distTo and edgeTo of shortest path
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> rd = new ArrayList<NavigationDirection>();
        NavigationDirection nd = new NavigationDirection();
        double distance = 0;
        String curWayName = "";

        nd.direction = NavigationDirection.START;
        long s = route.get(0);
        for (GraphDB.Edge e : g.node(s).adj()) {
            if (e.other(s) == route.get(1)) {
                nd.way = e.way().name() != null ? e.way().name() : NavigationDirection.UNKNOWN_ROAD;
                curWayName = nd.way;
                distance += e.weight();
                break;
            }
        }
//        System.out.println(curWayName);

        for (int i = 1; i < route.size() - 1; i++) {
            long v = route.get(i);
            for (GraphDB.Edge e : g.node(v).adj()) {
                if (e.other(v) == route.get(i + 1)) {
                    String name = e.way().name() != null ? e.way().name() : NavigationDirection.UNKNOWN_ROAD;
                    if (!name.equals(curWayName)) {
//                        System.out.println(curWayName + " " + name);
                        nd.distance = distance;
                        distance = 0;

                        rd.add(nd);

                        nd = new NavigationDirection();
                        nd.way = name;

                        double bearing = g.bearing(route.get(i + 1), v) - g.bearing(v, route.get(i - 1));
                        if (bearing <= -180) bearing += 360;
                        if (bearing >= 180) bearing -= 360;
                        if (-15 <= bearing && bearing <= 15) {
                            nd.direction = NavigationDirection.STRAIGHT;
                        } else if (-30 <= bearing && bearing < -15) {
                            nd.direction = NavigationDirection.SLIGHT_LEFT;
                        } else if (15 < bearing && bearing <= 30) {
                            nd.direction = NavigationDirection.SLIGHT_RIGHT;
                        } else if (30 < bearing && bearing <= 100) {
                            nd.direction = NavigationDirection.RIGHT;
                        } else if (-100 <= bearing && bearing < -30) {
                            nd.direction = NavigationDirection.LEFT;
                        } else if (bearing < -100) {
                            nd.direction = NavigationDirection.SHARP_LEFT;
                        } else if (bearing > 100) {
                            nd.direction = NavigationDirection.SHARP_RIGHT;
                        }

                        curWayName = nd.way;
                    }

                    distance += e.weight();
                    break;
                }
            }
        }

        nd.distance = distance;
        rd.add(nd);
//        for (NavigationDirection x : rd) {
//            System.out.println(x);
//        }
        return rd;
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }

    public static void main(String[] args) {
//        Map<Long, Double> m = new HashMap<>();
//        m.put((long) 11, 1.0);
//        m.put((long) 12, 2.0);
//        m.put(11L, 4.5);
//        for (long k : m.keySet()) {
//            System.out.println(k + " " + m.get(k));
//        }
        GraphDB g = new GraphDB("../library-sp18/data/tiny-clean.osm.xml");
        List<Long> route = new ArrayList<>();
        route.add(66L); route.add(63L); route.add(41L);
        List<NavigationDirection> rd = Router.routeDirections(g, route);
        for (NavigationDirection nd : rd) {
            System.out.println(nd);
        }
    }
}
