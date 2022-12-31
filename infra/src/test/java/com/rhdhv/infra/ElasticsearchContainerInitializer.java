package com.rhdhv.infra;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public class ElasticsearchContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Container
  public static final ElasticsearchContainer ELASTICSEARCH_CONTAINER = new ElasticsearchContainer(
      DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch").withTag("8.5.3"));


  @Override
  public void initialize(final ConfigurableApplicationContext configurableApplicationContext) {
    ELASTICSEARCH_CONTAINER.start();

    configurableApplicationContext
        .addApplicationListener((ApplicationListener<ContextClosedEvent>) event -> ELASTICSEARCH_CONTAINER.stop());

    TestPropertyValues
        .of("spring.elasticsearch.uris=" + ELASTICSEARCH_CONTAINER.getHttpHostAddress())
        .and("spring.elasticsearch.password=" + ELASTICSEARCH_CONTAINER.getEnvMap().get("ELASTIC_PASSWORD"))
        .applyTo(configurableApplicationContext.getEnvironment());
  }
}