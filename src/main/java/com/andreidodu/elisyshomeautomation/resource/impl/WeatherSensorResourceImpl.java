package com.andreidodu.elisyshomeautomation.resource.impl;

import com.andreidodu.elisyshomeautomation.dto.DeviceDTO;
import com.andreidodu.elisyshomeautomation.dto.common.SensorRequestCommonDTO;
import com.andreidodu.elisyshomeautomation.dto.request.SensorConfigurationRequestDTO;
import com.andreidodu.elisyshomeautomation.dto.request.WeatherByDateIntervalRequestDTO;
import com.andreidodu.elisyshomeautomation.dto.request.WeatherByDateRequestDTO;
import com.andreidodu.elisyshomeautomation.dto.response.WeatherDTO;
import com.andreidodu.elisyshomeautomation.dto.response.WeatherSensorConfigurationDTO;
import com.andreidodu.elisyshomeautomation.dto.response.WeatherSummaryDTO;
import com.andreidodu.elisyshomeautomation.entity.DeviceType;
import com.andreidodu.elisyshomeautomation.resource.WeatherSensorResource;
import com.andreidodu.elisyshomeautomation.service.DeviceService;
import com.andreidodu.elisyshomeautomation.service.WeatherSensorService;
import com.andreidodu.elisyshomeautomation.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class WeatherSensorResourceImpl implements WeatherSensorResource {

    @Value("${app.configuration.default.max.chart.elements.24h}")
    private int maxChartElements24h;

    final private WeatherSensorService weatherSensorService;
    final private DeviceService deviceService;


    @Override
    public ResponseEntity<WeatherSensorConfigurationDTO> getConfiguration(SensorConfigurationRequestDTO configurationRequestDTO) {
        return ResponseEntity.ok(this.weatherSensorService.getConfiguration(configurationRequestDTO));
    }

    @Override
    public ResponseEntity<WeatherDTO> submitNew(WeatherDTO weatherDTO) {
        return ResponseEntity.ok(this.weatherSensorService.insert(weatherDTO));
    }

    @Override
    public ResponseEntity<WeatherDTO> getLast(SensorRequestCommonDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.getLast(dto.getMacAddress()));
    }

    @Override
    public ResponseEntity<List<WeatherDTO>> getAllByDate(@RequestBody WeatherByDateRequestDTO dto) {
        Date startDate = DateUtil.calculateStartDate(dto.getDate());
        Date endDate = DateUtil.calculateEndDate(dto.getDate());
        return ResponseEntity.ok(this.weatherSensorService.getAllByDate(dto.getMacAddress(), startDate, endDate, Optional.of(maxChartElements24h)));
    }

    @Override
    public ResponseEntity<WeatherDTO> calculateAverageByDate(@RequestBody WeatherByDateRequestDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.calculateAverageByDate(dto.getMacAddress(), dto.getDate()));
    }

    @Override
    public ResponseEntity<WeatherDTO> calculateAverageByDateInterval(@RequestBody WeatherByDateIntervalRequestDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.calculateAverageByIntervalDate(dto.getMacAddress(), dto.getDateStart(), dto.getDateEnd()));
    }

    @Override
    public ResponseEntity<WeatherDTO> getMinimumTemperatureByDate(WeatherByDateRequestDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.getMinimumTemperature(dto.getMacAddress(), dto.getDate()));
    }

    @Override
    public ResponseEntity<WeatherDTO> getMaximumTemperatureByDate(WeatherByDateRequestDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.getMaximumTemperature(dto.getMacAddress(), dto.getDate()));
    }

    @Override
    public ResponseEntity<WeatherDTO> getTodayMinimumTemperature(SensorRequestCommonDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.getMinimumTemperature(dto.getMacAddress(), new Date()));
    }

    @Override
    public ResponseEntity<WeatherDTO> getTodayMaximumTemperature(SensorRequestCommonDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.getMaximumTemperature(dto.getMacAddress(), new Date()));
    }

    @Override
    public ResponseEntity<WeatherDTO> getTodayMinimumHumidity(SensorRequestCommonDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.getMinimumHumidity(dto.getMacAddress(), new Date()));
    }

    @Override
    public ResponseEntity<WeatherDTO> getTodayMaximumHumidity(SensorRequestCommonDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.getMaximumHumidity(dto.getMacAddress(), new Date()));
    }

    @Override
    public ResponseEntity<WeatherDTO> getMinimumHumidityByDate(WeatherByDateRequestDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.getMinimumHumidity(dto.getMacAddress(), dto.getDate()));
    }

    @Override
    public ResponseEntity<WeatherDTO> getMaximumHumidityByDate(WeatherByDateRequestDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.getMaximumHumidity(dto.getMacAddress(), dto.getDate()));
    }

    @Override
    public ResponseEntity<WeatherSummaryDTO> getTodaySummary(WeatherByDateRequestDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.retrieveTodaySummary(dto.getMacAddress()));
    }

    @Override
    public ResponseEntity<WeatherSummaryDTO> getYesterdayDaySummary(WeatherByDateRequestDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.retrieveYesterdayDaySummary(dto.getMacAddress()));
    }

    @Override
    public ResponseEntity<WeatherSummaryDTO> getLastNightSummary(WeatherByDateRequestDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.retrieveLastNightSummary(dto.getMacAddress()));
    }

    @Override
    public ResponseEntity<List<DeviceDTO>> getWeatherStations() {
        return ResponseEntity.ok(this.deviceService.retrieveDevicesByType(DeviceType.WeatherStation));
    }

    @Override
    public ResponseEntity<List<WeatherDTO>> getLast24h(SensorRequestCommonDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.getLast24h(dto));
    }

    @Override
    public ResponseEntity<WeatherSummaryDTO> getLast24hSummary(WeatherByDateRequestDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.getLast24hSummary(dto));
    }

    @Override
    public ResponseEntity<List<WeatherDTO>> getLast7d(SensorRequestCommonDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.getLast7d(dto));
    }

    @Override
    public ResponseEntity<WeatherSummaryDTO> getLast7dSummary(WeatherByDateRequestDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.getLast7dSummary(dto));
    }

    @Override
    public ResponseEntity<List<WeatherDTO>> getLastMonth(SensorRequestCommonDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.getLast1m(dto));
    }

    @Override
    public ResponseEntity<WeatherSummaryDTO> getLastMonthSummary(WeatherByDateRequestDTO dto) {
        return ResponseEntity.ok(this.weatherSensorService.getLast1mSummary(dto));
    }

}
