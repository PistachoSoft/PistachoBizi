package util;

/**
 * Created by areyes on 04/06/15.
 */
public class Params {
    Long latitude;
    Long longitude;

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Params{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }
}
