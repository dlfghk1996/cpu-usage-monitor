package io.terra.cpuusagemonitor.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCpuUsage is a Querydsl query type for CpuUsage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCpuUsage extends EntityPathBase<CpuUsage> {

    private static final long serialVersionUID = 945911440L;

    public static final QCpuUsage cpuUsage = new QCpuUsage("cpuUsage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> measurementTime = createDateTime("measurementTime", java.time.LocalDateTime.class);

    public final NumberPath<Double> measurementValue = createNumber("measurementValue", Double.class);

    public QCpuUsage(String variable) {
        super(CpuUsage.class, forVariable(variable));
    }

    public QCpuUsage(Path<? extends CpuUsage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCpuUsage(PathMetadata metadata) {
        super(CpuUsage.class, metadata);
    }

}

