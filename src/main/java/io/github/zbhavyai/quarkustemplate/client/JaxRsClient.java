package io.github.zbhavyai.quarkustemplate.client;

import io.github.zbhavyai.quarkustemplate.models.SimpleResponse;
import io.github.zbhavyai.quarkustemplate.utils.JSONMapper;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.CompletionStageRxInvoker;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.time.Duration;
import java.util.Map;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class JaxRsClient implements RestClient {

  private static final Logger LOGGER = Logger.getLogger(JaxRsClient.class.getSimpleName());

  private final Client client;
  private final Duration timeout;

  @Inject
  public JaxRsClient(@ConfigProperty(name = "timeout.secs", defaultValue = "5") long timeout) {

    this.client = ClientBuilder.newClient();
    this.timeout = Duration.ofSeconds(timeout);
  }

  @Override
  public Uni<Response> getRequest(String uri, Map<String, String> headers) {
    LOGGER.infof("getRequest: uri=\"%s\"", uri);

    return sendRequest(HttpMethod.GET, uri, headers, null);
  }

  @Override
  public Uni<Response> postRequest(String uri, Map<String, String> headers, Object payload) {
    LOGGER.infof("postRequest: uri=\"%s\"", uri);
    LOGGER.debugf("postRequest: payload=\"%s\"", JSONMapper.serialize(payload));

    return sendRequest(HttpMethod.POST, uri, headers, payload);
  }

  @Override
  public Uni<Response> putRequest(String uri, Map<String, String> headers, Object payload) {
    LOGGER.infof("putRequest: uri=\"%s\"", uri);
    LOGGER.debugf("putRequest: payload=\"%s\"", JSONMapper.serialize(payload));

    return sendRequest(HttpMethod.PUT, uri, headers, payload);
  }

  @Override
  public Uni<Response> patchRequest(String uri, Map<String, String> headers, Object payload) {
    LOGGER.infof("patchRequest: uri=\"%s\"", uri);
    LOGGER.debugf("patchRequest: payload=\"%s\"", JSONMapper.serialize(payload));

    return sendRequest(HttpMethod.PATCH, uri, headers, payload);
  }

  @Override
  public Uni<Response> deleteRequest(String uri, Map<String, String> headers) {
    LOGGER.infof("deleteRequest: uri=\"%s\"", uri);

    return sendRequest(HttpMethod.DELETE, uri, headers, null);
  }

  private Uni<Response> sendRequest(
      String method, String uri, Map<String, String> headers, Object payload) {
    LOGGER.debugf(
        "%s request: uri=\"%s\", payload=\"%s\"", method, uri, JSONMapper.serialize(payload));

    Invocation.Builder request = client.target(uri).request();
    headers.forEach((k, v) -> request.header(k, v));
    CompletionStageRxInvoker invoker = request.rx();

    return Uni.createFrom()
        .completionStage(
            payload == null ? invoker.method(method) : invoker.method(method, Entity.json(payload)))
        .onItem()
        .transform(r -> this.handleResponse(r))
        .onFailure()
        .transform(t -> this.handleFailure(t))
        .ifNoItem()
        .after(this.timeout)
        .failWith(() -> this.handleTimeout());
  }

  private Response handleResponse(Response res) {
    LOGGER.infof(
        "handleResponse: status=\"%d\", statusInfo=\"%s\", headers=\"%s\", startBuffer=\"%b\"",
        res.getStatus(), res.getStatusInfo().toEnum(), res.getHeaders(), res.bufferEntity());

    try {
      String body = res.readEntity(String.class);

      if (body == null || body.isBlank()) {
        // don't log empty body
      } else if (body.trim().startsWith("{") || body.trim().startsWith("[")) {
        JsonObject json = new JsonObject(body);
        LOGGER.debugf("handleResponse: body=\"%s\"", json.encode());
      } else {
        LOGGER.debugf("handleResponse: body=\"%s\"", body);
      }
    } catch (Exception e) {
      LOGGER.debugf("handleResponse: failed to read response body: \"%s\"", e.getMessage());
    }

    if (res.getStatus() >= 200 && res.getStatus() < 400) {
      return res;
    } else {
      throw new WebApplicationException(res);
    }
  }

  private Throwable handleFailure(Throwable t) {
    if (t instanceof WebApplicationException tw) {
      LOGGER.errorf(
          "handleFailure: statusCode=\"%s\", statusMessage=\"%s\", error=\"%s\"",
          tw.getResponse().getStatus(),
          tw.getResponse().getStatusInfo().getReasonPhrase(),
          tw.getLocalizedMessage());

      return new WebApplicationException(tw.getResponse());
    } else {
      LOGGER.errorf("handleFailure: error=\"%s\"", t.getLocalizedMessage());

      return new WebApplicationException(
          Response.status(Status.INTERNAL_SERVER_ERROR)
              .entity(SimpleResponse.create(t.getLocalizedMessage()))
              .build());
    }
  }

  private Throwable handleTimeout() {
    return new WebApplicationException(
        Response.status(Status.GATEWAY_TIMEOUT)
            .entity(SimpleResponse.create("Request timeout"))
            .build());
  }
}
