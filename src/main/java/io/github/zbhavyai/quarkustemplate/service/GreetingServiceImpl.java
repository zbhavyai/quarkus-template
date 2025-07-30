package io.github.zbhavyai.quarkustemplate.service;

import io.github.zbhavyai.quarkustemplate.models.SimpleResponse;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.jboss.logging.Logger;

@ApplicationScoped
public class GreetingServiceImpl implements GreetingService {

  private static final Logger LOGGER = Logger.getLogger(GreetingService.class.getSimpleName());

  @Override
  public Uni<Response> greet() {
    LOGGER.infof("greet");

    Response res = Response.status(Status.OK).entity(SimpleResponse.create("Hello World!")).build();

    return Uni.createFrom().item(res);
  }

  @Override
  public Uni<Response> error() {
    LOGGER.infof("error");

    return Uni.createFrom()
        .failure(
            () ->
                new WebApplicationException(
                    Response.status(Status.BAD_REQUEST)
                        .entity(SimpleResponse.create(Status.BAD_REQUEST.getReasonPhrase()))
                        .build()));
  }
}
