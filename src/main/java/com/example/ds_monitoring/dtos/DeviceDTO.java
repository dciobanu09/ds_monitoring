package com.example.ds_monitoring.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeviceDTO implements GenericDTO {
    private UUID id;
    private Double maxHourlyEnergyConsumption;
    private Double totalHourlyEnergyConsumption;
    private String mode;
    private UUID userId;
}
