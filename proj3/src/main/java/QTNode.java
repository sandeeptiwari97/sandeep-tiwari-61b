/**
 * Created by sandeeptiwari on 4/17/17.
 */
public class QTNode implements Comparable<QTNode> {
    private String file;
    double ullat;
    double ullon;
    double lrlon;
    double lrlat;

    QTNode upperLeft;
    QTNode upperRight;
    QTNode lowerLeft;
    QTNode lowerRight;

    int depth;

    public QTNode(String fileName, QTNode nw, QTNode ne,
                        QTNode sw, QTNode se) {
        this.file = fileName;
        this.upperLeft = nw;
        this.upperRight = ne;
        this.lowerLeft = sw;
        this.lowerRight = se;
    }

    public QTNode(String fileName, double ULLAT, double ULLON, double LRLAT, double LRLON, int d) {
        this.ullat = ULLAT;
        this.ullon = ULLON;
        this.lrlat = LRLAT;
        this.lrlon = LRLON;
        this.file = fileName;
        this.depth = d;
        if (depth < 8) {
            this.upperLeft = new QTNode(myFileName(fileName, 1), ULLAT, ULLON, (ULLAT + LRLAT) / 2, (ULLON + LRLON) / 2, depth + 1);
            this.upperRight = new QTNode(myFileName(fileName, 2), ULLAT, (ULLON + LRLON) / 2, (ULLAT + LRLAT) / 2, LRLON, depth + 1);
            this.lowerLeft = new QTNode(myFileName(fileName, 3), (ULLAT + LRLAT) / 2, ULLON, LRLAT, (ULLON + LRLON) / 2, depth + 1);
            this.lowerRight = new QTNode(myFileName(fileName, 4), (ULLAT + LRLAT) / 2, (ULLON + LRLON) / 2, LRLAT, LRLON, depth + 1);
        }
    }

    public String myFileName(String parent, int currNode) {
        int dotIndex = parent.lastIndexOf(".");
        String parsing = parent.substring(0, dotIndex);
        if (parent.equals("root.png")) {
            if (currNode == 1) {
                return "1.png";
            } else if (currNode == 2) {
                return "2.png";
            } else if (currNode == 3) {
                return "3.png";
            } else if (currNode == 4) {
                return "4.png";
            }
        } else {
            if (currNode == 1) {
                parsing += "1.png";
                return parsing;
            } else if (currNode == 2) {
                parsing += "2.png";
                return parsing;
            } else if (currNode == 3) {
                parsing += "3.png";
                return parsing;
            } else if (currNode == 4) {
                parsing += "4.png";
                return parsing;
            }
        }
        System.out.println(parsing);
        return parsing;
    }

//    public double getUllon() {
//        return ullon;
//    }
//
//    public void setUllon(double ullon) {
//        this.ullon = ullon;
//    }
//
//    public double getUllat() {
//        return ullat;
//    }
//
//    public void setUllat(double ullat) {
//        this.ullat = ullat;
//    }
//
//    public double getLrlon() {
//        return lrlon;
//    }
//
//    public void setLrlon(double lrlon) {
//        this.lrlon = lrlon;
//    }
//
//    public double getLrlat() {
//        return lrlat;
//    }
//
//    public void setLrlat(double lrlat) {
//        this.lrlat = lrlat;
//    }
//
    public String getFile() {
        return file;
    }

    public void setFile(String fileName) {
        this.file = fileName;
    }
//
//    public QTNode getUpperLeft() {
//        return upperRight;
//    }
//
//    public void setUpperLeft(QTNode upperLeft) {
//        this.upperRight = upperLeft;
//    }
//
//    public QTNode getUpperRight() {
//        return upperRight;
//    }
//
//    public void setUpperRight(QTNode upperRight) {
//        this.upperRight = upperRight;
//    }
//
//    public QTNode getLowerLeft() {
//        return lowerLeft;
//    }
//
//    public void setLowerLeft(QTNode bottomLeft) {
//        this.lowerLeft = bottomLeft;
//    }
//
//    public QTNode getLowerRight() {
//        return lowerRight;
//    }
//
//    public void setLowerRight(QTNode bottomRight) {
//        this.lowerRight = bottomRight;
//    }

    public int compareTo(QTNode qtn) {
        if (ullat == qtn.ullat) {
            if(ullon == qtn.ullon) {
                return 0;
            }
            if (ullon < qtn.ullon) {
                return -1;
            }
            return 1;
        }
        if (ullat < qtn.ullat) {
            return 1;
        }
        if (ullat > qtn.ullat) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "QuadTreeNode {" + "fileName = " + file + "\'" + ", upperLeft = " + upperLeft
                + ", upperRight = " + upperRight + ", bottomLeft = " + lowerLeft + ", bottomRight = "
                + lowerRight + "}";
    }
}
