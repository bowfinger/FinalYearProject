import com.google.gson.*;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Jamie on 13/11/2015.
 */
public class CustomDeserializer implements JsonDeserializer<FloorData> {
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");


    @Override
    public FloorData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        //Map.Entry<String, JsonElement> entry = obj.entrySet().iterator().next();
        //if (entry == null) return null;
        int floor = obj.get("Floor").getAsInt();
        Date date;
        try
        {
            date = df.parse(obj.get("Timestamp").getAsString());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            date = null;
        }
        int count = obj.get("Count").getAsInt();
        return new FloorData(floor, date, count);
    }
}
