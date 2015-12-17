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
public class DataInterpreter{

    //add checks and throw exceptions
    public static FloorData read(String jsonData){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(FloorData.class, new CustomDeserializer())
                .create();

        FloorData data = gson.fromJson(jsonData, FloorData.class);
        return data;
    }

    public static String write(FloorData floorData){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(FloorData.class, new CustomSerializer())
                .create();

        String dataToReturn = gson.toJson(floorData);
        return dataToReturn;
    }
}
