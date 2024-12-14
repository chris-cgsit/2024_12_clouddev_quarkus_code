package at.cgsit.demo.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TestResourceTest {

    @Test
    @Disabled("id is not ok")
    void testReadObjectById() {
        // .when().get("/test/1") hard coded
        int id = 1;
        given()
                .when().get("/test/{id}", id) // Pass a variable
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body(is(notNullValue()))
                .body("id", is(id))
                .body("name", notNullValue());
                // .body("description", notNullValue());
    }

    @Test
    void readAll() {
    }
}