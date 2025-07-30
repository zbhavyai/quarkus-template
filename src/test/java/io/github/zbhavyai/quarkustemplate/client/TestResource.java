package io.github.zbhavyai.quarkustemplate.client;

import io.github.zbhavyai.quarkustemplate.models.SimpleResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/test/v1/")
public class TestResource {

  private static final Logger LOGGER = Logger.getLogger(TestResource.class.getSimpleName());

  @POST
  public Uni<Response> testPost(String payload) {
    LOGGER.infof("testPost");

    return Uni.createFrom()
        .item(Response.ok(SimpleResponse.create("Test response with payload: " + payload)).build());
  }
}
