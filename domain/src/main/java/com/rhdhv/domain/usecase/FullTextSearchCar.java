package com.rhdhv.domain.usecase;

import org.springframework.data.domain.Pageable;

public record FullTextSearchCar(String text, Pageable pageRequest) {

  public boolean isEmpty() {
    return this.text == null || this.text.isEmpty() || this.text.isBlank();
  }
}
