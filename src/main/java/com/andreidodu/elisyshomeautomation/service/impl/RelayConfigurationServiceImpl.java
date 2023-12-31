package com.andreidodu.elisyshomeautomation.service.impl;

import com.andreidodu.elisyshomeautomation.entity.DeviceType;
import com.andreidodu.elisyshomeautomation.repository.DeviceRepository;
import com.andreidodu.elisyshomeautomation.repository.RelayConfigurationRepository;
import com.andreidodu.elisyshomeautomation.dto.request.RelayConfigurationRequestDTO;
import com.andreidodu.elisyshomeautomation.dto.response.RelayConfigurationResponseDTO;
import com.andreidodu.elisyshomeautomation.mapper.RelayConfigurationMapper;
import com.andreidodu.elisyshomeautomation.entity.Device;
import com.andreidodu.elisyshomeautomation.entity.RelayConfiguration;
import com.andreidodu.elisyshomeautomation.service.DeviceService;
import com.andreidodu.elisyshomeautomation.service.IAmAliveService;
import com.andreidodu.elisyshomeautomation.service.RelayConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RelayConfigurationServiceImpl implements RelayConfigurationService {

    private final static String DEVICE_NAME = "Relay device";

    private final RelayConfigurationRepository relayConfigurationRepository;
    private final RelayConfigurationMapper relayConfigurationMapper;
    private final DeviceRepository deviceRepository;
    final private DeviceService deviceService;
    final private IAmAliveService iAmAliveService;

    @Override
    public RelayConfigurationResponseDTO getConfiguration(RelayConfigurationRequestDTO configurationRequestDTO) {
        Optional<RelayConfiguration> configurationOptional = relayConfigurationRepository.findByDevice_MacAddress(configurationRequestDTO.getMacAddress());
        if (configurationOptional.isPresent()) {
            RelayConfigurationResponseDTO result = relayConfigurationMapper.toDTO(configurationOptional.get());
            log.info(result.toString());
            return result;
        }
        RelayConfigurationResponseDTO result = createNewConfiguration(configurationRequestDTO, this::loadDefaultConfiguration);
        log.info(result.toString());
        iAmAliveService.updateByMacAddress(configurationRequestDTO.getMacAddress());
        return result;
    }

    private RelayConfigurationResponseDTO createNewConfiguration(RelayConfigurationRequestDTO configurationRequestDTO, Function<String, RelayConfigurationResponseDTO> function) {
        Optional<Device> deviceOptional = this.deviceRepository.findByMacAddress(configurationRequestDTO.getMacAddress());
        if (deviceOptional.isEmpty()) {
            this.deviceService.createNewDevice(DeviceType.Relay, configurationRequestDTO.getMacAddress(), DEVICE_NAME, configurationRequestDTO.getMacAddress());
            deviceOptional = this.deviceRepository.findByMacAddress(configurationRequestDTO.getMacAddress());
        }
        RelayConfigurationResponseDTO dto = function.apply(configurationRequestDTO.getMacAddress());
        RelayConfiguration model = relayConfigurationMapper.toModel(dto);
        model.setDevice(deviceOptional.get());
        return this.relayConfigurationMapper.toDTO(this.relayConfigurationRepository.save(model));
    }

    @Override
    public RelayConfigurationResponseDTO switchOnOrOff(RelayConfigurationRequestDTO configurationRequestDTO, boolean switchOn) {
        Optional<RelayConfiguration> configurationOptional = relayConfigurationRepository.findByDevice_MacAddress(configurationRequestDTO.getMacAddress());
        if (configurationOptional.isPresent()) {
            RelayConfiguration relayConfiguration = configurationOptional.get();
            relayConfiguration.setPowerOn(switchOn);
            RelayConfiguration newRelayConfiguration = this.relayConfigurationRepository.save(relayConfiguration);
            iAmAliveService.updateByMacAddress(configurationRequestDTO.getMacAddress());
            return relayConfigurationMapper.toDTO(newRelayConfiguration);
        }
        RelayConfigurationResponseDTO configuration = createNewConfiguration(configurationRequestDTO, (String macAddress) -> loadDefaultConfiguration(macAddress, switchOn));
        iAmAliveService.updateByMacAddress(configurationRequestDTO.getMacAddress());
        return configuration;
    }

    private RelayConfigurationResponseDTO loadDefaultConfiguration(String macAddress) {
        RelayConfigurationResponseDTO dto = new RelayConfigurationResponseDTO();
        dto.setMacAddress(macAddress);
        dto.setPowerOn(false);
        return dto;
    }

    private RelayConfigurationResponseDTO loadDefaultConfiguration(String macAddress, boolean powerOn) {
        RelayConfigurationResponseDTO dto = new RelayConfigurationResponseDTO();
        dto.setMacAddress(macAddress);
        dto.setPowerOn(powerOn);
        return dto;
    }


}
