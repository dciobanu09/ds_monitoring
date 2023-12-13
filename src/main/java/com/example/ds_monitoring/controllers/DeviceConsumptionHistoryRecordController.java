package com.example.ds_monitoring.controllers;

import com.example.ds_monitoring.dtos.GenericDTO;
import com.example.ds_monitoring.services.DeviceConsumptionHistoryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/consumptionRecords")
public class DeviceConsumptionHistoryRecordController {
    @Autowired
    DeviceConsumptionHistoryRecordService deviceConsumptionHistoryRecordService;

    @GetMapping("/getAllByDevice")
    public ResponseEntity<List<GenericDTO>> getRecordsByDeviceId(@RequestHeader(name = "device") UUID deviceId, @RequestHeader(name = "selectedDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime selectedDate) {
        return deviceConsumptionHistoryRecordService.getRecordsByDeviceId(deviceId, selectedDate);
    }
}
