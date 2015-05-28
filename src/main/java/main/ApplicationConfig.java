package main;

import org.glassfish.jersey.server.ResourceConfig;
import service.P6RestService;

/**
 * Created by david on 29/05/2015.
 */
public class ApplicationConfig extends ResourceConfig {

    /**
     * Configuración de la aplicación
     * Registra el servicio donde se encuentran todos los endpoints
     * Adicionalmente registra la configuración de CORS
     * Todas las peticiones se harán a través de esta clase
     */
    public ApplicationConfig() {
//        register(MOXyJsonProvider.class);
        register(CrossDomainFilter.class);
        register(P6RestService.class);
//        register(SSEProvider.class);
//        registerInstances(new LoggingFilter(Logger.getLogger(ApplicationConfig.class.getName()),true));
    }
}
