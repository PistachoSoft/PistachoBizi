package util;

import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Town {

    private static final String CSV = "/15codmun50.csv";

    private int id;
    private String town;

    public void setId(int id) {
        this.id = id;
    }

    public void setTown(String town) {
        this.town = town;
    }

    private Town(){};

    private Town(int id, String town) {
        this.id = id;
        this.town = town;
    }

    public int getId() {
        return id;
    }

    public String getTown() {
        return town;
    }

    public static List<Town> getTowns() throws JDOMException, IOException {
        List<Town> r = new ArrayList<>();

        // Don't even think about using a parser ... too simple to waste my time
        Scanner sc = new Scanner(Town.class.getResourceAsStream(CSV), "ISO-8859-1");
        while(sc.hasNextLine()){
            String[] l = sc.nextLine().split(";");
            r.add(new Town(Integer.parseInt(l[0]), l[1]));
        }
        sc.close();

        return r;
    }

    @Override
    public String toString() {
        return "Town{" +
                "id=" + id +
                ", town='" + town + '\'' +
                '}';
    }
}
