package io.terra.cpuusagemonitor.controller;

import io.terra.cpuusagemonitor.common.dto.Response;
import io.terra.cpuusagemonitor.common.enums.ResponseCode;
import io.terra.cpuusagemonitor.controller.doc.CpuUsageControllerDoc;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageDTO;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageStatsDTO;
import io.terra.cpuusagemonitor.service.CpuUsageService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/cpu-usage")
public class CpuUsageController implements CpuUsageControllerDoc {
    private final CpuUsageService service;

    /**
     * 지정한 시간 구간의 분 단위 CPU 사용률 조회
     *
     * @param request (시작 기간, 종료 기간, 조회기준타입)
     * @return cpu 사용량 리스트
     */
    @GetMapping("/list")
    public Response<List<CpuUsageDTO>> getMinuteCpuUsages(@Valid CpuUsageDTO.CpuUsageRequest request) {

        return new Response<>(service.getCpuUsagesByTimeRange(request), ResponseCode.OK);
    }


    /**
     * 지정한 날짜의 시 또는 일 단위 CPU 최소/최대/평균 사용률 조회
     *
     * @param request (시작 기간, 종료 기간, 조회기준타입)
     * @return CPU 사용량 통계
     */
    @GetMapping("/stats")
    public Response<List<CpuUsageStatsDTO>> getDailyCpuUsageStats(@Valid CpuUsageDTO.CpuUsageRequest request) {
        return new Response<>(service.getCpuUsageStats(request), ResponseCode.OK);
    }

}
