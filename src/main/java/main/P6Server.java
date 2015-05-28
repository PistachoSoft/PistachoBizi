package main;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import service.P6RestService;

public class P6Server {
    private static final Logger LOGGER = Grizzly.logger(P6Server.class);

    public static void main(String[] args) {
        LOGGER.setLevel(Level.FINER);

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create("http://localhost:9000/") ,
                new ResourceConfig().register(P6RestService.class));
        try {
            server.start();
            LOGGER.info("server running @ http://localhost:9000/");
            LOGGER.info("Press 's' to shutdown now the server...");
            while(true){
                int c = System.in.read();
                if (c == 's')
                    break;
            }
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, ioe.toString(), ioe);
        } finally {
            server.shutdown();
            LOGGER.info("Server stopped");
        }
    }
}
