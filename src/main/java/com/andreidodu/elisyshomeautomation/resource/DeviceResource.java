package com.andreidodu.elisyshomeautomation.resource;

import com.andreidodu.elisyshomeautomation.dto.DeviceDTO;
import com.andreidodu.elisyshomeautomation.dto.common.SensorRequestCommonDTO;
import com.andreidodu.elisyshomeautomation.dto.request.DeviceRegistrationDTO;
import com.andreidodu.elisyshomeautomation.dto.response.ResponseStatusDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/device")
public interface DeviceResource {
    @Operation(summary = "Device registration")
    @PostMapping(value = "/register")
    ResponseEntity<ResponseStatusDTO> register(@RequestBody DeviceRegistrationDTO deviceRegistrationDTO);

    @Operation(summary = "Retrieve device by macAddress")
    @PostMapping(value = "/device")
    ResponseEntity<DeviceDTO> getDevice(@RequestBody SensorRequestCommonDTO sensorRequestCommonDTO);

    @Operation(summary = "Updates Device")
    @PutMapping(value = "/id/{id}")
    ResponseEntity<DeviceDTO> update(@PathVariable Long id, @RequestBody DeviceDTO dto);

}
