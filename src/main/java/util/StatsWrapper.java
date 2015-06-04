package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian on 04/06/2015.
 */
public class StatsWrapper {
    List<DataPair> stats = new ArrayList<>();

    public void add(String browser, int number){
        DataPair bp = new DataPair();
        bp.data = browser;
        bp.number = number;
        stats.add(bp);
    }

    private class DataPair {
        String data;
        int number;
    }
}
