package id2216.ict.kth.se.ontime;

/**
 * Represents a station or bus stop, containing its name and siteId
 *
 * Created by jronn on 2015-02-19.
 */
public class Station {
    private String name;
    private int siteId;

    public Station(String name, int siteId) {
        this.name = name;
        this.siteId = siteId;
    }

    public String getName() {
        return name;
    }

    public int getSiteId() {
        return siteId;
    }

    public String toString() {
        return name;
    }
}
