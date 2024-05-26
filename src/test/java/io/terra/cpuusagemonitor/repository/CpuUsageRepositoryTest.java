package io.terra.cpuusagemonitor.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.terra.cpuusagemonitor.common.config.db.QueryDslConfig;
import io.terra.cpuusagemonitor.domain.CpuUsage;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
@DataJpaTest
@Transactional
@Import(QueryDslConfig.class)
public class CpuUsageRepositoryTest {
    @Autowired
    private CpuUsageRepository cpuUsageRepository;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("CPU 사용량 저장 테스트")
    void save_find_test() {
        CpuUsage cpuUsage = CpuUsage.builder()
            .measurementValue(10.0)
            .measurementTime(LocalDateTime.now()).build();

        cpuUsageRepository.save(cpuUsage);

        assertNotNull(cpuUsage.getId());
        assertTrue(cpuUsage.getId() > 0);
        cpuUsageRepository.deleteById(cpuUsage.getId());
    }

    @Test
    @DisplayName("CPU 사용량 통계 조회 테스트")
    public void find_cpu_usage_stats_test() {
        // given
        List<CpuUsage> list = Arrays.asList(
            new CpuUsage(2000L, 10.0, LocalDateTime.now().plusDays(3)),
            new CpuUsage(2001L, 20.0, LocalDateTime.now().plusDays(4)),
            new CpuUsage(2002L, 80.0, LocalDateTime.now().plusDays(4)),
            new CpuUsage(2003L, 30.0, LocalDateTime.now().plusDays(5)));

        cpuUsageRepository.saveAll(list);

        Object[] firstRow = this.testQueryWithDateFormatForH2(LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4)).get(0);

        assertThat(list).isNotNull();
        assertThat((double) firstRow[1]).isEqualTo(80.0);
        assertThat((double) firstRow[2]).isEqualTo(20.0);
        assertThat((double) firstRow[3]).isEqualTo(50.0);

        cpuUsageRepository.deleteAll(list);
    }

    List<Object[]> testQueryWithDateFormatForH2(LocalDateTime startTime, LocalDateTime endTime) {
        // H2에서 사용할 쿼리
        String query = ""
            + "SELECT FORMATDATETIME(measurement_time, 'yyyy-MM-dd'), "
            + "MAX(measurement_value), "
            + "MIN(measurement_value), "
            + "AVG(measurement_value) " +
            "FROM cpu_usage " +
            "WHERE measurement_time BETWEEN ? AND ? " +
            "GROUP BY FORMATDATETIME(measurement_time, 'yyyy-MM-dd')  " +
            "ORDER BY FORMATDATETIME(measurement_time, 'yyyy-MM-dd')";

        // 테스트에 사용될 쿼리 실행
        List<Object[]> results = jdbcTemplate.query(query, new Object[]{startTime, endTime}, (rs, rowNum) ->
            new Object[]{
                rs.getString(1),
                rs.getDouble(2),
                rs.getDouble(3),
                rs.getDouble(4)
        });
        return results;
    }
}
