package io.github.zbhavyai.quarkustemplate.client;

import io.github.zbhavyai.quarkustemplate.models.SimpleResponse;
import io.github.zbhavyai.quarkustemplate.utils.JSONMapper;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.MultiMap;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.HttpRequest;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.codec.BodyCodec;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.time.Duration;
import java.util.Map;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class VertxRestClient implements RestClient {

  private static final Logger LOGGER = Logger.getLogger(VertxRestClient.class.getSimpleName());

  private final WebClient client;
  private final Duration timeout;

  @Inject
  public VertxRestClient(
      Vertx vertx, @ConfigProperty(name = "timeout.secs", defaultValue = "5") long timeout) {

    this.client = WebClient.create(vertx);
    this.timeout = Duration.ofSeconds(timeout);
  }

  @Override
  public Uni<Response> getRequest(String uri, Map<String, String> headers) {
    LOGGER.infof("getRequest: uri=\"%s\"", uri);

    HttpRequest<JsonObject> req =
        this.client
            .getAbs(uri)
            .as(BodyCodec.jsonObject())
            .putHeaders(this.convertToMultiMap(headers));

    return req.send()
        .onItem()
        .transform(r -> this.handleResponse(r))
        .onFailure()
        .transform(t -> this.handleFailure(t))
        .ifNoItem()
        .after(this.timeout)
        .failWith(this.handleTimeout());
  }

  @Override
  public Uni<Response> postRequest(String uri, Map<String, String> headers, Object payload) {
    LOGGER.infof("postRequest: uri=\"%s\"", uri);
    LOGGER.debugf("postRequest: payload=\"%s\"", JSONMapper.serialize(payload));

    HttpRequest<JsonObject> req =
        this.client
            .postAbs(uri)
            .as(BodyCodec.jsonObject())
            .putHeaders(this.convertToMultiMap(headers));

    return req.sendJson(payload)
        .onItem()
        .transform(r -> this.handleResponse(r))
        .onFailure()
        .transform(t -> this.handleFailure(t))
        .ifNoItem()
        .after(this.timeout)
        .failWith(this.handleTimeout());
  }

  @Override
  public Uni<Response> putRequest(String uri, Map<String, String> headers, Object payload) {
    LOGGER.infof("putRequest: uri=\"%s\"", uri);
    LOGGER.debugf("putRequest: payload=\"%s\"", JSONMapper.serialize(payload));

    HttpRequest<JsonObject> req =
        this.client
            .putAbs(uri)
            .as(BodyCodec.jsonObject())
            .putHeaders(this.convertToMultiMap(headers));

    return req.sendJson(payload)
        .onItem()
        .transform(r -> this.handleResponse(r))
        .onFailure()
        .transform(t -> this.handleFailure(t))
        .ifNoItem()
        .after(this.timeout)
        .failWith(this.handleTimeout());
  }

  @Override
  public Uni<Response> patchRequest(String uri, Map<String, String> headers, Object payload) {
    LOGGER.infof("patchRequest: uri=\"%s\"", uri);
    LOGGER.debugf("patchRequest: payload=\"%s\"", JSONMapper.serialize(payload));

    HttpRequest<JsonObject> req =
        this.client
            .patch(uri)
            .as(BodyCodec.jsonObject())
            .putHeaders(this.convertToMultiMap(headers));

    return req.sendJson(payload)
        .onItem()
        .transform(r -> this.handleResponse(r))
        .onFailure()
        .transform(t -> this.handleFailure(t))
        .ifNoItem()
        .after(this.timeout)
        .failWith(this.handleTimeout());
  }

  @Override
  public Uni<Response> deleteRequest(String uri, Map<String, String> headers) {
    LOGGER.infof("deleteRequest: uri=\"%s\"", uri);

    HttpRequest<JsonObject> req =
        this.client
            .delete(uri)
            .as(BodyCodec.jsonObject())
            .putHeaders(this.convertToMultiMap(headers));

    return req.send()
        .onItem()
        .transform(r -> this.handleResponse(r))
        .onFailure()
        .transform(t -> this.handleFailure(t))
        .ifNoItem()
        .after(this.timeout)
        .failWith(this.handleTimeout());
  }

  private <T> Response handleResponse(HttpResponse<T> res) {
    LOGGER.infof("handleResponse: status=\"%s\"", res.statusCode());
    LOGGER.debugf(
        "handleResponse: headers=\"%s\", body=\"%s\"",
        res.headers(), JSONMapper.serialize(res.body()));

    if (res.statusCode() >= 200 && res.statusCode() < 400) {
      return Response.status(res.statusCode())
          .replaceAll(this.convertToMultivaluedMap(res.headers()))
          .entity(res.body())
          .build();
    } else {
      Response errorResponse =
          Response.status(res.statusCode())
              .replaceAll(this.convertToMultivaluedMap(res.headers()))
              .entity(res.body() == null ? "" : res.body())
              .build();

      throw new WebApplicationException(errorResponse);
    }
  }

  private Throwable handleFailure(Throwable t) {
    if (t instanceof WebApplicationException tw) {
      LOGGER.errorf(
          "handleFailure: statusCode=\"%s\", statusMessage=\"%s\" error=\"%s\"",
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

  private MultiMap convertToMultiMap(Map<String, String> obj) {
    return MultiMap.caseInsensitiveMultiMap().addAll(obj);
  }

  private MultivaluedMap<String, Object> convertToMultivaluedMap(MultiMap obj) {
    MultivaluedMap<String, Object> map = new MultivaluedHashMap<>();
    obj.names().forEach(name -> map.addAll(name, obj.get(name)));
    return map;
  }
}
