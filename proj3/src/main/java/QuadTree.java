/**
 * Created by sandeeptiwari on 4/17/17.
 */
import java.util.ArrayList;
import java.io.File;
import java.util.HashMap;

public class QuadTree {
    QTNode root;
    int maximumDepth;

    public QuadTree() {
        this.root = new QTNode("root.png", 37.892195547244356, -122.2998046875, 37.82280243352756, -122.2119140625, 1);
        maximumDepth = 7;
//        setLocations(root);
    }

//    private void createTree() {
//
//        File folder = new File("img/");
//        File[] listOfFiles = folder.listFiles();
//
//        for (File file: listOfFiles) {
//            if (file.isFile()) {
//                int newlength = file.getName().length() - 4;
//                String readableFileName = file.getName().substring(0, newlength);
//                this.insert(new QTNode(readableFileName, null, null, null, null));
//                if ((file.getName().length() - 4) > maximumDepth) {
//                    maximumDepth = (file.getName().length() - 4);
//                }
//            }
//        }
//
////        setLocations(root);
//    }

//    private void setLocations(QTNode qtn) {
//        if (qtn == null) {
//            return;
//        }
//
//        double midLongitude = (qtn.lrlon + qtn.getUllon()) / 2;
//        double midLatitude = (qtn.getLrlat() + qtn.getUllat()) / 2;
//
//        QTNode ul = qtn.upperLeft;
//        QTNode ur = qtn.upperRight;
//        QTNode ll = qtn.lowerleft;
//        QTNode lr = qtn.lowerRight;
//
//        if (ul == null) {
//            return;
//        }
//
//        ul.setUllat(qtn.getUllat());
//        ul.setUllon(qtn.getUllon());
//        ul.setLrlat(midLatitude);
//        ul.setLrlon(midLongitude);
//
//        ur.setUllon(midLongitude);
//        ur.setUllat(qtn.getUllat());
//        ur.setLrlat(midLatitude);
//        ur.setLrlon(qtn.lrlon);
//
//        ll.setUllon(qtn.getUllon());
//        ll.setUllat(midLatitude);
//        ll.setLrlat(qtn.getLrlat());
//        ll.setLrlon(midLongitude);
//
//        lr.setLrlat(qtn.getLrlat());
//        lr.setLrlon(qtn.lrlon);
//        lr.setUllat(midLatitude);
//        lr.setUllon(midLongitude);
//
//        setLocations(qtn.upperLeft);
//        setLocations(qtn.upperRight);
//        setLocations(qtn.lowerleft);
//        setLocations(qtn.lowerRight);
//    }

    public ArrayList<QTNode> getNodesAtDepth(int d) {
        ArrayList<QTNode> tiles = new ArrayList<>();
        addTiles(root, tiles, d);
        return tiles;
    }

    public void addTiles(QTNode r, ArrayList<QTNode> i, int d) {
        if (r == null) {
            return;
        }
        if (d == 0) {
            i.add(r);
        } else {
            addTiles(r.lowerLeft, i, d - 1);
            addTiles(r.lowerRight, i, d - 1);
            addTiles(r.upperRight, i, d - 1);
            addTiles(r.upperLeft, i, d - 1);
        }
    }

    public QTNode getRoot() {
        return root;
    }

    public void setRoot(QTNode root) {
        this.root = root;
    }


//    public void insert(QTNode add) {
//        QTNode parentNode = insertHelper(root, add, 0);
//        char toRead = add.getFile().charAt(add.getFile().length() - 1);
//        int location = Character.getNumericValue(toRead);
//        if (location == 1) {
//            parentNode.setUpperLeft(add);
//        }
//        if (location == 2) {
//            parentNode.setUpperRight(add);
//
//        }
//        if (location == 3) {
//            parentNode.setLowerLeft(add);
//
//        }
//        if (location == 4) {
//            parentNode.setLowerRight(add);
//        }
//    }

//    private QTNode insertHelper(QTNode r, QTNode i, int d) {
//        int location = relativeLocation(d, i);
//        if (location == 1) {
//            if (r.upperLeft == null) {
//                return r;
//            } else {
//                return insertHelper(r.upperLeft, i, d + 1);
//            }
//        }
//        if (location == 2) {
//            if (r.upperRight == null) {
//                return r;
//            } else {
//                return insertHelper(r.upperRight, i, d + 1);
//            }
//
//        }
//        if (location == 3) {
//            if (r.lowerleft == null) {
//                return r;
//            } else {
//                return insertHelper(r.lowerleft, i, d + 1);
//            }
//
//        }
//        if (location == 4) {
//            if (r.lowerRight == null) {
//                return r;
//            } else {
//                return insertHelper(r.lowerRight, i, d + 1);
//            }
//
//        }
//        return null;
//    }

    private int relativeLocation(int d, QTNode i) {
        int a = Character.getNumericValue(i.getFile().charAt(d));
        return a;
    }


    @Override
    public String toString() {
        return "QuadTree{" + " root = " + root + '}';
    }

    public int getMaxdepth() {
        return maximumDepth;
    }

    public void setMaxdepth(int maxDepth) {
        this.maximumDepth = maxDepth;
    }

    /* Recursively retrieves the QuadTree nodes, mapping an ArrayList of QTNodes to each node's ullat. */
    void getQTNodesHelp(QTNode qtn, Query query, HashMap<Double, ArrayList<QTNode>> d) {
        if (qtn.getFile().equals("root")) {
            getQTNodesHelp(qtn.upperLeft, query, d);
            getQTNodesHelp(qtn.upperRight, query, d);
            getQTNodesHelp(qtn.lowerLeft, query, d);
            getQTNodesHelp(qtn.lowerRight, query, d);
        } else if (intersects(qtn, query) && lonDPPsmallerThanOrIsLeaf(qtn, query)) {
            if (d.containsKey(qtn.ullat)) {
                d.get(qtn.ullat).add(qtn);
            }
            if (!d.containsKey(qtn.ullat)) {
                ArrayList<QTNode> latTile = new ArrayList<>();
                latTile.add(qtn);
                d.put(qtn.ullat, latTile);
            }
            if (intersects(qtn, query) && !lonDPPsmallerThanOrIsLeaf(qtn, query)) {
                getQTNodesHelp(qtn.upperLeft, query, d);
                getQTNodesHelp(qtn.upperRight, query, d);
                getQTNodesHelp(qtn.lowerLeft, query, d);
                getQTNodesHelp(qtn.lowerRight, query, d);
            }
        }
    }

    private static boolean lonDPPsmallerThanOrIsLeaf(QTNode qtn, Query query) {
        if (qtn.depth == 7) {
            return true;
        }
        double qtnLonDPP = (qtn.ullon - qtn.lrlon) / 256;
        return query.lonDPP >= qtnLonDPP;
    }

    private boolean intersects(QTNode qtn, Query query) {
        if (qtn.lrlat > query.ULLAT || query.LRLAT > qtn.ullat) {
            return false;
        }
        if (qtn.ullon > query.LRLON || query.ULLON > qtn.lrlon) {
            return false;
        }
        if (qtn.ullat > query.LRLAT || query.ULLAT > qtn.lrlat) {
            return false;
        }
        if (qtn.lrlon > query.ULLON || query.LRLON > qtn.ullon) {
            return false;
        }
        return true;
    }
}
