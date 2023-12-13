package com.example.ds_monitoring.repositories;

import com.example.ds_monitoring.entities.Device;
import com.example.ds_monitoring.entities.DeviceConsumptionHistoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceConsumptionHistoryRecordRepository extends JpaRepository<DeviceConsumptionHistoryRecord, UUID> {
    Optional<List<DeviceConsumptionHistoryRecord>> findAllByDevice(Device device);
}
