import java.util.Date;
import java.util.List;

/**
 * Created by Jamie on 16/03/2016.
 */
public class DataStorageHelper implements StorageHelper{

    @Override
    public void store(List<FloorData> floorDataList) {
        //send data to database
    }

    @Override
    public List<FloorData> get() {
        //get all floor data from db
        return null;
    }

    @Override
    public List<FloorData> get(int floorNum) {
        //get all floor data for specific floor
        return null;
    }

    @Override
    public FloorData get(int floorNum, Date dateProxy) {
        //get single floor data within date threshold
        return null;
    }
}
