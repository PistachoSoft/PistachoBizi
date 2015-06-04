package util;

/**
 * Created by david on 03/06/2015.
 */
public class StatsInput {

    private String method;
    private String data;

    public StatsInput() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StatsInput{" +
                "method='" + method + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
