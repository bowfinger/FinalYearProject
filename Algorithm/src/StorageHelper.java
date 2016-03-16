import java.util.Date;
import java.util.List;

/**
 * Created by Jamie on 16/03/2016.
 */
public interface StorageHelper {
    void store(List<FloorData> floorDataList);
    List<FloorData> get();
    List<FloorData> get(int floorNum);
    FloorData get(int floorNum, Date dateProxy);
}
