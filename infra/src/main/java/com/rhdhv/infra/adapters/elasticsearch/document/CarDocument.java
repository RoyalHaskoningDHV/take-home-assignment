package com.rhdhv.infra.adapters.elasticsearch.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rhdhv.domain.model.Car;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "cars")
public class CarDocument implements Persistable<String> {

  @Id
  private String id;

  @Field(type = FieldType.Keyword)
  private String brand;

  @Field(type = FieldType.Keyword)
  private String model;

  @Field(type = FieldType.Keyword)
  private String version;

  @Field(type = FieldType.Integer)
  private Integer releaseYear;

  @Field(type = FieldType.Double)
  private BigDecimal price;

  @Field(type = FieldType.Double)
  private BigDecimal fuelConsumption;

  @Field(type = FieldType.Double)
  private BigDecimal annualMaintenanceCost;

  @CreatedDate
  @DateTimeFormat(iso = ISO.DATE_TIME)
  @JsonFormat(pattern = "uuuuMMdd'T'HHmmss.SSSXXX")
  @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
  private Instant createdAt;

  @LastModifiedDate
  @DateTimeFormat(iso = ISO.DATE_TIME)
  @JsonFormat(pattern = "uuuuMMdd'T'HHmmss.SSSXXX")
  @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
  private Instant updatedAt;

  @JsonIgnore
  public Car toModel() {
    return Car.builder()
        .id(this.id)
        .brand(this.brand)
        .model(this.model)
        .version(this.version)
        .releaseYear(this.releaseYear)
        .price(this.price)
        .fuelConsumption(this.fuelConsumption)
        .annualMaintenanceCost(this.annualMaintenanceCost)
        .createdAt(ZonedDateTime.ofInstant(this.createdAt, ZoneId.systemDefault()))
        .updatedAt(ZonedDateTime.ofInstant(this.updatedAt, ZoneId.systemDefault()))
        .build();
  }

  @Override
  @JsonIgnore
  public boolean isNew() {
    return this.id == null;
  }
}

