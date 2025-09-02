package io.github.zbhavyai.quarkustemplate.exceptions;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
