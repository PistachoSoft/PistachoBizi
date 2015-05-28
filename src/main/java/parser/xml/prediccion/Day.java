package parser.xml.prediccion;

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

public class Day {

    private static int periods_added = 0;

    private long date;
    private List<Period> periods;
    private Temperature temperature;
    private int uvMax = -1;

    /**
     * Generates a Day from a given Element (html node)
     *
     * @param day
     * @return
     * @throws ParseException
     */
    public static Day apply(Element day) throws ParseException {
        Day d = new Day();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        d.date = formatter.parse(day.getAttributeValue("fecha")).getTime();
        d.periods = new ArrayList<>();

        List<String> periods_strings = new ArrayList<>();
        /* retrieve the periods used */
        int min = Integer.MAX_VALUE;
        for (int i = day.getChildren("prob_precipitacion").size() - 1; i >= 0; i--) {
            Element e = day.getChildren("prob_precipitacion").get(i);
            if (e.getContentSize() != 0 && e.getAttributeValue("periodo") != null) {
                String[] vals = e.getAttributeValue("periodo").split("-");
                if (min > Integer.parseInt(vals[0])) {
                    min = Integer.parseInt(vals[0]);
                    periods_strings.add(0, e.getAttributeValue("periodo"));
                }
            }
        }


        /* retrieve all the data based on its period */
        if (!periods_strings.isEmpty()) {
            periods_strings.forEach(e -> {
                    d.periods.add(new Period(e));
            });

            for (Element element : day.getChildren("prob_precipitacion")) {
                for (int i = 0; i < periods_strings.size(); i++) {
                    if (element.getAttributeValue("periodo").equals(periods_strings.get(i))) {
                        d.periods.get(i).setRain(Integer.parseInt(element.getValue()));
                    }
                }
            }
            for (Element element : day.getChildren("cota_nieve_prov")) {
                for (int i = 0; i < periods_strings.size(); i++) {
                    if (element.getAttributeValue("periodo").equals(periods_strings.get(i))) {
                        if (!element.getValue().isEmpty()) {
                            d.periods.get(i).setSnow(Integer.parseInt(element.getValue()));
                        }
                    }
                }
            }
            for (Element element : day.getChildren("estado_cielo")) {
                for (int i = 0; i < periods_strings.size(); i++) {
                    if (element.getAttributeValue("periodo").equals(periods_strings.get(i))) {
                        Sky s = new Sky();
                        s.description = element.getAttributeValue("descripcion");
                        s.value = element.getValue();
                        d.periods.get(i).setSky(s);
                    }
                }
            }
            for (Element element : day.getChildren("viento")) {
                for (int i = 0; i < periods_strings.size(); i++) {
                    if (element.getAttributeValue("periodo").equals(periods_strings.get(i))) {
                        Wind w = new Wind();
                        w.direction = element.getChildText("direccion");
                        w.speed = Integer.parseInt(element.getChildText("velocidad"));
                        d.periods.get(i).setWind(w);
                    }
                }
            }
        }
        /* special case, no period. I wonder, why the hell aemet doesn't use the 00-24 period ... */
        else {
            Period i = new Period("00-24");
            i.setRain(Integer.parseInt(day.getChildText("prob_precipitacion")));
            if (!day.getChildText("cota_nieve_prov").isEmpty())
                i.setSnow(Integer.parseInt(day.getChildText("cota_nieve_prov")));
            Wind w = new Wind();
            w.direction = day.getChild("viento").getChildText("direccion");
            w.speed = Integer.parseInt(day.getChild("viento").getChildText("velocidad"));
            i.setWind(w);
            Sky s = new Sky();
            s.description = day.getChild("estado_cielo").getAttributeValue("descripcion");
            s.value = day.getChildText("estado_cielo");
            d.periods.add(i);
        }

        /* Shared data between periods */
        d.temperature = new Temperature();
        d.temperature.min = Integer.parseInt(day.getChild("temperatura").getChildText("minima"));
        d.temperature.max = Integer.parseInt(day.getChild("temperatura").getChildText("maxima"));
        if (day.getChildText("uvMax") != null) d.uvMax = Integer.parseInt(day.getChildText("uvMax"));

        return d;
    }

