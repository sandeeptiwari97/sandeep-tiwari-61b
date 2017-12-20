/**
 * Created by sandeeptiwari on 4/18/17.
 */
public class Connect {
    Node n1;
    Node n2;
    String name;
    String way;
    String id;

    public Connect(String id, Node n1, Node n2, String name, String way) {
        this.id = id;
        this.n1 = n1;
        this.n2 = n2;
        this.name = name;
        this.way = way;
    }

    public Connect(String id, String name, String way) {
        this.id = id;
        this.name = name;
        this.way = way;
    }

    public Node getFirstNode() {
        return n1;
    }

    public void setFirstNode(Node n1) {
        this.n1 = n1;
    }

    public Node getNextNode() {
        return n2;
    }

    public void setNextNode(Node n2) {
        this.n2 = n2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
