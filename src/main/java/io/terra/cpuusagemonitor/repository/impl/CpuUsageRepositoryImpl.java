package io.terra.cpuusagemonitor.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.terra.cpuusagemonitor.domain.QCpuUsage;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageDTO.CpuUsageRequest;
import io.terra.cpuusagemonitor.domain.dto.CpuUsageStatsDTO;
import io.terra.cpuusagemonitor.enums.DateType;
import io.terra.cpuusagemonitor.repository.CpuUsageRepositoryCustom;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class CpuUsageRepositoryImpl implements CpuUsageRepositoryCustom {

    static QCpuUsage cpuUsage = QCpuUsage.cpuUsage;
    private final JPAQueryFactory queryFactory;

    @Autowired
    private DataSource dataSource;

    public StringTemplate createDateFormat(DateType dateType) {

        if (isH2Database()) {
            String h2Format = (dateType == DateType.HOURS) ? "yyyy-MM-dd HH:00" : "yyyy-MM-dd";
            return Expressions.stringTemplate("FORMATDATETIME({0},'" + h2Format + "')", cpuUsage.measurementTime);
        }

        String format = (dateType == DateType.HOURS) ? "%Y-%m-%d HH:00" : "%Y-%m-%d";
        return Expressions.stringTemplate(
            "DATE_FORMAT({0}, '" + format + "')",
            cpuUsage.measurementTime);
    }

    @Override
    public List<CpuUsageStatsDTO> getCpuUsageStats(CpuUsageRequest request) {
        StringTemplate timeTemplate = createDateFormat(request.getDateType());

        List<Tuple> results = queryFactory
            .select(timeTemplate,
                cpuUsage.measurementValue.max(),
                cpuUsage.measurementValue.min(),
                cpuUsage.measurementValue.avg())
            .from(cpuUsage)
            .where(cpuUsage.measurementTime.between(request.getStartTime(), request.getEndTime()))
            .groupBy(timeTemplate)
            .orderBy(timeTemplate.asc())
            .fetch();


        return results.stream()
            .map(tuple -> CpuUsageStatsDTO.builder()
                .measurementTime(tuple.get(timeTemplate))
                .maxUsage(tuple.get(cpuUsage.measurementValue.max()))
                .minUsage(tuple.get(cpuUsage.measurementValue.min()))
                .averageUsage(tuple.get(cpuUsage.measurementValue.avg()))
                .build())
            .collect(Collectors.toList());
    }

    private boolean isH2Database() {
        try {
            return "H2".equalsIgnoreCase(dataSource.getConnection().getMetaData().getDatabaseProductName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
