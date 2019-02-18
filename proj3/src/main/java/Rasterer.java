import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private static final int MAX_DEPTH = 7;
    private static final double MAX_LONDPP = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / 256;

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    // min zoom, max zoom, number of images
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlat = params.get("lrlat");
        double width = params.get("w");
        double height = params.get("h");
        double QBLonDPP = (lrlon - ullon) / width;          // query box LonDPP
        double imgLonDPP = MAX_LONDPP / 2;                // max imgLonDPP
        int depth = 1;                                    // min depth = 1

        // calculate depth (zoom) level
        for (int i = 2; imgLonDPP > QBLonDPP && i <= MAX_DEPTH; i++) {
            imgLonDPP /= 2;
            depth++;
        }

        int num = (int) Math.pow(2, depth);
        double lonLength = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / num;    // horizontal length for 1 image
        double latLength = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / num;    // vertical length for 1 image

//        double curLon = MapServer.ROOT_ULLON;
//        double curLat = MapServer.ROOT_ULLAT;

        int[] positions = new int[4];       // x start pos, y start pos, x end pos, y end pos
        positions[0] = (int)((ullon - MapServer.ROOT_ULLON) / lonLength);
        positions[1] = (int)((MapServer.ROOT_ULLAT - ullat) / latLength);
        positions[2] = (int)((lrlon - MapServer.ROOT_ULLON) / lonLength);
        positions[3] = (int)((MapServer.ROOT_ULLAT - lrlat) / latLength);

        for (int i = 0; i < positions.length; i++) {
            if (positions[i] < 0) {
                positions[i] = 0;
            }

            if (positions[i] >= num) {
                positions[i] = num - 1;
            }
        }

        double raster_ullon = MapServer.ROOT_ULLON + positions[0] * lonLength;
        double raster_ullat = MapServer.ROOT_ULLAT - positions[1] * latLength;
        double raster_lrlon = MapServer.ROOT_ULLON + (positions[2] + 1) * lonLength;
        double raster_lrlat = MapServer.ROOT_ULLAT - (positions[3] + 1) * latLength;

        String[][] grid = new String[positions[3] - positions[1] + 1][positions[2] - positions[0] + 1];
        for (int y = positions[1], i = 0; y <= positions[3]; y++, i++) {
            for (int x = positions[0], j = 0; x <= positions[2]; x++, j++) {
                grid[i][j] = "d" + depth + "_x" + x + "_y" + y + ".png";
            }
        }

        results.put("render_grid", grid);
        results.put("raster_ul_lon", raster_ullon);
        results.put("raster_ul_lat", raster_ullat);
        results.put("raster_lr_lon", raster_lrlon);
        results.put("raster_lr_lat", raster_lrlat);
        results.put("depth", depth);
        if (lrlon < MapServer.ROOT_ULLON || ullon > MapServer.ROOT_LRLON || ullat < MapServer.ROOT_LRLAT || lrlat > MapServer.ROOT_ULLAT) {
            results.put("query_success", false);
        } else {
            results.put("query_success", true);
        }

        return results;
    }

//    public static void main(String[] args) {
//
//    }
}
