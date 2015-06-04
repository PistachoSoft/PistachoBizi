package util;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian on 04/06/2015.
 */
public class StatsWrapper {
    List<JsonElement> stats = new ArrayList<>();

    public void add(JsonElement element){
        stats.add(element);
    }
}
