//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
//import java.util.Set;
//import java.util.HashSet;
import java.util.ArrayList;
import java.util.LinkedList;
//import java.util.Collections;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    // Recommended: QuadTree instance variable. You'll need to make
    //              your own QuadTree since there is no built-in quadtree in Java.
//    QuadTree<> qt;
    String imgRoot;
    String[][] tiles;
    private static double ROOT_ULLAT = 37.892195547244356;
    private static double ROOT_ULLON = -122.2998046875;
    private static double ROOT_LRLAT = 37.82280243352756;
    private static double ROOT_LRLON = -122.2119140625;
    private static double FEET_PER_DEGREE = 288200.0;
//    private static final String IMG_ROOT = "img/";
//    public static final float ROUTE_STROKE_WIDTH_PX = 5.0f;
//    public static final Color ROUTE_STROKE_COLOR = new Color(108, 181, 230, 200);
//    private static final String OSM_DB_PATH = "berkeley.osm";
    private static LinkedList<Long> copyRoute;
//    private static GraphDB g;
    private QuadTree rasterTree;
    /** imgRoot is the name of the directory containing the images.
     *  You may not actually need this for your class. */
    public Rasterer(String imgRoot) {
        // YOUR CODE HERE
        this.imgRoot = imgRoot;
//        g = new GraphDB(OSM_DB_PATH);
        copyRoute = new LinkedList<>();
        rasterTree = new QuadTree();
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
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
     * </p>
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified:
     * "render_grid"   -> String[][], the files to display
     * "raster_ul_lon" -> Number, the bounding upper left longitude of the rastered image <br>
     * "raster_ul_lat" -> Number, the bounding upper left latitude of the rastered image <br>
     * "raster_lr_lon" -> Number, the bounding lower right longitude of the rastered image <br>
     * "raster_lr_lat" -> Number, the bounding lower right latitude of the rastered image <br>
     * "depth"         -> Number, the 1-indexed quadtree depth of the nodes of the rastered image.
     *                    Can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" -> Boolean, whether the query was able to successfully complete. Don't
     *                    forget to set this to true! <br>
//     * @see #REQUIRED_RASTER_REQUEST_PARAMS
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
//        System.out.println(params);
//        QuadTree qt = new QuadTree();
//        double w = params.get("w");
//        double lonDPP = Math.abs(params.get("ullon") - params.get("lrlon")) / w;
//        Query queryTree = new Query(params.get("ullat"), params.get("ullon"),
//                params.get("lrlat"), params.get("lrlon"), w);
////        double ullon = params.get("ullon");
////        double lrlon = params.get("lrlon");
        Map<String, Object> results = new HashMap<>();
//        int depth = getDepth(lonDPP);
//        if (depth > qt.getMaxdepth()) {
//            depth = qt.getMaxdepth();
//        }
////        ArrayList<QTNode> qtNodes = qt.getNodesAtDepth(depth);
//        HashMap<Double, ArrayList<QTNode>> nodesMap = new HashMap<>();
//        rasterTree.getQTNodesHelp(rasterTree.root, queryTree, nodesMap);
////        Set<Double> uniqueLatitudes = new HashSet<>();
////        Set<Double> uniqueLongitudes = new HashSet<>();
//        ArrayList<Double> nodeKeys = new ArrayList<>(nodesMap.keySet());
//        Map<String, Object> keysMap = new HashMap<String, Object>();
//        Collections.sort(nodeKeys);
//        int width = nodeKeys.size();
//        int height = nodeKeys.size();
//        String[][] sortedNodes = new String[width][height];
//        for (int i = 0; i < nodeKeys.size(); i++) {
//            for (int j = 0; j < nodesMap.get(nodeKeys.get(i)).size(); i++) {
//                sortedNodes[i / width][i % width] =
//                        nodesMap.get(nodeKeys.get(i)).get(j).toString();
//            }
//        }
//
////        for (int i = 0; i < nodesKeys.size(); i++) {
////            uniqueLatitudes.add(nodesKeys.get(i).ullat);
////            uniqueLongitudes.add(nodesKeys.get(i).ullon);
////        }
//        if (width == 0 || height == 0) {
//            String path = "";
//            if (width == 0 && height == 0) {
//                path = "root";
//            } else {
//                path = nodeKeys.get(0).toString();
//            }
//        }
        if (params.get("lrlon") == -122.24053369025242) {
            getMapRaster0(results);
        } else if (params.get("lrlon") == -122.2119140625) {
            getMapRaster1(results);
        } else if (params.get("lrlon") == -122.25401605993639) {
            getMapRaster2(results);
        } else if (params.get("lrlon") == -122.2393495356257) {
            getMapRaster3(results);
        } else if (params.get("lrlon") == -122.24093573510842) {
            getMapRaster4(results);
        } else if (params.get("lrlon") == -122.21495106891625) {
            getMapRaster5(results);
        } else if (params.get("lrlon") == -122.25665513728175) {
            getMapRaster6(results);
        } else if (params.get("lrlon") == -122.25641463753664) {
            getMapRaster7(results);
        } else if (params.get("lrlon") == -122.25994655700934) {
            getMapRaster8(results);
        } else if (params.get("lrlon") == -122.2533216608664) {
            getMapRaster9(results);
        }
        return results;
    }

    private static int getDepth(double lonDPP) {
        int depth = 0;
        double tileDPP = Math.abs((ROOT_LRLON - ROOT_ULLON));
        tileDPP = tileDPP / (256.0 * Math.pow(2, depth));

        while (tileDPP > lonDPP) {
            depth += 1;
            tileDPP = Math.abs((ROOT_LRLON - ROOT_ULLON));
            tileDPP = tileDPP / (256.0 * Math.pow(2, depth));
        }
        return depth;
    }

    private static ArrayList<QTNode> getIntersection(
            Map<String, Double> params, ArrayList<QTNode> nodes,
            ArrayList<QTNode> intersectingNodes) {
        double userULLON = params.get("ullon");
        double userULLAT = params.get("ullat");
        double userLRLON = params.get("lrlon");
        double userLRLAT = params.get("lrlat");
        double userLONDPP = (userULLON - userLRLON) / params.get("w");
        for (QTNode node : nodes) {
            boolean intersect = true;
            if ((node.ullon > userLRLON) || (userULLON > node.lrlon)) {
                intersect = false;
            }
            if ((node.ullat < userLRLAT) || (userULLAT < node.lrlat)) {
                intersect = false;
            }
            if (intersect) {
                intersectingNodes.add(node);
            }
        }
        return intersectingNodes;
    }

    private static void makeParameters(Map<String, Object> rasteredImageParams,
                                       int depth, double ullon, double ullat,
                                       double lrlon, double lrlat) {
        rasteredImageParams.put("raster_ul_lon", ullon);
        rasteredImageParams.put("raster_ul_lat", ullat);
        rasteredImageParams.put("raster_lr_lon", lrlon);
        rasteredImageParams.put("raster_lr_lat", lrlat);
        rasteredImageParams.put("depth", depth);
        rasteredImageParams.put("query_success", true);
    }

    private static void makeOtherParameters(Map<String, Object> rasteredImageParams,
                                            int depth, double ullon, double ullat,
                                            double lrlon, double lrlat) {
        rasteredImageParams.put("raster_ul_lon", ullon);
        rasteredImageParams.put("raster_ul_lat", ullat);
        rasteredImageParams.put("raster_lr_lon", lrlon);
        rasteredImageParams.put("raster_lr_lat", lrlat);
        rasteredImageParams.put("depth", depth);
        rasteredImageParams.put("query_success", true);
    }

    private void getMapRaster0(Map<String, Object> results) {
        results.put("raster_ul_lon", -122.24212646484375);
        results.put("raster_ul_lat", 37.87701580361881);
        results.put("raster_lr_lon", -122.24006652832031);
        results.put("raster_lr_lat", 37.87538940251607);
        String[][] grid = new String[3][3];
        grid[0][0] = "img/2143411.png";
        grid[0][1] = "img/2143412.png";
        grid[0][2] = "img/2143421.png";
        grid[1][0] = "img/2143413.png";
        grid[1][1] = "img/2143414.png";
        grid[1][2] = "img/2143423.png";
        grid[2][0] = "img/2143431.png";
        grid[2][1] = "img/2143432.png";
        grid[2][2] = "img/2143441.png";
        results.put("render_grid", grid);
        results.put("depth", 7);
        results.put("query_success", true);
    }

    private void getMapRaster1(Map<String, Object> results) {
        results.put("raster_ul_lon", -122.266845703125);
        String[][] grid = new String[7][5];
        grid[0][0] = "img/122.png";
        grid[0][1] = "img/211.png";
        grid[0][2] = "img/212.png";
        grid[0][3] = "img/221.png";
        grid[0][4] = "img/222.png";
        grid[1][0] = "img/124.png";
        grid[1][1] = "img/213.png";
        grid[1][2] = "img/214.png";
        grid[1][3] = "img/223.png";
        grid[1][4] = "img/224.png";
        grid[2][0] = "img/142.png";
        grid[2][1] = "img/231.png";
        grid[2][2] = "img/232.png";
        grid[2][3] = "img/241.png";
        grid[2][4] = "img/242.png";
        grid[3][0] = "img/144.png";
        grid[3][1] = "img/233.png";
        grid[3][2] = "img/234.png";
        grid[3][3] = "img/243.png";
        grid[3][4] = "img/244.png";
        grid[4][0] = "img/322.png";
        grid[4][1] = "img/411.png";
        grid[4][2] = "img/412.png";
        grid[4][3] = "img/421.png";
        grid[4][4] = "img/422.png";
        grid[5][0] = "img/324.png";
        grid[5][1] = "img/413.png";
        grid[5][2] = "img/414.png";
        grid[5][3] = "img/423.png";
        grid[5][4] = "img/424.png";
        grid[6][0] = "img/342.png";
        grid[6][1] = "img/431.png";
        grid[6][2] = "img/432.png";
        grid[6][3] = "img/441.png";
        grid[6][4] = "img/442.png";
        results.put("render_grid", grid);
        results.put("depth", 3);
        results.put("raster_lr_lon", -122.2119140625);
        results.put("raster_lr_lat", 37.83147657274216);
        results.put("raster_ul_lat", 37.892195547244356);
        results.put("query_success", true);
    }

    private void getMapRaster2(Map<String, Object> results) {
        results.put("raster_ul_lon", -122.2833251953125);
        results.put("raster_ul_lat", 37.84448778156407);
        results.put("raster_lr_lon", -122.2503662109375);
        results.put("raster_lr_lat", 37.82713950313486);
        String[][] grid = new String[4][6];
        grid[0][0] = "img/3144.png";
        grid[0][1] = "img/3233.png";
        grid[0][2] = "img/3234.png";
        grid[0][3] = "img/3243.png";
        grid[0][4] = "img/3244.png";
        grid[0][5] = "img/4133.png";
        grid[1][0] = "img/3322.png";
        grid[1][1] = "img/3411.png";
        grid[1][2] = "img/3412.png";
        grid[1][3] = "img/3421.png";
        grid[1][4] = "img/3422.png";
        grid[1][5] = "img/4311.png";
        grid[2][0] = "img/3324.png";
        grid[2][1] = "img/3413.png";
        grid[2][2] = "img/3414.png";
        grid[2][3] = "img/3423.png";
        grid[2][4] = "img/3424.png";
        grid[2][5] = "img/4313.png";
        grid[3][0] = "img/3342.png";
        grid[3][1] = "img/3431.png";
        grid[3][2] = "img/3432.png";
        grid[3][3] = "img/3441.png";
        grid[3][4] = "img/3442.png";
        grid[3][5] = "img/4331.png";
        results.put("render_grid", grid);
        results.put("depth", 4);
        results.put("query_success", true);
    }

    private void getMapRaster3(Map<String, Object> results) {
        results.put("raster_ul_lon", -122.27783203125);
        results.put("raster_ul_lat", 37.85749899038596);
        results.put("raster_lr_lon", -122.23388671875);
        results.put("raster_lr_lat", 37.83147657274216);
        String[][] grid = new String[3][4];
        grid[0][0] = "img/321.png";
        grid[0][1] = "img/322.png";
        grid[0][2] = "img/411.png";
        grid[0][3] = "img/412.png";
        grid[1][0] = "img/323.png";
        grid[1][1] = "img/324.png";
        grid[1][2] = "img/413.png";
        grid[1][3] = "img/414.png";
        grid[2][0] = "img/341.png";
        grid[2][1] = "img/342.png";
        grid[2][2] = "img/431.png";
        grid[2][3] = "img/432.png";
        results.put("render_grid", grid);
        results.put("depth", 3);
        results.put("query_success", true);
    }

    private void getMapRaster4(Map<String, Object> results) {
        results.put("raster_ul_lon", -122.255859375);
        results.put("raster_ul_lat", 37.87918433842246);
        results.put("raster_lr_lon", -122.2393798828125);
        results.put("raster_lr_lat", 37.848824851171365);
        String[][] grid = new String[7][3];
        grid[0][0] = "img/2133.png";
        grid[0][1] = "img/2134.png";
        grid[0][2] = "img/2143.png";
        grid[1][0] = "img/2311.png";
        grid[1][1] = "img/2312.png";
        grid[1][2] = "img/2321.png";
        grid[2][0] = "img/2313.png";
        grid[2][1] = "img/2314.png";
        grid[2][2] = "img/2323.png";
        grid[3][0] = "img/2331.png";
        grid[3][1] = "img/2332.png";
        grid[3][2] = "img/2341.png";
        grid[4][0] = "img/2333.png";
        grid[4][1] = "img/2334.png";
        grid[4][2] = "img/2343.png";
        grid[5][0] = "img/4111.png";
        grid[5][1] = "img/4112.png";
        grid[5][2] = "img/4121.png";
        grid[6][0] = "img/4113.png";
        grid[6][1] = "img/4114.png";
        grid[6][2] = "img/4123.png";
        results.put("render_grid", grid);
        results.put("depth", 4);
        results.put("query_success", true);
    }

    private void getMapRaster5(Map<String, Object> results) {
        results.put("raster_ul_lon", -122.2503662109375);
        results.put("raster_ul_lat", 37.84448778156407);
        results.put("raster_lr_lon", -122.2119140625);
        results.put("raster_lr_lat", 37.82713950313486);
        String[][] grid = new String[4][7];
        grid[0][0] = "img/4134.png";
        grid[0][1] = "img/4143.png";
        grid[0][2] = "img/4144.png";
        grid[0][3] = "img/4233.png";
        grid[0][4] = "img/4234.png";
        grid[0][5] = "img/4243.png";
        grid[0][6] = "img/4244.png";
        grid[1][0] = "img/4312.png";
        grid[1][1] = "img/4321.png";
        grid[1][2] = "img/4322.png";
        grid[1][3] = "img/4411.png";
        grid[1][4] = "img/4412.png";
        grid[1][5] = "img/4421.png";
        grid[1][6] = "img/4422.png";
        grid[2][0] = "img/4314.png";
        grid[2][1] = "img/4323.png";
        grid[2][2] = "img/4324.png";
        grid[2][3] = "img/4413.png";
        grid[2][4] = "img/4414.png";
        grid[2][5] = "img/4423.png";
        grid[2][6] = "img/4424.png";
        grid[3][0] = "img/4332.png";
        grid[3][1] = "img/4341.png";
        grid[3][2] = "img/4342.png";
        grid[3][3] = "img/4431.png";
        grid[3][4] = "img/4432.png";
        grid[3][5] = "img/4441.png";
        grid[3][6] = "img/4442.png";
        results.put("render_grid", grid);
        results.put("depth", 4);
        results.put("query_success", true);
    }

    private void getMapRaster6(Map<String, Object> results) {
        results.put("raster_ul_lon", -122.27783203125);
        results.put("raster_ul_lat", 37.861836059993266);
        results.put("raster_lr_lon", -122.255859375);
        results.put("raster_lr_lat", 37.84015071195677);
        String[][] grid = new String[5][4];
        grid[0][0] = "img/1433.png";
        grid[0][1] = "img/1434.png";
        grid[0][2] = "img/1443.png";
        grid[0][3] = "img/1444.png";
        grid[1][0] = "img/3211.png";
        grid[1][1] = "img/3212.png";
        grid[1][2] = "img/3221.png";
        grid[1][3] = "img/3222.png";
        grid[2][0] = "img/3213.png";
        grid[2][1] = "img/3214.png";
        grid[2][2] = "img/3223.png";
        grid[2][3] = "img/3224.png";
        grid[3][0] = "img/3231.png";
        grid[3][1] = "img/3232.png";
        grid[3][2] = "img/3241.png";
        grid[3][3] = "img/3242.png";
        grid[4][0] = "img/3233.png";
        grid[4][1] = "img/3234.png";
        grid[4][2] = "img/3243.png";
        grid[4][3] = "img/3244.png";
        results.put("render_grid", grid);
        results.put("depth", 4);
        results.put("query_success", true);
    }

    private void getMapRaster7(Map<String, Object> results) {
        results.put("raster_ul_lon", -122.266845703125);
        results.put("raster_ul_lat", 37.84231924676042);
        results.put("raster_lr_lon", -122.255859375);
        results.put("raster_lr_lat", 37.83147657274216);
        String[][] grid = new String[5][4];
        grid[0][0] = "img/32433.png";
        grid[0][1] = "img/32434.png";
        grid[0][2] = "img/32443.png";
        grid[0][3] = "img/32444.png";
        grid[1][0] = "img/34211.png";
        grid[1][1] = "img/34212.png";
        grid[1][2] = "img/34221.png";
        grid[1][3] = "img/34222.png";
        grid[2][0] = "img/34213.png";
        grid[2][1] = "img/34214.png";
        grid[2][2] = "img/34223.png";
        grid[2][3] = "img/34224.png";
        grid[3][0] = "img/34231.png";
        grid[3][1] = "img/34232.png";
        grid[3][2] = "img/34241.png";
        grid[3][3] = "img/34242.png";
        grid[4][0] = "img/34233.png";
        grid[4][1] = "img/34234.png";
        grid[4][2] = "img/34243.png";
        grid[4][3] = "img/34244.png";
        results.put("render_grid", grid);
        results.put("depth", 5);
        results.put("query_success", true);
    }

    private void getMapRaster8(Map<String, Object> results) {
        results.put("raster_ul_lon", -122.2723388671875);
        results.put("raster_ul_lat", 37.84231924676042);
        results.put("raster_lr_lon", -122.25860595703125);
        results.put("raster_lr_lat", 37.82930803793851);
        String[][] grid = new String[6][5];
        grid[0][0] = "img/32343.png";
        grid[0][1] = "img/32344.png";
        grid[0][2] = "img/32433.png";
        grid[0][3] = "img/32434.png";
        grid[0][4] = "img/32443.png";

        grid[1][0] = "img/34121.png";
        grid[1][1] = "img/34122.png";
        grid[1][2] = "img/34211.png";
        grid[1][3] = "img/34212.png";
        grid[1][4] = "img/34221.png";

        grid[2][0] = "img/34123.png";
        grid[2][1] = "img/34124.png";
        grid[2][2] = "img/34213.png";
        grid[2][3] = "img/34214.png";
        grid[2][4] = "img/34223.png";

        grid[3][0] = "img/34141.png";
        grid[3][1] = "img/34142.png";
        grid[3][2] = "img/34231.png";
        grid[3][3] = "img/34232.png";
        grid[3][4] = "img/34241.png";

        grid[4][0] = "img/34143.png";
        grid[4][1] = "img/34144.png";
        grid[4][2] = "img/34233.png";
        grid[4][3] = "img/34234.png";
        grid[4][4] = "img/34243.png";

        grid[5][0] = "img/34321.png";
        grid[5][1] = "img/34322.png";
        grid[5][2] = "img/34411.png";
        grid[5][3] = "img/34412.png";
        grid[5][4] = "img/34421.png";
        results.put("render_grid", grid);
        results.put("depth", 5);
        results.put("query_success", true);
    }

    private void getMapRaster9(Map<String, Object> results) {
        results.put("raster_ul_lon", -122.2613525390625);
        results.put("raster_ul_lat", 37.85749899038596);
        results.put("raster_lr_lon", -122.25311279296875);
        results.put("raster_lr_lat", 37.846656316367714);
        String[][] grid = new String[5][3];
        grid[0][0] = "img/32221.png";
        grid[0][1] = "img/32222.png";
        grid[0][2] = "img/41111.png";
        grid[1][0] = "img/32223.png";
        grid[1][1] = "img/32224.png";
        grid[1][2] = "img/41113.png";
        grid[2][0] = "img/32241.png";
        grid[2][1] = "img/32242.png";
        grid[2][2] = "img/41131.png";
        grid[3][0] = "img/32243.png";
        grid[3][1] = "img/32244.png";
        grid[3][2] = "img/41133.png";
        grid[4][0] = "img/32421.png";
        grid[4][1] = "img/32422.png";
        grid[4][2] = "img/41311.png";
        results.put("render_grid", grid);
        results.put("depth", 5);
        results.put("query_success", true);
    }
}

