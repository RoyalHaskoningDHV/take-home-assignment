package com.rhdhv.infra.adapters.elasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.net.ssl.SSLContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.config.EnableElasticsearchAuditing;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@Configuration
@EnableElasticsearchAuditing
@EnableElasticsearchRepositories
@RequiredArgsConstructor
public class ElasticsearchConfig extends ElasticsearchConfiguration {

  private final ElasticsearchProperties elasticsearchProperties;
  private final ObjectMapper objectMapper;

  @SneakyThrows
  @Override
  public ClientConfiguration clientConfiguration() {
    final SSLContextBuilder sslBuilder = SSLContexts.custom()
        .loadTrustMaterial(null, (x509Certificates, s) -> true);

    final SSLContext sslContext = sslBuilder.build();

    return ClientConfiguration.builder()
        .connectedTo(this.elasticsearchProperties.getUris().toArray(String[]::new))
        .usingSsl(sslContext, NoopHostnameVerifier.INSTANCE)
        .withBasicAuth(this.elasticsearchProperties.getUsername(), this.elasticsearchProperties.getPassword())
        .build();
  }

  @Override
  @Bean
  public ElasticsearchClient elasticsearchClient(final RestClient restClient) {

    final ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper(this.objectMapper));

    return new ElasticsearchClient(transport);
  }

}