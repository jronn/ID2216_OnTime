package id2216.ict.kth.se.ontime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jronn on 2015-02-20.
 */
public class Departure {
    private String transportation;
    private Date expectedDateTime;
    private String destination;

    public Departure(String transportation, Date expectedDateTime, String destination) {
        this.transportation = transportation;
        this.expectedDateTime = expectedDateTime;
        this.destination = destination;
    }

    public Departure() {

    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public Date getExpectedDateTime() {
        return expectedDateTime;
    }

    public void setExpectedDateTime(Date expectedDateTime) {
        this.expectedDateTime = expectedDateTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(transportation + ": ");
        sb.append(destination + ", ");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sb.append(sdf.format(expectedDateTime));

        return sb.toString();
    }

    public String getFormattedExpectedDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(expectedDateTime);
    }
}
