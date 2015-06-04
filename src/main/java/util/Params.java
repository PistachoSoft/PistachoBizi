package util;

/**
 * Created by areyes on 04/06/15.
 */
public class Params {
    Integer id;
    String dest;
    String env;
    Float lat;
    Float lng;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }


    public void setLng(Float lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Params{" +
                "id=" + id +
                ", dest='" + dest + '\'' +
                ", env='" + env + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }

}