    /**
     * Generates a new day given a ResultSet, the ResultSet must be
     * point to a Day row
     */
//    public static Day apply(ResultSet rs, Connection con) throws SQLException {
//        Day d = new Day();
//        d.date = rs.getLong(2);
//        d.temperature.min = rs.getInt(3);
//        d.temperature.max = rs.getInt(4);
//        d.uvMax = rs.getInt(5);
//
//        Statement st = con.createStatement();
//        ResultSet dummy = st.executeQuery("Select * FROM interv " +
//                "WHERE day_id = " + rs.getInt(1));
//        while (dummy.next()) {
//            Period p = new Period(dummy.getString(2));
//            p.setRain(dummy.getInt(3));
//            p.setSnow(dummy.getInt(4));
//            Wind w = new Wind();
//            w.direction = dummy.getString(5);
//            w.speed = dummy.getInt(6);
//            p.setWind(w);
//            if(dummy.getString(7) != null){
//                Sky s = new Sky();
//                s.description = dummy.getString(7);
//                s.value = dummy.getString(8);
//                p.setSky(s);
//            }
//            d.periods.add(p);
//        }
//
//        st.close();
//        return d;
//    }
    public Day() {
        temperature = new Temperature();
        periods = new ArrayList<>();
    }

    public long getDate() {
        return date;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public int getUvMax() {
        return uvMax;
    }

    /**
     * Generates the first header line
     *
     * @return
     */
    public Element genHeader1() {
        if (periods.size() == 1 && periods.get(0).getPeriod().equals("00-24")) {
            return new Element("th").setAttribute("rowspan", "2")
                    .setText(new Date(date).toString().substring(0, 10));
        } else {
            return new Element("th").setAttribute("colspan", Integer.toString(periods.size()))
                    .setText(new Date(date).toString().substring(0, 10));
        }
    }

    /**
     * Generates the second header line (periods names)
     *
     * @return
     */
    public List<Element> genHeader2() {
        List<Element> r = new ArrayList<>();
        periods.forEach(p -> {
            if (p.genHeader2() != null)
                r.add(p.genHeader2());
        });
        return r;
    }

    /**
     * Generates the Sky condition HTML th with its image
     */
    public List<Element> genSkyHTML() {
        List<Element> r = new ArrayList<>();
        periods.forEach(p -> r.add(p.genSkyHTML()));
        return r;
    }

    /**
     * Generates the rain's HTML
     *
     * @return
     */
    public List<Element> genRainHTML() {
        List<Element> r = new ArrayList<>();
        periods.forEach(p -> r.add(p.genRainHTML()));
        return r;
    }

    /**
     * Generates the snow's HTML
     *
     * @return
     */
    public List<Element> genSnowHTML() {
        List<Element> r = new ArrayList<>();
        periods.forEach(p -> r.add(p.genSnowHTML()));
        return r;
    }

    /**
     * Generates the Temperature's HTML
     *
     * @return
     */
    public Element genTempHTML() {
        return new Element("th").setAttribute("colspan", Integer.toString(periods.size()))
                .setText(temperature.min + "/" + temperature.max);
    }

    /**
     * Generates the Wind's HTML with its image
     *
     * @return
     */
    public List<Element> genWindHTML() {
        List<Element> r = new ArrayList<>();
        periods.forEach(p -> r.add(p.genWindHTML()));
        return r;
    }

    /**
     * Generates the Wind speed HTML
     *
     * @return
     */
    public List<Element> genWindSpeedHTML() {
        List<Element> r = new ArrayList<>();
        periods.forEach(p -> r.add(p.genWindSpeedHTML()));
        return r;
    }

    /**
     * Generates the Maximum UV HTML
     *
     * @return
     */
    public Element genUVHTML() {
        return new Element("th").setAttribute("colspan", Integer.toString(periods.size()))
                .setText(uvMax == -1 ? "" : Integer.toString(uvMax));
    }

    /**
     * This class is used to store a temperature data (min / max
     */
    public static class Temperature {
        private int min;
        private int max;

        public Temperature() {
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }
    }

    /**
     * This is used to store a sky condition data
     */
    public static class Sky {
        private String description;
        private String value;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Sky() {
        }
    }

    /**
     * This is used to store a Wind condition data
     */
    public static class Wind {
        private String direction;
        private int speed;

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public Wind() {

        }
    }
}
