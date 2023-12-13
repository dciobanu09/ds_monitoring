package com.example.ds_monitoring.services;

import com.example.ds_monitoring.dtos.DeviceConsumptionHistoryRecordDTO;
import com.example.ds_monitoring.dtos.ExceptionDTO;
import com.example.ds_monitoring.dtos.GenericDTO;
import com.example.ds_monitoring.entities.Device;
import com.example.ds_monitoring.entities.DeviceConsumptionHistoryRecord;
import com.example.ds_monitoring.repositories.DeviceConsumptionHistoryRecordRepository;
import com.example.ds_monitoring.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceConsumptionHistoryRecordService {
    @Autowired
    DeviceConsumptionHistoryRecordRepository deviceConsumptionHistoryRecordRepository;
    @Autowired
    DeviceRepository deviceRepository;

    public ResponseEntity<GenericDTO> create(DeviceConsumptionHistoryRecordDTO deviceConsumptionHistoryRecordDTO) { // sa nu uit sa scot respentity
        Optional<Device> optionalDevice = deviceRepository.findById(deviceConsumptionHistoryRecordDTO.getDeviceId());
        if (optionalDevice.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO("Device not found!"));
        }
        Device device = optionalDevice.get();

        DeviceConsumptionHistoryRecord deviceConsumptionHistoryRecord = new DeviceConsumptionHistoryRecord();
        deviceConsumptionHistoryRecord.setId(deviceConsumptionHistoryRecordDTO.getId());
        deviceConsumptionHistoryRecord.setReadValue(deviceConsumptionHistoryRecordDTO.getReadValue());
        deviceConsumptionHistoryRecord.setTimestamp(deviceConsumptionHistoryRecordDTO.getTimestamp());
        deviceConsumptionHistoryRecord.setDevice(device);
//        device.getConsumptionRecords().add(deviceConsumptionHistoryRecord);//

        deviceConsumptionHistoryRecord = deviceConsumptionHistoryRecordRepository.save(deviceConsumptionHistoryRecord);

        deviceConsumptionHistoryRecordDTO.setDeviceId(deviceConsumptionHistoryRecord.getId());
        deviceConsumptionHistoryRecordDTO.setReadValue(deviceConsumptionHistoryRecord.getReadValue());
        deviceConsumptionHistoryRecordDTO.setTimestamp(deviceConsumptionHistoryRecord.getTimestamp());
        deviceConsumptionHistoryRecordDTO.setDeviceId(deviceConsumptionHistoryRecord.getDevice().getId());

        return ResponseEntity.status(HttpStatus.OK).body(deviceConsumptionHistoryRecordDTO);
    }

    public ResponseEntity<List<GenericDTO>> getRecordsByDeviceId(UUID deviceId, LocalDateTime selectedDate) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
        if (optionalDevice.isEmpty()) {
            List<GenericDTO> result = new ArrayList<>();
            result.add(new ExceptionDTO("The device was not found!"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        Device device = optionalDevice.get();

        Optional<List<DeviceConsumptionHistoryRecord>> optionalDeviceConsumptionHistoryRecordList = deviceConsumptionHistoryRecordRepository.findAllByDevice(device);
        if (optionalDeviceConsumptionHistoryRecordList.isEmpty()) {
            List<GenericDTO> result = new ArrayList<>();
            result.add(new ExceptionDTO("No records found!"));
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        List<DeviceConsumptionHistoryRecord> deviceConsumptionHistoryRecordList = optionalDeviceConsumptionHistoryRecordList.get();
        List<GenericDTO> deviceConsumptionHistoryRecordDTOs = deviceConsumptionHistoryRecordList.stream().map(record -> {
                    DeviceConsumptionHistoryRecordDTO deviceConsumptionHistoryRecordDTO = new DeviceConsumptionHistoryRecordDTO();
                    deviceConsumptionHistoryRecordDTO.setId(record.getId());
                    deviceConsumptionHistoryRecordDTO.setReadValue(record.getReadValue());
                    deviceConsumptionHistoryRecordDTO.setTimestamp(record.getTimestamp());
                    deviceConsumptionHistoryRecordDTO.setDeviceId(record.getDevice().getId());

                    return deviceConsumptionHistoryRecordDTO;
                }).filter(record -> record.getTimestamp().getDayOfMonth() == selectedDate.getDayOfMonth() && record.getTimestamp().getMonth() == selectedDate.getMonth() && selectedDate.getYear() == selectedDate.getYear())
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(deviceConsumptionHistoryRecordDTOs);
    }
}
