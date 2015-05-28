package service;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import parser.xml.prediccion.Forecast;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;

public class P6SoapService {

    public String weatherXML(String xml){
        String r = null;
        try {
            URL url = Resources.getResource("input.dtd");
            String dtd = Resources.toString(url, Charsets.UTF_8);
            Document doc = new SAXBuilder().build(new StringReader(dtd + xml));
            int id = Integer.parseInt(doc.getRootElement().getChild("id").getText());
            r = generarHTML(descargaInfoTiempo(id));
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        return r;
    }

    public String weatherJSON(String json){
        JsonInput ji = new Gson().fromJson(json, JsonInput.class);
        return ji.getId() != -1 ? generarHTML(descargaInfoTiempo(ji.getId())) : null;
    }

    private String descargaInfoTiempo(int municipio) {
        String s = "http://www.aemet.es/xml/municipios/localidad_" + municipio + ".xml";
        String r = null;
        try {
            Document doc = new SAXBuilder().build(URI.create(s).toURL());
            r = new XMLOutputter(Format.getPrettyFormat()).outputString(doc);
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        return r;
    }

    private String generarHTML(String xml) {
        String r = null;
        try {
            Document doc = new SAXBuilder().build(new StringReader(xml));
            Forecast forecast = Forecast.apply(doc.getRootElement());
            r = new XMLOutputter().outputString(forecast.genHTMLTable());
        } catch (JDOMException | ParseException | IOException e) {
            e.printStackTrace();
        }
        return r;
    }

    private String generarJSON(String xml) {
        String r = null;
        try {
            Document doc = new SAXBuilder().build(new StringReader(xml));
            Forecast forecast = Forecast.apply(doc.getRootElement());
            r = new GsonBuilder().setPrettyPrinting().create().toJson(forecast);
        } catch (JDOMException | ParseException | IOException e) {
            e.printStackTrace();
        }
        return r;
    }
}
