package io.terra.cpuusagemonitor.controller.doc;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.terra.cpuusagemonitor.common.dto.ErrorResponse;
import io.terra.cpuusagemonitor.common.dto.Response;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageDTO;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageDTO.CpuUsageRequest;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageStatsDTO;
import java.util.List;

@Tag(name = "CPU 사용률", description = "CPU 사용률 조회 API")
public interface CpuUsageControllerDoc {

    @Operation(
        summary = "CPU 사용률 조회",
        description = "지정한 시간 구간의 분 단위 CPU 사용률 조회",
        tags = {"CPU 사용률"})
    @Parameters(value = {
        @Parameter(name = "startTime", description = "조회 시작 시간", required = true, in = QUERY),
        @Parameter(name = "endTime", description = "조회 종료 시간", required = true, in = QUERY),
        @Parameter(name = "dateType", description = "조회 타입", required = true, in = QUERY,
            schema = @Schema(type = "string", allowableValues = {"MINUTE", "HOURS", "DAY"}))})
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "2000", description = "성공"),
            @ApiResponse(responseCode = "4XXX~6XXX", description = "실패",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
    Response<List<CpuUsageDTO>> getMinuteCpuUsages(@Parameter(hidden = true) CpuUsageRequest request);

    @Operation(
        summary = "CPU 사용률 통계 조회",
        description = "지정한 구간의 CPU 최소/최대/평균 사용률 조회",
        tags = {"CPU 사용률"})
    @Parameters(value = {
        @Parameter(name = "startTime", description = "시작날짜", required = true, in = QUERY),
        @Parameter(name = "endTime", description = "조회 종료 시간", required = true, in = QUERY),
        @Parameter(name = "dateType", description = "조회 타입", required = true, in = QUERY,
            schema = @Schema(type = "string", allowableValues = {"MINUTE", "HOURS", "DAY"}))
    })
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "2000", description = "성공"),
            @ApiResponse(responseCode = "4XXX~6XXX", description = "실패",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
    Response<List<CpuUsageStatsDTO>> getDailyCpuUsageStats(@Parameter(hidden = true) CpuUsageRequest request);

}
