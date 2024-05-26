package io.terra.cpuusagemonitor.common.util;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class DateUtil {


    /**
     * 요청일자가 기준일자 범위에 포함되는지 확인하는 메서드
     *
     * @param requestTime 요청일자
     * @param startTime 기준 시작일자
     * @param endTime 기준 종료일자
     * @return 요청일자가 기준일자 범위에 포함되면 true, 아니면 false
     */
    public static boolean isDateInRange(LocalDateTime requestTime, LocalDateTime startTime, LocalDateTime endTime) {
        if (requestTime == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException("입력된 일시는 null일 수 없습니다.");
        }
        return !requestTime.isBefore(startTime) && !requestTime.isAfter(endTime);

    }

    /**
     * 기준 범위 구하는 메서드
     *
     * @param requestTime 요청일자
     * @param unit 기준 기간 단위 (예: ChronoUnit.MONTHS, ChronoUnit.WEEKS 등)
     * @param amount 기준 기간
     * @return 기간 내에 있으면 true, 아니면 false
     */
    public static boolean isWithinRange(LocalDateTime requestTime, TemporalUnit unit, long amount) {
        if (requestTime == null) {
            throw new IllegalArgumentException("입력된 일시는 null일 수 없습니다.");
        }
        LocalDateTime startTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).minus(amount, unit);

        return isDateInRange(requestTime, startTime, LocalDateTime.now());
    }

    /**
     * 시작 시간과 종료 시간 유효 관계 검사 메서드
     *
     * @param startTime 시작 시간
     * @param endTime 종료 시간
     * @return 종료 시간이 시작 시간보다 이전 이면 true
     */
    public static boolean isStartTimeAfterEndTime(LocalDateTime startTime, LocalDateTime endTime) {
        // 종료 시간이 시작시간 이전 인지 검사
        return endTime.isBefore(startTime);
    }
}
