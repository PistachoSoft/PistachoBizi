package util;

/**
 * Created by david on 03/06/2015.
 */
public class Stats {

    private String method;
    private Params params;

    public Stats() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "method='" + method + '\'' +
                ", params='" + params + '\'' +
                '}';
    }
}
