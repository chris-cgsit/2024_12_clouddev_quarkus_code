package at.cgsit.demo;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

@Path("/hello")
public class ExampleResource {

    @Inject
    Logger logger;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        logger.warn("hello wurde aufgerufen");
        logger.infov( "LOGGER {0}", "myparameter");
        logger.debug("hello DEBUG wurde aufgerufen");

        return "Hallo Wifi Kurs sdfasdf";

    }
}
