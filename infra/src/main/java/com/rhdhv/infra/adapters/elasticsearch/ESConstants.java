package com.rhdhv.infra.adapters.elasticsearch;

public final class ESConstants {

  public static final int ELASTICSEARCH_MAX_PAGINATION_LIMIT = 10000;
  public static final String RELEASE_YEAR_OR_BRAND_QUERY_STR = "(releaseYear:%s OR brand:%s)";
  public static final String CARS_INDEX = "cars";
  public static final String BRAND = "brand";
  public static final String RELEASE_YEAR = "releaseYear";
  public static final String TOTAL_ANNUAL_COST = "totalAnnualCost";

  private ESConstants() {
  }
}
