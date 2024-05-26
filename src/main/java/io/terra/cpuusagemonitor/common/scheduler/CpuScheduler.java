package io.terra.cpuusagemonitor.common.scheduler;


import io.terra.cpuusagemonitor.common.config.actuator.MetricEndpoint;
import io.terra.cpuusagemonitor.domain.CpuUsage;
import io.terra.cpuusagemonitor.service.CpuUsageService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CpuScheduler {

    private final MetricsEndpoint metricsEndpoint;

    private final CpuUsageService cpuUsageService;

    @Scheduled(fixedDelay = 1000 * 60)
    public void collectCpuUsage() {
        double usage = metricsEndpoint.metric(MetricEndpoint.CPU_USAGE, null)
            .getMeasurements().get(0).getValue();

        cpuUsageService.add(
            CpuUsage.builder().measurementValue(usage)
            .measurementTime(LocalDateTime.now())
            .build());
    }
}
