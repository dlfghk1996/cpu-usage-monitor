package io.terra.cpuusagemonitor.common.exception;


import io.terra.cpuusagemonitor.common.enums.ResponseCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper = false)
public class BizException extends RuntimeException {

    @Getter
    private int errorCode;
    private ResponseCode responseCode;
    private String message;
    public BizException() {
        super();
    }

    public BizException(ResponseCode responseCode) {
        super(responseCode.getLabel());
        this.errorCode = responseCode.getCode();
        this.responseCode = responseCode;
    }

    public BizException(ResponseCode responseCode, String message) {
        super(responseCode.getLabel());
        this.errorCode = responseCode.getCode();
        this.responseCode = responseCode;
        this.message = message;
    }
}
