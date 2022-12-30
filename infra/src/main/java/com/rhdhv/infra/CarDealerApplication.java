package com.rhdhv.infra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.rhdhv")
@SpringBootApplication
public class CarDealerApplication {

  public static void main(final String[] args) {
    SpringApplication.run(CarDealerApplication.class, args);
  }
}
