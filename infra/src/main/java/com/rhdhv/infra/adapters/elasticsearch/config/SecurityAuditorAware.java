package com.rhdhv.infra.adapters.elasticsearch.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;

public class SecurityAuditorAware implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {

    return Optional.of("SYSTEM_USER");
  }
}

