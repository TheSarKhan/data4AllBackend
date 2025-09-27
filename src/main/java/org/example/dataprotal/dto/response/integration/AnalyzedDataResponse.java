package org.example.dataprotal.dto.response.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyzedDataResponse {

    private String sector;
    private String model;
    private String category;
    private Period period;
    private Statistics statistics;

    @JsonProperty("data_points")
    private List<DataPoint> dataPoints;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Period {
        @JsonProperty("start_year")
        private Integer startYear;

        @JsonProperty("end_year")
        private Integer endYear;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataPoint {
        @JsonProperty("year")
        private Integer year;

        @JsonProperty("value")
        private Double value;

        @JsonProperty("formatted_value")
        private String formattedValue;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Statistics {
        @JsonProperty("last_value")
        private Double lastValue;

        @JsonProperty("first_value")
        private Double firstValue;

        private Double mean;

        private Double median;

        private Double std;

        private Double min;

        private Double max;

        private Double range;

        private Double change;

        @JsonProperty("percent_change")
        private Double percentChange;

        private Double slope;

        @JsonProperty("r_squared")
        private Double rSquared;

        @JsonProperty("trend_direction")
        private String trendDirection;
    }
}
