package util;

import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;

public class Towns {
    List<Town> towns;

    public List<Town> getTowns() {
        return towns;
    }

    public void setTowns(List<Town> towns) {
        this.towns = towns;
    }

    public Towns() {
        try {
            towns = Town.getTowns();
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Towns{" +
                "towns=" + towns +
                '}';
    }
}
