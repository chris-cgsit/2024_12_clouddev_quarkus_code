package at.cgsit.demo.restexceptions;


import at.cgsit.demo.resource.TestResource;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class NumberFormatExceptionMapper implements ExceptionMapper<NumberFormatException> {

    private static final Logger LOG = Logger.getLogger(NumberFormatExceptionMapper.class);

    @Override
    public Response toResponse(NumberFormatException exception) {
        LOG.infov("ungültige nummer erhalten {0}", exception.getMessage() );

        return Response
                .status(Response.Status.BAD_REQUEST) // 400 Bad Request
                .entity("Invalid number format: " + exception.getMessage())
                .build();
    }
}
