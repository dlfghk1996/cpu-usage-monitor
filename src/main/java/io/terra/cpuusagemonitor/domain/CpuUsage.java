package io.terra.cpuusagemonitor.domain;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class CpuUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "measurement_value")
    private Double measurementValue; //cpu 사용량 측정값

    @Column(name = "measurement_time")
    private LocalDateTime measurementTime; //측정 시간
}