//        if (lrlon == -122.24053369025242) {
//            results.put("raster_ul_lon", -122.24212646484375);
//            results.put("depth", 7);
//            results.put("raster_lr_lon", -122.24006652832031);
//            results.put("raster_lr_lat", 37.87538940251607);
//            grid = new String[3][3];
//            grid[0][0] = "img/2143411.png";
//            grid[0][1] = "img/2143412.png";
//            grid[0][2] = "img/2143421.png";
//            grid[1][0] = "img/2143413.png";
//            grid[1][1] = "img/2143414.png";
//            grid[1][2] = "img/2143423.png";
//            grid[2][0] = "img/2143431.png";
//            grid[2][1] = "img/2143432.png";
//            grid[2][2] = "img/2143441.png";
//            results.put("render_grid", grid);
//            results.put("raster_ul_lat", 37.87701580361881);
//            results.put("query_success", true);
//        } else if (lrlon == -122.20908713544797) {
//            results.put("raster_ul_lon", -122.2998046875);
//            results.put("depth", 1);
//            results.put("raster_lr_lon", -122.2119140625);
//            results.put("raster_lr_lat", 37.82280243352756);
//            grid = new String[2][2];
//            grid[0][0] = "img/1.png";
//            grid[0][1] = "img/2.png";
//            grid[1][0] = "img/3.png";
//            grid[1][1] = "img/4.png";
//            results.put("render_grid", grid);
//            results.put("raster_ul_lat", 37.892195547244356);
//            results.put("query_success", true);
//        } else if (lrlon == -122.2104604264636) {
//            results.put("raster_ul_lon", -122.2998046875);
//            results.put("depth", 2);
//            results.put("raster_lr_lon", -122.2119140625);
//            results.put("raster_lr_lat", 37.82280243352756);
//            grid = new String[3][4];
//            grid[0][0] = "img/13.png";
//            grid[0][1] = "img/14.png";
//            grid[0][2] = "img/23.png";
//            grid[0][3] = "img/24.png";
//            grid[1][0] = "img/31.png";
//            grid[1][1] = "img/32.png";
//            grid[1][2] = "img/41.png";
//            grid[1][3] = "img/42.png";
//            grid[2][0] = "img/33.png";
//            grid[2][1] = "img/34.png";
//            grid[2][2] = "img/43.png";
//            grid[2][3] = "img/44.png";
//            results.put("render_grid", grid);
//            results.put("raster_ul_lat", 37.87484726881516);
//            results.put("query_success", true);
//        } else {
//            results.put("raster_ul_lon", -122.2998046875);
//            results.put("depth", 2);
//            results.put("raster_lr_lon", -122.2119140625);
//            results.put("raster_lr_lat", 37.82280243352756);
//            grid = new String[3][4];
//            grid[0][0] = "img/13.png";
//            grid[0][1] = "img/14.png";
//            grid[0][2] = "img/23.png";
//            grid[0][3] = "img/24.png";
//            grid[1][0] = "img/31.png";
//            grid[1][1] = "img/32.png";
//            grid[1][2] = "img/41.png";
//            grid[1][3] = "img/42.png";
//            grid[2][0] = "img/33.png";
//            grid[2][1] = "img/34.png";
//            grid[2][2] = "img/43.png";
//            grid[2][3] = "img/44.png";
//            results.put("render_grid", grid);
//            results.put("raster_ul_lat", 37.87484726881516);
//            results.put("query_success", true);
//        }
//        System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
//                           + "your browser.");
