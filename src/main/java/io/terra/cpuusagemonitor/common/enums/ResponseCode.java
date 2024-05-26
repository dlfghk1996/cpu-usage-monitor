package io.terra.cpuusagemonitor.common.enums;


import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public enum ResponseCode {
    OK(2000, "success", SC_OK),

    INVALID_REQUEST(4000, "잘못된 요청입니다.", SC_OK),

    ERROR(5000, "SERVER ERROR", SC_OK),
    ERROR_SQL(5001, "SQL ERROR", SC_OK),

    // Service error
    INVALID_REQUEST_TIME(6000, "조회 가능기간에 포함되지 않습니다.", SC_OK);

    private final int code;
    private final String label;
    private final int httpStatusCode;

    ResponseCode(int code, String label, int httpStatusCode) {
        this.code = code;
        this.label = label;
        this.httpStatusCode = httpStatusCode;
    }

    public int getCode() {
        return this.code;
    }


    public String getLabel() {
        return this.label;
    }

    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }
}
