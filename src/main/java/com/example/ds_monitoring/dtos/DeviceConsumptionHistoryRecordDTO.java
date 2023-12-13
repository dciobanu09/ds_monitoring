package com.example.ds_monitoring.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeviceConsumptionHistoryRecordDTO implements GenericDTO {
    private UUID id;
    private Double readValue;
    private LocalDateTime timestamp;
    private UUID deviceId;
}
