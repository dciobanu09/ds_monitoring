package com.example.ds_monitoring.services;

import com.example.ds_monitoring.dtos.DeviceDTO;
import com.example.ds_monitoring.dtos.ExceptionDTO;
import com.example.ds_monitoring.dtos.GenericDTO;
import com.example.ds_monitoring.entities.Device;
import com.example.ds_monitoring.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;

    public GenericDTO create(DeviceDTO deviceDTO) {
        Device device = new Device();
        device.setId(deviceDTO.getId());
        device.setMaxHourlyEnergyConsumption(deviceDTO.getMaxHourlyEnergyConsumption());
        device.setOwnerId(deviceDTO.getUserId());

        device = deviceRepository.save(device);

        deviceDTO.setId(device.getId());
        deviceDTO.setMaxHourlyEnergyConsumption(device.getMaxHourlyEnergyConsumption());

        return deviceDTO;
    }

    public GenericDTO update(DeviceDTO deviceDTO) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceDTO.getId());
        if (optionalDevice.isEmpty()) {
            return new ExceptionDTO("Device not found!");
        }
        Device device = optionalDevice.get();
        device.setMaxHourlyEnergyConsumption(deviceDTO.getMaxHourlyEnergyConsumption());
        device.setTotalHourlyEnergyConsumption(deviceDTO.getTotalHourlyEnergyConsumption());
        device.setOwnerId(deviceDTO.getUserId());

        device = deviceRepository.save(device); // sa nu fie bai de aici

        deviceDTO.setId(device.getId());
        deviceDTO.setMaxHourlyEnergyConsumption(device.getMaxHourlyEnergyConsumption());
        return deviceDTO;
    }

    public GenericDTO delete(DeviceDTO deviceDTO) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceDTO.getId());
        if (optionalDevice.isEmpty()) {
            return new ExceptionDTO("Device not found!");
        }
        Device foundDevice = optionalDevice.get();

        deviceRepository.delete(foundDevice);

        return deviceDTO;
    }

    public Device getDeviceById(UUID deviceId) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
        if (optionalDevice.isEmpty()) {
            return null;
        }
        return optionalDevice.get();
    }
}

