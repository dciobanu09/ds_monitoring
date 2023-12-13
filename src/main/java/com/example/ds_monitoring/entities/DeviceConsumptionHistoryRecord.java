package com.example.ds_monitoring.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "device_consumption_history_record")
@Getter
@Setter
public class DeviceConsumptionHistoryRecord {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "read_value")
    private Double readValue;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private Device device;
}
