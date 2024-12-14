package at.cgsit.demo.resource;

import at.cgsit.demo.dto.TestDTO;
import at.cgsit.demo.model.TestEntity;
import at.cgsit.demo.repository.TestEntityRepository;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.metrics.Metric;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

@Path("test")
public class TestResource {
    private static final Logger LOG = Logger.getLogger(TestResource.class);

    @Inject
    TestEntityRepository testEntityRepository;

    @Operation( summary = "read a Test DTO Object by ID",
            description = "read a Test DTO Object by ID and return it",
            operationId = "readTestDtoById")
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TestDTO readObjectById(
            @Parameter(name = "input", description = "The TestDTO Input object to store", required = true, allowEmptyValue = false)
            @PathParam("id") String id
    ){
        LOG.infov("input {0} , objectOutput {1}",  id, "");

        int pId = 0;
        try {
            pId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            LOG.warnv ("invalid input {}", id);
            throw e;
        }

        TestEntity result = testEntityRepository.readTestEntityById(pId);

        TestDTO dto = new TestDTO();
        dto.setId(result.getId().longValue());
        dto.setName(result.getName());
        dto.setVorname("");

        return dto;
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(name = "testResourceFindAllCount",
            description = "How many primality checks have been performed.")
    @Timed(name = "testResourceFindAllTimer",
            description = "A measure of how long it takes to perform the primality test.",
            unit = MetricUnits.MICROSECONDS)
    public List<TestDTO> readAll(
    ){
        LOG.infov("called read all objects ");

        List<TestEntity> resultList = testEntityRepository.findAll(TestEntity.class);

        List<TestDTO> dtoList = new ArrayList<TestDTO>();
        for (TestEntity result : resultList) {
            TestDTO dto = new TestDTO();
            dto.setId(result.getId().longValue());
            dto.setName(result.getName());
            dto.setVorname("");
            dtoList.add(dto);
        }

        return dtoList;
    }

    @PUT
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTestEntity(
            @Parameter(name = "name", description = "The name of the TestEntity", required = true, allowEmptyValue = false)
            @PathParam("name") String name
    ) {
        LOG.infov("Creating new TestEntity with name: {0}", name);

        // Validate input
        if (name == null || name.isEmpty()) {
            LOG.warn("Invalid name parameter");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Name parameter cannot be empty")
                    .build();
        }

        try {
            // Create and populate TestEntity
            TestEntity newEntity = new TestEntity();
            newEntity.setName(name);

            // insert. newEntity will be updated with ID
            testEntityRepository.insertTestEntity(newEntity);

            // Map entity to DTO for response
            TestDTO dto = new TestDTO();
            dto.setId(newEntity.getId().longValue()); // Assuming ID is generated after persist
            dto.setName(newEntity.getName());
            dto.setVorname("");

            // Return 201 Created response with DTO
            return Response.status(Response.Status.CREATED)
                    .entity(dto)
                    .build();

        } catch (Exception e) {
            LOG.error("Error creating TestEntity", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating entity")
                    .build();
        }
    }

    @PUT
    @Path("/{id}/{name}/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTestEntity(
            @Parameter(name = "id", description = "The ID of the TestEntity", required = true, allowEmptyValue = false)
            @PathParam("id") Long id,
            @Parameter(name = "name", description = "The new name for the TestEntity", required = true, allowEmptyValue = false)
            @PathParam("name") String name,
            @Parameter(name = "version", description = "The version for optimistic locking", required = true, allowEmptyValue = false)
            @PathParam("version") Long version
    ) {
        LOG.infov("Updating TestEntity with ID: {0}, new name: {1}, and version: {2}", id, name, version);

        // Validate input
        if (id == null || id <= 0) {
            LOG.warn("Invalid ID parameter");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID parameter must be a positive number")
                    .build();
        }

        if (name == null || name.isEmpty()) {
            LOG.warn("Invalid name parameter");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Name parameter cannot be empty")
                    .build();
        }

        if (version == null || version < 0) {
            LOG.warn("Invalid version parameter");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Version parameter must be a non-negative number")
                    .build();
        }

        try {
            // read the existing entity .. if not we get null !!
            TestEntity existingEntity = testEntityRepository.findById(id);
            if (existingEntity == null) {
                LOG.warnv("TestEntity with ID: {0} not found", id);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("entity not found")
                        .build();
            }

            // Update the entity's name
            existingEntity.setName(name);
            // also set the version number here
            // the client (e.g. angular remembered it, so we are sure no one else has change the object meanwhile
            existingEntity.setVersionNo(version);

            existingEntity = testEntityRepository.updateTestEntity(existingEntity);

            // Map updated entity to DTO for response
            TestDTO dto = new TestDTO();
            dto.setId(existingEntity.getId().longValue());
            dto.setVersionNumber(existingEntity.getVersionNo());
            dto.setName(existingEntity.getName());
            dto.setVorname("");

            // Return 200 OK response with updated DTO
            return Response.status(Response.Status.OK)
                    .entity(dto)
                    .build();
        } catch(OptimisticLockException exception) {
            LOG.error("OptimisticLockException : entity was already updated", exception);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("OptimisticLockException : entity was already updated")
                    .build();

        } catch (Exception e) {
            LOG.error("Error updating TestEntity", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating entity")
                    .build();
        }
    }


}
