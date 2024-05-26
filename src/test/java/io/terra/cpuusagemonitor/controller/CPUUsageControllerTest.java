package io.terra.cpuusagemonitor.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageDTO.CpuUsageRequest;
import io.terra.cpuusagemonitor.service.CpuUsageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
@WebMvcTest(CpuUsageController.class)
class CPUUsageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CpuUsageService cpuUsageService;

    private CpuUsageRequest request;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("CPU 분 단위 사용률 리스트 조회 API")
    void cpu_usage_list_test() throws Exception {
        mockMvc.perform(get("/cpu-usage/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());

        verify(cpuUsageService, times(1)).getCpuUsagesByTimeRange(any());
    }

    @Test
    @DisplayName("CPU 사용률 통계 조회 API")
    void cpu_usage_stats_test() throws Exception {
        mockMvc.perform(get("/cpu-usage/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());

        verify(cpuUsageService, times(1)).getCpuUsageStats(any());
    }
}