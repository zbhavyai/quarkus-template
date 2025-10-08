package io.github.zbhavyai.quarkustemplate.api.rest.common;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class LoggingFilter implements ContainerRequestFilter {

  private static final org.jboss.logging.Logger LOG = Logger.getLogger(LoggingFilter.class);

  @Override
  public void filter(ContainerRequestContext context) {
    LOG.debugf("Request: %s at '%s'", context.getMethod(), context.getUriInfo().getPath());
  }
}
