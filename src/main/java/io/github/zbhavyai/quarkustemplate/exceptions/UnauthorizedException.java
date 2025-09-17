package io.github.zbhavyai.quarkustemplate.exceptions;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;

public class UnauthorizedException extends ClientErrorException {

  public UnauthorizedException() {
    super(Response.Status.UNAUTHORIZED);
  }

  public UnauthorizedException(String message) {
    super(message, Response.Status.UNAUTHORIZED);
  }
}
