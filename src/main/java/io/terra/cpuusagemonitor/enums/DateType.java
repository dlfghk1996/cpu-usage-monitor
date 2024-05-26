package io.terra.cpuusagemonitor.enums;


import io.terra.cpuusagemonitor.common.enums.ResponseCode;
import io.terra.cpuusagemonitor.common.exception.BizException;
import io.terra.cpuusagemonitor.common.util.DateUtil;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Consumer;

public enum DateType {
  // 분 단위 조회
  MINUTE((requestTime) -> {
      if (!DateUtil.isWithinRange(requestTime, ChronoUnit.WEEKS, 1)) {
          throw new BizException(ResponseCode.INVALID_REQUEST_TIME, "최근 1주 데이터만 제공됩니다.");
      }
  }),

  // 시 단위 조회
  HOURS((requestTime) -> {
    if (!DateUtil.isWithinRange(requestTime, ChronoUnit.MONTHS, 3)) {
      throw new BizException(ResponseCode.INVALID_REQUEST_TIME, "최근 3달 데이터만 제공됩니다.");
    }
  }),

  // 일 단위 조회
  DAY((requestTime) -> {
    if (!DateUtil.isWithinRange(requestTime, ChronoUnit.YEARS, 1)) {
      throw new BizException(ResponseCode.INVALID_REQUEST_TIME, "최근 1년 데이터만 제공됩니다.");
    }
  });

  private final Consumer<LocalDateTime> isWithinRange;

  DateType(Consumer<LocalDateTime> isWithinRange) {
    this.isWithinRange = isWithinRange;
  }

  public void isWithinRange(LocalDateTime requestTime) {
     isWithinRange.accept(requestTime);
  }
}
