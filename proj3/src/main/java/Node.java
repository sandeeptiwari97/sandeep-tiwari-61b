/**
 * Created by sandeeptiwari on 4/18/17.
 */
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Node implements Comparable<Node> {
    String id;
    double latitude;
    double longitude;
    String name;
    HashSet<Connect> connectSet;
    double distance;
    double priority;
    Node previous;



    public Node(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        name = "";
        distance = 0;
        connectSet = new HashSet<Connect>();
    }

    public double distanceTo(double yLatitude, double yLongitude) {
        return Math.pow((latitude - yLatitude), 2) + Math.pow((longitude - yLongitude), 2);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Connect> getConnectSet() {
        return connectSet;
    }

    public void setConnectSet(HashSet<Connect> connectSet) {
        this.connectSet = connectSet;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public double distanceTo(Node n) {
        double lonDistance = Math.pow((this.longitude - n.getLongitude()), 2);
        return Math.sqrt(lonDistance + Math.pow((this.latitude - n.getLatitude()), 2));
    }

    public double getDist() {
        return distance;
    }

    public void setDist(double distance) {
        this.distance = distance;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public ArrayList<Node> getNeighbor() {
        ArrayList<Node> neighbors = new ArrayList<>();
        for (Connect c: connectSet) {
            if (c.getFirstNode().getId().equals(id)) {
                neighbors.add(c.getNextNode());
            } else {
                neighbors.add(c.getFirstNode());
            }
        }
        return neighbors;
    }

    public int compareTo(Node n) {
        if (this.getPriority() < n.getPriority()) {
            return -1;
        } else if (this.getPriority() > n.getPriority()) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Node{" + "id=" + id + ", lat=" + latitude + ", lon=" + longitude + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Node)) {
            return false;
        }

        Node node = (Node) o;

        if (Double.compare(node.latitude, latitude) != 0) {
            return false;
        }
        if (Double.compare(node.longitude, longitude) != 0) {
            return false;
        }
        if (id != null ? !id.equals(node.id) : node.id != null) {
            return false;
        }
        return name != null ? name.equals(node.name) : node.name == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
