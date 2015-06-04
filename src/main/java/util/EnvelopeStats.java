package util;

/**
 * Created by Adrian on 04/06/2015.
 */
public class EnvelopeStats {
    EnvelopeInnerStat stats = new EnvelopeInnerStat();

    public void setJSON(int count) {
        stats.json = count;
    }

    public void setSOAP(int count) {
        stats.soap = count;
    }

    private class EnvelopeInnerStat {
        int json;
        int soap;
    }
}
