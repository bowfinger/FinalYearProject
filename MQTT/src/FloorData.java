import java.util.Date;

/**
 * Created by Jamie on 13/11/2015.
 */
public class FloorData {
    private int Floor;
    private Date Timestamp;
    private int Count;

    public FloorData(int floor, Date timestamp, int count){
        this.Floor = floor;
        this.Timestamp = timestamp;
        this.Count = count;
    }

    public int getFloor() {
        return this.Floor;
    }

    public void setFloor(int floor) {
        this.Floor = floor;
    }

    public Date getTimestamp() {
        return this.Timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.Timestamp = timestamp;
    }

    public int getCount() {
        return this.Count;
    }

    public void setCount(int count) {
        this.Count = count;
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
