package com.rhdhv.domain.usecase;

import org.springframework.data.domain.PageRequest;

public record RetrieveCarsByFilter(Integer year, String brand, PageRequest pageRequest) {

}

