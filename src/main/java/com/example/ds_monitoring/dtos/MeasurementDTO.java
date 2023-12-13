package com.example.ds_monitoring.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MeasurementDTO {
    @JsonProperty("device_id")
    private UUID deviceId;
    @JsonProperty("measurement_value")
    private double measurementValue;
    @JsonProperty("is_last_reading_of_current_hour")
    private boolean lastReadingOfTheHour;
    private String timestamp;
}
