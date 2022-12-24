package com.taxi.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class TripDto {
    private int orderId;
    private String driverId;
    private String comment;
    private int stars;
    private OffsetDateTime arrivalTime;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
}
