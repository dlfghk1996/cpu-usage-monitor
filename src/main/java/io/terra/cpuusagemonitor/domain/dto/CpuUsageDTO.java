package io.terra.cpuusagemonitor.domain.dto;

import static io.terra.cpuusagemonitor.enums.DateType.DAY;

import io.swagger.v3.oas.annotations.media.Schema;
import io.terra.cpuusagemonitor.enums.DateType;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "CPU 사용률 응답 DTO")
public class CpuUsageDTO {

    @Schema(description = "식별자")
    private Long id;

    @Schema(description = "CPU 사용률")
    private Double measurementValue;

    @Schema(description = "측정 시간")
    private LocalDateTime measurementTime;

    @ToString
    @Builder
    @Data
    @Schema(title = "CPU 사용률 요청 DTO")
    public static class CpuUsageRequest {
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(description = "조회 시작 시간", example = "2024-04-25T01:00:00")
        private LocalDateTime startTime;

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(description = "조회 종료 시간", example = "2024-04-25T02:00:00")
        private LocalDateTime endTime;

        @NotNull
        @Schema(description = "조회 타입", allowableValues = {"MINUTE", "HOURS", "DAY"})
        private DateType dateType;

        public void transformAndFormatDateTime(){
            ChronoUnit chronoUnit;

            if(this.dateType == DAY){
                chronoUnit = ChronoUnit.DAYS;
                this.endTime = this.endTime.plusDays(1);
            }else{
                chronoUnit = ChronoUnit.HOURS;
                this.endTime = this.endTime.plusHours(1);
            }
            this.startTime = this.startTime.truncatedTo(chronoUnit);
            this.endTime = this.endTime.truncatedTo(chronoUnit);

        }
    }
}
