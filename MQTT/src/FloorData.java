import java.util.Date;

/**
 * Created by Jamie on 13/11/2015.
 */
public class FloorData {
    private int floor;
    private Date timestamp;
    private int count;

    public FloorData(int floor, Date timestamp, int count){
        this.floor = floor;
        this.timestamp = timestamp;
        this.count = count;
    }

    public int getFloor() {
        return this.floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    //should it return empty string or null/crash?
    public String getTimestampAsString() {
        return (this.timestamp == null ? this.timestamp.toString() : "");
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    // Used for testing/console output
    @Override
    public String toString() {
        return ("----- Floor data -----" + "" +
                "\nFloor: " + this.getFloor() +
                "\nTimestamp: " + this.getTimestamp() +
                "\nPassenger Count: " + this.getCount());
    }

}
