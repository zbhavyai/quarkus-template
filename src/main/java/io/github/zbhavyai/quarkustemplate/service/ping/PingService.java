package io.github.zbhavyai.quarkustemplate.service.ping;

import io.smallrye.mutiny.Uni;

public interface PingService {

  Uni<String> ping();
}
