package io.github.zbhavyai.quarkustemplate.api.rest.note;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class NoteResourceTest {

  @Test
  void testNoteListing() {
    given()
        .when()
        .get("/v1/note")
        .then()
        .statusCode(200)
        .contentType(MediaType.APPLICATION_JSON)
        .body("size()", is(3))
        .body("[0].id", is("3282249b-19ee-4c5e-9be2-b9f714610aa6"))
        .body("[1].id", is("1d7539f9-e9b7-4a06-9e6c-d5d6cf74d87a"))
        .body("[2].id", is("e969ffd7-b4ce-47e1-8f43-8811ae576392"));
  }
}
