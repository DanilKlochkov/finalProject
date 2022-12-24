package com.taxi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class OrderResponse {
    private String passenger;
    private OffsetDateTime orderTime;
    private String addressFrom;
    private String addressTo;
    private Integer cost;
    private String status;
}
