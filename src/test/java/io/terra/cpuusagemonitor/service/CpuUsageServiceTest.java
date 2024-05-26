package io.terra.cpuusagemonitor.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import io.terra.cpuusagemonitor.common.enums.ResponseCode;
import io.terra.cpuusagemonitor.common.exception.BizException;
import io.terra.cpuusagemonitor.domain.CpuUsage;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageDTO;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageDTO.CpuUsageRequest;
import io.terra.cpuusagemonitor.enums.DateType;
import io.terra.cpuusagemonitor.repository.CpuUsageRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("dev")
class CpuUsageServiceTest {

    @Mock
    private CpuUsageService cpuUsageService;

    @Mock
    private CpuUsageRepository cpuUsageRepository;

    private CpuUsage cpuUsage;

    private CpuUsageRequest request;


    // 값 초기화
    @BeforeEach
    void setUp() {
        cpuUsage = CpuUsage.builder()
            .id(1L)
            .measurementValue(0.333)
            .measurementTime(LocalDateTime.now())
            .build();
    }

    @Nested
    @DisplayName("사용자 요청값 유효성 검사")
    class request_value_invalid_test {

        @Test
        @DisplayName("분 단위 API: 요청값이 최근 1주 범위 미포함시 INVALID_REQUEST_PROMOTION ResponseConde 반환. ")
        void req_not_within_week_test() {
            setCpuUsageStatsRequest(LocalDateTime.now().minusDays(9), LocalDateTime.now().minusDays(8), DateType.MINUTE);

            BizException e = assertThrows(BizException.class, () ->
                    request.getDateType()
                        .isWithinRange(request.getStartTime())
                , "범위 미포함.");

            // 예외 객체의 메시지와 ResponseCode를 확인
            assertEquals(e.getMessage(), "최근 1주 데이터만 제공됩니다.");
            assertThat(e.getResponseCode()).isEqualTo(ResponseCode.INVALID_REQUEST_TIME);

        }

        @Test
        @DisplayName("시 단위 API: 요청값이 최근 3달 범위 미포함시 INVALID_REQUEST_PROMOTION ResponseConde 반환. ")
        void req_not_within_month_test() {
            setCpuUsageStatsRequest(LocalDateTime.now().minusMonths(5), LocalDateTime.now().minusMonths(4), DateType.HOURS);

            BizException e = assertThrows(BizException.class, () ->
                    request.getDateType()
                        .isWithinRange(request.getStartTime())
                , "범위 미포함.");

            // 예외 객체의 메시지와 ResponseCode 확인
            assertEquals(e.getMessage(), "최근 3달 데이터만 제공됩니다.");
            assertThat(e.getResponseCode()).isEqualTo(ResponseCode.INVALID_REQUEST_TIME);
        }

        @Test
        @DisplayName("일 단위 API: 요청값이 최근 1년 범위 미포함시 INVALID_REQUEST_PROMOTION ResponseConde 반환. ")
        void req_not_within_year_test() {
            setCpuUsageStatsRequest(LocalDateTime.now().minusYears(3), LocalDateTime.now().minusYears(2), DateType.DAY);

            BizException e = assertThrows(BizException.class, () ->
                    request.getDateType()
                        .isWithinRange(request.getStartTime())
                , "범위 미포함.");

            // 예외 객체의 메시지와 ResponseCode 확인
            assertEquals(e.getMessage(), "최근 1년 데이터만 제공됩니다.");
            assertThat(e.getResponseCode()).isEqualTo(ResponseCode.INVALID_REQUEST_TIME);
        }
    }


    @Test
    @DisplayName("지정한 시간 구간의 CPU 사용량 통계 조회 테스트")
    void cpu_usage_find_test_with_hours() {

        setCpuUsageStatsRequest(LocalDateTime.now().minusDays(2), LocalDateTime.now(), DateType.MINUTE);

        // CPUUsageRepository의 findByTimestampBetween 메서드가 호출될 때 해당 CPU 사용률 데이터 리스트를 리턴하도록 설정
        when(cpuUsageRepository.findAllByMeasurementTimeBetween(any(), any()))
            .thenReturn(
                setCpuUsageList(LocalDateTime.now(), null)
                .stream()
                .filter(c -> !c.getMeasurementTime().isBefore(request.getStartTime()) && !c.getMeasurementTime().isAfter(request.getEndTime()))
                    .collect(Collectors.toList()));

        List<CpuUsageDTO> result = cpuUsageService.getCpuUsagesByTimeRange(request);
        result.forEach(c -> {
            assertTrue(c.getMeasurementTime().isAfter(request.getEndTime()) && c.getMeasurementTime().isBefore(request.getStartTime()));
        });

    }

    private void setCpuUsageStatsRequest(LocalDateTime startTime, LocalDateTime endTime, DateType dateType) {
        request = CpuUsageRequest.builder()
            .startTime(startTime)
            .endTime(endTime)
            .dateType(dateType)
            .build();
    }

    private List<CpuUsage> setCpuUsageList(LocalDateTime startTime, Double usage) {

        List<CpuUsage> cpuUsageList = new ArrayList<>();

        for(int i=0; i<5; i++){
            CpuUsage cpuUsageTest = cpuUsage;
            if(usage !=null){
                cpuUsageTest.setMeasurementValue(usage * (i+1));
            }else{
                cpuUsageTest.setMeasurementTime(startTime.minusDays(i));
            }
            cpuUsageList.add(cpuUsageTest);
        }
        return cpuUsageList;
    }
}

