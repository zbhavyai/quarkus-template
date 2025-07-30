package io.github.zbhavyai.quarkustemplate.rest;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class GreetingRestTest {

  @Test
  void testGreet() {
    given()
        .when()
        .get("/api/v1/hello")
        .then()
        .statusCode(200)
        .contentType(MediaType.APPLICATION_JSON);
  }

  @Test
  void testError() {
    given()
        .when()
        .get("/api/v1/error")
        .then()
        .statusCode(400)
        .contentType(MediaType.APPLICATION_JSON);
  }
}
