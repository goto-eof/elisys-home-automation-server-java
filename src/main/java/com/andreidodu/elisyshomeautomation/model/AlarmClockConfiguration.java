package com.andreidodu.elisyshomeautomation.model;

import com.andreidodu.elisyshomeautomation.model.common.ModelCommon;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Table(name = "ha_alarm_clock_configuration")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AlarmClockConfiguration extends ModelCommon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "timezone_seconds", nullable = false)
    private Long timezoneSeconds;

    @Column(name = "i_am_alive_endpoint", nullable = false)
    private String iAmAliveEndpoint;

    @Column(name = "i_am_alive_interval_seconds", nullable = false)
    private Long iAmAliveIntervalSeconds;

    @Column(name = "alarm_interval_minutes", nullable = false)
    private Integer alarmIntervalMinutes;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private Device device;

    @OneToMany(mappedBy = "configuration")
    private List<AlarmClockConfigurationCron> cronList;
    @Override
    public String toString() {
        return "AlarmClockConfiguration{" +
                "id=" + id +
                ", timezoneSeconds=" + timezoneSeconds +
                //", iAmAliveEndpoint='" + iAmAliveEndpoint + '\'' +
                //", iAmAliveIntervalSeconds=" + iAmAliveIntervalSeconds +
                ", alarmIntervalSeconds=" + alarmIntervalMinutes +
                ", device=" + device.getMacAddress() +
                '}';
    }
}