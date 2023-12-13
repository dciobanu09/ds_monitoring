package com.example.ds_monitoring.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "device")
@Getter
@Setter
public class Device {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "max_hourly_energy_consumption")
    private Double maxHourlyEnergyConsumption;

    @Column(name = "total_hourly_energy_consumption")
    private Double totalHourlyEnergyConsumption;

    @Column(name = "owner_id")
    private UUID ownerId;

    @OneToMany(mappedBy = "device")
    private List<DeviceConsumptionHistoryRecord> consumptionRecords;
}



