package io.terra.cpuusagemonitor.service;


import io.terra.cpuusagemonitor.domain.CpuUsage;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageDTO;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageDTO.CpuUsageRequest;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageStatsDTO;
import java.util.List;

public interface CpuUsageService {

    List<CpuUsageDTO> getCpuUsagesByTimeRange(CpuUsageRequest request);

    List<CpuUsageStatsDTO> getCpuUsageStats(CpuUsageRequest request);

    void add(CpuUsage cpuUsage);
}
