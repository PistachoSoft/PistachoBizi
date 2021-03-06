package service;


import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.glassfish.grizzly.Grizzly;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import util.ParseData;
import util.StatsInput;
import util.StatsWrapper;
import util.Towns;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import java.util.*;
import java.util.logging.Logger;

@Path("/")
public class P6RestService {

    private static final Logger LOGGER = Grizzly.logger(P6RestService.class);
    private static final String PARSE_URL = "https://api.parse.com/1/classes/";
    private static final String PARSE_APP_ID = "aRTM8aCEZlZiD70JaRC1JtzW2eTch90VlLYtI4TA";
    private static final String PARSE_REST_KEY = "3rH6uIPaw9o7o4bIEwbXUrdWbC7sEGMZev4zn7hB";

    @GET
    @Path("/weather/{city}/{envelope}")
    @Produces("text/html")
    public String weatherService(@PathParam("city") int city, @PathParam("envelope") String envelope) {

        LOGGER.info("Request: " + city + "," + envelope);

        String param;
        // Verify the envelope request and gen the param needed, return BadRequest in case is wrong
        switch (envelope.toUpperCase()) {
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
    public Towns getTowns() {
        return new Towns();
    }

    @POST
    @Path("/stats")
    @Consumes("application/json")
    public Response logStats(StatsInput stats,
                             @HeaderParam("user-agent") String userAgentString,
                             @Context HttpServletRequest request) {

        LOGGER.info("StatsInput update: " + stats + ", request by: " + request.getRemoteAddr());

        UserAgent userAgent = new UserAgent(userAgentString);

        // Set all the inputs to insert the data in parse
        ParseData parseInput = new ParseData();
        parseInput.setIp(request.getRemoteAddr());
        parseInput.setBrowser(userAgent.getBrowser().getGroup().getName());
        parseInput.setData(stats.getData());

        // Set parse parameters
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("X-Parse-Application-Id", PARSE_APP_ID);
        headersMap.put("X-Parse-REST-API-Key", PARSE_REST_KEY);
        headersMap.put("Content-Type", "application/json");

        HttpHeaders headers = new HttpHeaders();
        headers.setAll(headersMap);

        HttpEntity<String> entity = new HttpEntity<>(new Gson().toJson(parseInput), headers);

        // Call Parse Rest API
        ResponseEntity<String> responseEntity = new RestTemplate()
                .exchange(PARSE_URL + stats.getMethod().toUpperCase()
                        , HttpMethod.POST, entity, String.class);

        if (responseEntity.getStatusCode().value() >= 200 &&
                responseEntity.getStatusCode().value() < 300)
            return Response.ok().build();
        else
            throw new BadRequestException();
    }

    @GET
    @Path("/stats/{method}")
    @Produces("application/json")
    public Response getMethodStats(@PathParam("method") String method) {
        Gson gson = new Gson();

        LOGGER.info("Stats request: " + method);

        if (!("envelope".equalsIgnoreCase(method) || "browser".equalsIgnoreCase(method) ||
                "weather".equalsIgnoreCase(method) || "info".equalsIgnoreCase(method)))
            throw new BadRequestException();

        StatsWrapper wrapper = new StatsWrapper();

        String field = "browser".equalsIgnoreCase(method) ? "browser" : "data";

        List<String> dataInfo = P6RestService.getData(method, field);

        Collection<String> dataUpper = Collections2.transform(dataInfo, String::toUpperCase);

        Set<String> dataUnique = new HashSet<>(dataUpper);

        for (String data : dataUnique) {
            wrapper.add(data, Collections.frequency(dataUpper, data));
        }

        return Response.ok(gson.toJson(wrapper)).build();
    }

    /**
     * given a class in parse looks for the param env and verifies whether the param
     * "env" is json or soap
     *
     * @param parseClass the class in parse
     */
    private static List<String> getData(String parseClass, String field) {
        JsonParser parser = new JsonParser();


        List<String> r = new ArrayList<>();

        // Set parse parameters
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("X-Parse-Application-Id", PARSE_APP_ID);
        headersMap.put("X-Parse-REST-API-Key", PARSE_REST_KEY);

        HttpHeaders headers = new HttpHeaders();
        headers.setAll(headersMap);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = new RestTemplate()
                .exchange(PARSE_URL + parseClass.toUpperCase() + "?limit=0&count=1"
                        , HttpMethod.GET, entity, String.class);

        int count = parser.parse(responseEntity.getBody()).getAsJsonObject().get("count").getAsInt();

        responseEntity = new RestTemplate()
                .exchange(PARSE_URL + parseClass.toUpperCase() + "?limit=" + count
                        , HttpMethod.GET, entity, String.class);

        JsonObject jsonObject = parser.parse(responseEntity.getBody()).getAsJsonObject();

        for (JsonElement jsonElement : jsonObject.getAsJsonArray("results")) {
            r.add(jsonElement.getAsJsonObject().get(field).getAsString());
        }

        return r;
    }
}
