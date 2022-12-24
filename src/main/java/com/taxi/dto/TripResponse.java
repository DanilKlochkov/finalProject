package com.taxi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TripResponse {
    private Integer id;
    private String passengerName;
    private String driverName;
    private Integer cost;
    private Long spendPassengerTime;
    private Long spendDriverTime;
    private Long tripTime;
    private Integer stars;
    private String comment;
}
