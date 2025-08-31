package io.github.zbhavyai.quarkustemplate.rest.ping;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class PingResourceTest {

  @Test
  void testPingEndpoint() {
    given().when().get("/ping").then().statusCode(200).body(is("pong\n"));
  }
}
