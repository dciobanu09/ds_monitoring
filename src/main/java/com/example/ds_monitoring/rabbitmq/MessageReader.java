package com.example.ds_monitoring.rabbitmq;

import com.example.ds_monitoring.constants.Modes;
import com.example.ds_monitoring.dtos.DeviceConsumptionHistoryRecordDTO;
import com.example.ds_monitoring.dtos.DeviceDTO;
import com.example.ds_monitoring.dtos.MeasurementDTO;
import com.example.ds_monitoring.entities.Device;
import com.example.ds_monitoring.services.DeviceConsumptionHistoryRecordService;
import com.example.ds_monitoring.services.DeviceService;
import com.example.ds_monitoring.websockets.WebSocketSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MessageReader {
    @Autowired
    DeviceService deviceService;
    @Autowired
    DeviceConsumptionHistoryRecordService deviceConsumptionHistoryRecordService;
    @Autowired
    private WebSocketSender webSocketSender;

    private Double currentTotalPerHour = 0.0;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "testqueue")
    public void listen(String message) {
        System.out.println(message);

        try {
            MeasurementDTO measurementDTO = objectMapper.readValue(message, MeasurementDTO.class);
            System.out.println("Received object: " + measurementDTO);

            verifyConsumption(measurementDTO);
        } catch (IOException e) {
            System.err.println("Error parsing to DTO: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "deviceUpdatesQueue")
    public void listenDeviceUpdates(String message) {
        System.out.println(message);
        try {
            DeviceDTO deviceDTO = objectMapper.readValue(message, DeviceDTO.class);
            System.out.println("Received object: " + deviceDTO);

            switch (deviceDTO.getMode()) {
                case Modes.CREATE -> deviceService.create(deviceDTO);
                case Modes.UPDATE -> deviceService.update(deviceDTO);
                case Modes.REMOVE -> deviceService.delete(deviceDTO);
            }

        } catch (IOException e) {
            System.err.println("Error parsing to DTO: " + e.getMessage());
        }

    }

    private void verifyConsumption(MeasurementDTO measurementDTO) {
        currentTotalPerHour += measurementDTO.getMeasurementValue();
        if (measurementDTO.isLastReadingOfTheHour()) {
            Device device = deviceService.getDeviceById(measurementDTO.getDeviceId());
            DeviceDTO deviceDTO = new DeviceDTO();
            deviceDTO.setId(device.getId());
            deviceDTO.setMaxHourlyEnergyConsumption(device.getMaxHourlyEnergyConsumption());
            deviceDTO.setTotalHourlyEnergyConsumption(currentTotalPerHour);
            deviceDTO.setUserId(device.getOwnerId());
            deviceService.update(deviceDTO);
            currentTotalPerHour = 0.0;

            DeviceConsumptionHistoryRecordDTO deviceConsumptionHistoryRecordDTO = new DeviceConsumptionHistoryRecordDTO();
            deviceConsumptionHistoryRecordDTO.setTimestamp(LocalDateTime.parse(measurementDTO.getTimestamp(), formatter));
            deviceConsumptionHistoryRecordDTO.setReadValue(measurementDTO.getMeasurementValue());
            deviceConsumptionHistoryRecordDTO.setDeviceId(measurementDTO.getDeviceId());

            deviceConsumptionHistoryRecordService.create(deviceConsumptionHistoryRecordDTO);

            if (device.getMaxHourlyEnergyConsumption() > measurementDTO.getMeasurementValue()) {
                System.out.println("greater");
                webSocketSender.sendMessageToUser(device.getOwnerId().toString(), "Your device with the id: " + device.getId() + " consumed more than the max threshold at: " + '\n' + measurementDTO.getTimestamp());
            } else {
                System.out.println("less");
            }
        }
    }
}
