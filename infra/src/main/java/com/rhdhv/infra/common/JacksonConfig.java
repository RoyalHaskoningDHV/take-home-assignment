package com.rhdhv.infra.common;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.TimeZone;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
    return new JacksonCustomizer();
  }

  private static final class JacksonCustomizer implements Jackson2ObjectMapperBuilderCustomizer, Ordered {

    @Override
    public void customize(final Jackson2ObjectMapperBuilder builder) {
      builder
          .modules(new JavaTimeModule())
          .modules(new Jdk8Module())
          .modules(new JSR310Module())
          .timeZone(TimeZone.getDefault())
          .serializationInclusion(Include.NON_NULL)
          .featuresToDisable(
              MapperFeature.DEFAULT_VIEW_INCLUSION,
              DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
              DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE,
              DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS,
              SerializationFeature.INDENT_OUTPUT
          );
    }

    @Override
    public int getOrder() {
      return 1; // just after Spring's own
    }
  }
}
