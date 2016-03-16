import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Jamie on 13/11/2015.
 */
public class DataInterpreter{

    //add checks and throw exceptions
    public static FloorData readFloorData(String jsonData){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(FloorData.class, new CustomDeserializer())
                .create();

        FloorData data = gson.fromJson(jsonData, FloorData.class);
        return data;
    }

    public static String writeFloorData(FloorData floorData){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(FloorData.class, new CustomSerializer())
                .create();

        String dataToReturn = gson.toJson(floorData);
        return dataToReturn;
    }

    public static String writeRouteData(RouteData routeData){
        return new Gson().toJson(routeData);
    }

    public static RouteData readRouteData(String jsonData){
        Type objType = new TypeToken<RouteData>() {}.getType();
        return new Gson().fromJson(jsonData, objType);
    }
}
