import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    private Map<Long, Node> nodes;
    private Set<Way> ways;
    private long W;
    private long V;
    private long E;

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        nodes = new HashMap<Long, Node>();
        ways = new HashSet<>();
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        List<Long> removeIds = new ArrayList<Long>();
        for (Long id : nodes.keySet()) {
            if (!nodes.get(id).isConnected()) {
                removeIds.add(id);
            }
        }
        for (Long id : removeIds) {
            nodes.remove(id);
            V--;
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return nodes.keySet();
    }

    Node node(long id) {
        return nodes.get(id);
    }

    void addWay(Way way) {
        ways.add(way);
        W++;
    }

    void insert(long id, double lat, double lon) {
        if (id < 0) throw new IllegalArgumentException("id must not be negative");
        if (!nodes.containsKey(id)) {
            nodes.put(id, new Node(lat, lon));
            V++;
        }
    }

    void remove(long id) {
        validateNode(id);
        int numVertices = nodes.get(id).numConnections();
        nodes.remove(id);
        V--;
        E -= numVertices;
    }

    void addEdge(long v, long w, Way way) {
        double weight = distance(v, w);
        validateNode(v);
        validateNode(w);
        Edge edge = new Edge(v, w, weight, way);
        nodes.get(v).addEdge(edge);
        nodes.get(w).addEdge(edge);
        E++;
    }

    void addEdge(List<Long> nodeIds, Way way) {
        int n = nodeIds.size();
        for (int i = 0; i < n - 1; i++) {
            addEdge(nodeIds.get(i), nodeIds.get(i + 1), way);
        }
    }

    boolean isEmpty() {
        return V == 0;
    }

    long V() {
        return V;
    }

    long E() {
        return E;
    }

    long W() {
        return W;
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        List<Long> adj = new ArrayList<Long>();
        for (Edge edge : nodes.get(v).adj()) {
            adj.add(edge.other(v));
        }
        return adj;
    }

    void validateNode(long id) {
        if (id < 0) throw new IllegalArgumentException("node id must not be negative");
        if (!nodes.containsKey(id)) throw new NoSuchElementException("node does not exist");
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        long result = 0;
        double minDistance = Double.POSITIVE_INFINITY;
        for (long v : nodes.keySet()) {
            double dist = distance(lon, lat, lon(v), lat(v));
            if (minDistance > dist) {
                minDistance = dist;
                result = v;
            }
        }
        return result;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodes.get(v).lon();
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodes.get(v).lat();
    }

    static class Node {
//        long id;
        private double lat;
        private double lon;
        private String name;
        private List<Edge> adj;

        Node(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
            adj = new ArrayList<Edge>();
        }

        double lat() {
            return lat;
        }

        double lon() {
            return lon;
        }

        String name() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        int numConnections() {
            return adj.size();
        }

        boolean isConnected() {
            return numConnections() != 0;
        }

        void addEdge(Edge w) {
            adj.add(w);
        }

        Iterable<Edge> adj() {
            return adj;
        }
    }

    static class Edge implements Comparable<Edge> {
        private long v;
        private long w;
        private double weight;
        private Way way;

        Edge(long v, long w, double weight, Way way) {
            if (v < 0) throw new IllegalArgumentException("vertex index must be a nonnegative integer");
            if (w < 0) throw new IllegalArgumentException("vertex index must be a nonnegative integer");
            if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
            this.v = v;
            this.w = w;
            this.weight = weight;
            this.way = way;
        }

        double weight() {
            return weight;
        }

        long either() {
            return v;
        }

        long other(long vertex) {
            if      (vertex == v) return w;
            else if (vertex == w) return v;
            else throw new IllegalArgumentException("Illegal endpoint");
        }

        Way way() {
            return way;
        }

        @Override
        public int compareTo(Edge that) {
            return Double.compare(this.weight, that.weight);
        }
    }

    static class Way {
        private long id;
        private String name;
        private double maxSpeed;
        private String unit;
        private String hwType;
        private boolean valid;

        Way(long id) {
            this.id = id;
            valid = false;
        }

        long id() {
            return id;
        }

        String name() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        double maxSpeed() {
            return maxSpeed;
        }

        void setMaxSpeed(double maxSpeed) {
            this.maxSpeed = maxSpeed;
        }

        String hwType() {
            return hwType;
        }

        void setHwType(String hwType) {
            this.hwType = hwType;
        }

        boolean isValid() {
            return valid;
        }

        void setValid(boolean valid) {
            this.valid = valid;
        }

        String unit() {
            return unit;
        }

        void setUnit(String unit) {
            this.unit = unit;
        }
    }

//    public static void main(String[] args) {
//        String DBPathTiny = "../library-sp18/data/tiny-clean.osm.xml";
//        GraphDB g = new GraphDB(DBPathTiny);
//        for (Long x : g.vertices()) {
//            System.out.print(x + " ");
//        }
//        System.out.println();
//
//        GraphDB.Node node1 = g.node(22);
//        node1.setName("con cac");
//        GraphDB.Node node = g.node(22);
//        System.out.println(node.lat() + " " + node.lon() + " " + node.name());
//    }
}
