package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian on 04/06/2015.
 */
public class BrowserStats {
    List<BrowserPair> stats = new ArrayList<>();

    public void addBrowser(String browser, int number){
        BrowserPair bp = new BrowserPair();
        bp.browser = browser;
        bp.number = number;
        stats.add(bp);
    }

    private class BrowserPair {
        String browser;
        int number;
    }
}
