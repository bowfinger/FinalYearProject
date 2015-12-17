import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Jamie on 17/12/2015.
 */
public class CustomSerializer implements JsonSerializer<FloorData> {
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");

    @Override
    public JsonElement serialize(FloorData floorData, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("Floor", floorData.getFloor());
        obj.addProperty("Timestamp", df.format(floorData.getTimestamp()));
        obj.addProperty("Count", floorData.getCount());

        return obj;
    }
}
