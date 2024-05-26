package io.terra.cpuusagemonitor.domain.dto;

import static io.terra.cpuusagemonitor.enums.DateType.DAY;

import io.swagger.v3.oas.annotations.media.Schema;
import io.terra.cpuusagemonitor.enums.DateType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@Schema(title = "CPU 사용률 통계 응답 DTO")
public class CpuUsageStatsDTO {

    @Schema(description = "측정 시간")
    private String measurementTime;

    @Schema(description = "최대 사용률")
    private Double maxUsage;

    @Schema(description = "최소 사용률")
    private Double minUsage;

    @Schema(description = "평균 사용률")
    private Double averageUsage;
}
