package io.github.zbhavyai.quarkustemplate.client;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Response;
import java.util.Map;

public interface RestClient {

  public Uni<Response> getRequest(String uri, Map<String, String> headers);

  public Uni<Response> postRequest(String uri, Map<String, String> headers, Object payload);

  public Uni<Response> putRequest(String uri, Map<String, String> headers, Object payload);

  public Uni<Response> patchRequest(String uri, Map<String, String> headers, Object payload);

  public Uni<Response> deleteRequest(String uri, Map<String, String> headers);
}
