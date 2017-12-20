/**
 * Created by sandeeptiwari on 4/19/17.
 */
public class Query {
    double ULLAT;
    double ULLON;
    double LRLAT;
    double LRLON;
    double lonDPP;

    public Query(double ullat, double ullon, double lrlat, double lrlon, double width) {
        this.ULLAT = ullat;
        this.ULLON = ullon;
        this.LRLAT = lrlat;
        this.LRLON = lrlon;
        this.lonDPP = (ullon - lrlon) / width;
    }
}
