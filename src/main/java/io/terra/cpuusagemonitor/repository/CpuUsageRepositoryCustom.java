package io.terra.cpuusagemonitor.repository;


import io.terra.cpuusagemonitor.domain.dto.CpuUsageDTO.CpuUsageRequest;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageStatsDTO;
import java.util.List;

public interface CpuUsageRepositoryCustom {

    // CPU 최소/최대/평균 사용률 조회
    List<CpuUsageStatsDTO> getCpuUsageStats(CpuUsageRequest request);
}
