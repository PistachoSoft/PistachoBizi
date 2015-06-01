package service;


import com.google.gson.Gson;
import org.apache.axis.encoding.XMLType;
import org.glassfish.grizzly.Grizzly;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import javax.ws.rs.*;
import java.util.logging.Logger;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import util.Towns;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

@Path("/")
public class P6RestService {

    private static final Logger LOGGER = Grizzly.logger(P6RestService.class);

    @GET
    @Path("/weather/{city}/{envelope}")
    @Produces("text/html")
    public String weatherService(@PathParam("city") int city, @PathParam("envelope") String envelope){

        LOGGER.info("Request: " + city + "," + envelope);

        String param = null;
        // Verify the envelope request and gen the param needed, return BadRequest in case is wrong
        switch (envelope.toUpperCase()){
            case "JSON":
                param = new Gson().toJson(new JsonInput(city));
                break;
            case "XML":
                Element root = new Element("input").addContent(new Element("id").setText(Integer.toString(city)));
                param = new XMLOutputter().outputString(root);
                break;
            default:
                throw new BadRequestException();
        }

        LOGGER.info("Param request: " + param);

        String endpoint = "http://localhost:8080/axis/services/ForecastService";
        String r = null;
//
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();

            call.setTargetEndpointAddress(endpoint);
            call.setOperationName(new QName("ForecastService", "weather" + envelope.toUpperCase()));
            call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);

            r = (String) call.invoke(new Object[]{param});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return r;
    }

    @GET
    @Path("/towns")
    @Produces("application/json")
    public Towns getTowns(){
        return new Towns();
    }
}
