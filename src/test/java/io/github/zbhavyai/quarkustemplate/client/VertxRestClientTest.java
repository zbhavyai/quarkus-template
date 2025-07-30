package io.github.zbhavyai.quarkustemplate.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class VertxRestClientTest {

  @Inject private VertxRestClient restClient;
  private String baseURL;
  private Map<String, String> headers;

  @BeforeEach
  void init() {
    baseURL = "http://127.0.0.1:8081";
    headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
  }

  @Test
  void testPostRequest() {
    Response res =
        restClient.postRequest(baseURL + "/test/v1/", headers, "custom").await().indefinitely();
    assertEquals(200, res.getStatus());
  }
}
