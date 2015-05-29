package parser.xml.prediccion;

import org.jdom2.Element;

public class Period {
    private String period;
    private int rain;
    private int snow;
    private Day.Wind wind;
    private Day.Sky sky;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getRain() {
        return rain;
    }

    public void setRain(int rain) {
        this.rain = rain;
    }

    public int getSnow() {
        return snow;
    }

    public void setSnow(int snow) {
        this.snow = snow;
    }

    public Day.Wind getWind() {
        return wind;
    }

    public void setWind(Day.Wind wind) {
        this.wind = wind;
    }

    public Day.Sky getSky() {
        return sky;
    }

    public void setSky(Day.Sky sky) {
        this.sky = sky;
    }

    public Period(String period) {
        this.period = period;
    }

    public Element genHeader2() {
        if(period.equals("00-24"))
            return null;
        Element r = new Element("th").setText(period);
        return r;
    }

    public Element genSkyHTML() {
        if(sky == null){
            return new Element("td").setText("");
        }
        return new Element("td").addContent(new Element("img").setAttribute("src", "http://www.aemet.es/imagenes/gif/estado_cielo/" + sky.getValue() + ".gif"));
    }

    public Element genRainHTML() {
        return new Element("td").setText(rain + "%");
    }

    public Element genSnowHTML() {
        return new Element("td").setText(Integer.toString(snow));
    }

    public Element genWindHTML() {
        if(sky == null){
            return new Element("td").setText("");
        }
        return new Element("td").addContent(new Element("img").setAttribute("src","http://www.aemet.es/imagenes/gif/iconos_viento/"+wind.getDirection()+".gif"));
    }

    public Element genWindSpeedHTML() {
        return new Element("td").setText(Integer.toString(wind.getSpeed()));
    }
}
