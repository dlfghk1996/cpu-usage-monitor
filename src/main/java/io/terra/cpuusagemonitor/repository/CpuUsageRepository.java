package io.terra.cpuusagemonitor.repository;

import io.terra.cpuusagemonitor.domain.CpuUsage;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CpuUsageRepository extends JpaRepository<CpuUsage, Long>, CpuUsageRepositoryCustom {

    // 분 단위 cpu 사용률 조회
    List<CpuUsage> findAllByMeasurementTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
