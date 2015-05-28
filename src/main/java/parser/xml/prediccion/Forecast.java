package parser.xml.prediccion;

import com.google.gson.GsonBuilder;
import org.jdom2.Element;

//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Forecast {

    private static String[] ROW_NAMES = new String[8];

    static {
        ROW_NAMES[0] = "Date";
        ROW_NAMES[1] = "Sky";
        ROW_NAMES[2] = "Rainfall";
        ROW_NAMES[3] = "Snow";
        ROW_NAMES[4] = "Wind";
        ROW_NAMES[5] = "km/h";
        ROW_NAMES[7] = "Maximum UV";
    }

    public static long fcCreationDate;

    private long creationDate;
    private List<Day> days;

    /**
     * Generates a new forecast given a root xMl element
     */
    public static Forecast apply(Element root) throws ParseException {
        Forecast forecast = new Forecast();
        int added = 0;

        /* Parsing the date */
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        forecast.creationDate = formatter.parse(root.getChildText("elaborado").replace("T", ",")).getTime();
        Forecast.fcCreationDate = forecast.creationDate;
        /* Parse each day */
        root.getChild("prediccion")
                .getChildren()
                .forEach(e -> {
                    try {
                        forecast.days.add(Day.apply(e));
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                });

        Date toFilter = new Date();

        //Inefficient but works around the problem, should be focused while parsing the xml
        Forecast filtered = new Forecast();
        filtered.creationDate = forecast.creationDate;
        for (Day day : forecast.days) {
            List<Period> periods = new ArrayList<>();
            for (Period period : day.getPeriods()) {
                Date to_compare = new Date(day.getDate() + Integer.parseInt(period.getPeriod().split("-")[1]) * 60 * 60 * 1000);
                if (to_compare.compareTo(toFilter) > 0 && added++ < 2) {
                    periods.add(period);
                }
            }
            if(!periods.isEmpty()){
                day.getPeriods().clear();
                day.getPeriods().addAll(periods);
                filtered.days.add(day);
            }
        }

        return filtered;
    }

    public Element genHTMLTable() {
        Element r = new Element("table").setAttribute("border", "1");

        Element thead = new Element("thead");
        Element thead_1 = new Element("tr");
        Element thead_2 = new Element("tr");
        Element tbody = new Element("tbody");
        Element sky = new Element("tr");
        Element rain = new Element("tr");
        Element snow = new Element("tr");
        Element temp = new Element("tr");
        Element wind_dir = new Element("tr");
        Element wind_speed = new Element("tr");
        Element uv = new Element("tr");

        thead_1.addContent(new Element("th").setAttribute("rowspan", "2")
                .addContent(new Element("div").setAttribute("class", "cabecera_celda").setText("Date")));
        days.forEach(d -> thead_1.addContent(d.genHeader1()));

        days.forEach(d -> thead_2.addContent(d.genHeader2()));

        sky.addContent(new Element("th").setText("Sky"));
        days.forEach(d -> sky.addContent(d.genSkyHTML()));

        rain.addContent(new Element("th").setText("Rain"));
        days.forEach(d -> rain.addContent(d.genRainHTML()));

        snow.addContent(new Element("th").setText("Snow"));
        days.forEach(d -> snow.addContent(d.genSnowHTML()));

        temp.addContent(new Element("th").setText("Temp."));
        days.forEach(d -> temp.addContent(d.genTempHTML()));

        wind_dir.addContent(new Element("th").setText("Wind"));
        days.forEach(d -> wind_dir.addContent(d.genWindHTML()));

        wind_speed.addContent(new Element("th").setText("(km/h)"));
        days.forEach(d -> wind_speed.addContent(d.genWindSpeedHTML()));

//        uv.addContent(new Element("th").setText("Maximum UV"));
//        days.forEach(d -> uv.addContent(d.genUVHTML()));

        tbody.addContent(sky);
        tbody.addContent(rain);
        tbody.addContent(snow);
        tbody.addContent(temp);
        tbody.addContent(wind_dir);
        tbody.addContent(wind_speed);
//        tbody.addContent(uv);
        thead.addContent(thead_1);
        thead.addContent(thead_2);
        r.addContent(thead);
        r.addContent(tbody);
        return r;
    }

    private Forecast() {
        days = new ArrayList<>();
    }

    public long getCreationDate() {
        return creationDate;
    }

    public List<Day> getDays() {
        return days;
    }
}
