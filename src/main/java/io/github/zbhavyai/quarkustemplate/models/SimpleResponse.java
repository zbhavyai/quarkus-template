package io.github.zbhavyai.quarkustemplate.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class SimpleResponse {

  private final String message;

  @JsonCreator
  private SimpleResponse(@JsonProperty("message") String message) {
    this.message = message;
  }

  public static SimpleResponse create(String message) {
    return new SimpleResponse(message);
  }

  @JsonProperty("message")
  public String getMessage() {
    return this.message;
  }
}
