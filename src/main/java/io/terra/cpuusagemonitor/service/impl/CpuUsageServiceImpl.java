package io.terra.cpuusagemonitor.service.impl;


import io.terra.cpuusagemonitor.common.enums.ResponseCode;
import io.terra.cpuusagemonitor.common.exception.BizException;
import io.terra.cpuusagemonitor.common.util.DateUtil;
import io.terra.cpuusagemonitor.domain.CpuUsage;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageDTO;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageDTO.CpuUsageRequest;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageStatsDTO;
import io.terra.cpuusagemonitor.repository.CpuUsageRepository;
import io.terra.cpuusagemonitor.service.CpuUsageService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CpuUsageServiceImpl implements CpuUsageService {

    private final CpuUsageRepository repository;
    private final ModelMapper mapper;


    @Override
    public List<CpuUsageDTO> getCpuUsagesByTimeRange(CpuUsageRequest request) {
        //1. 요청값 검사: StartTime이 endTime이전인지 여부
        if(DateUtil.isStartTimeAfterEndTime(request.getStartTime(), request.getEndTime())){
            throw new BizException(ResponseCode.INVALID_REQUEST);
        }

        //2. 요청값 검사: 기준범위 내 요청값 포함여부 검사
        request.getDateType().isWithinRange(request.getStartTime());
        //3. 요청값 포맷 및 endTime 변경
        request.transformAndFormatDateTime();
        //4. DB 조회
        return repository.findAllByMeasurementTimeBetween(
                request.getStartTime(),
                request.getEndTime())
            .stream()
            .map(entity -> mapper.map(entity, CpuUsageDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<CpuUsageStatsDTO> getCpuUsageStats(CpuUsageRequest request) {
        //1. 요청값 검사: StartTime이 endTime이전인지 여부
        if(DateUtil.isStartTimeAfterEndTime(request.getStartTime(), request.getEndTime())) {
            throw new BizException(ResponseCode.INVALID_REQUEST);
        }

        //2. 요청값 검사: 기준범위 내 요청값 포함여부 검사
        request.getDateType().isWithinRange(request.getStartTime());
        //3. 요청값 포맷 및 endTime 변경
        request.transformAndFormatDateTime();
        //4. DB 조회
        return repository.getCpuUsageStats(request);
    }


    @Override
    public void add(CpuUsage cpuUsage) {
        try {
            repository.save(cpuUsage);
        } catch (Exception e) {
            log.info(cpuUsage.toString());
            // 예외 처리
            log.info("데이터 수집 중 오류 발생: " + e.getMessage());
        }

    }
}
