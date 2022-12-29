package com.rhdhv.infra;

import com.rhdhv.infra.AbstractIT.DataSourceInitializer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@ContextConfiguration(initializers = DataSourceInitializer.class)
public abstract class AbstractIT {

  @Container
  public static final ElasticsearchContainer ELASTICSEARCH_CONTAINER = new ElasticsearchContainer(
      DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch").withTag("8.5.3"));

  public static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
      TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
          applicationContext,
          "spring.elasticsearch.uris=" + ELASTICSEARCH_CONTAINER.getHttpHostAddress(),
          "spring.elasticsearch.password=" + ELASTICSEARCH_CONTAINER.getEnvMap().get("ELASTIC_PASSWORD")
      );
    }
  }
}
