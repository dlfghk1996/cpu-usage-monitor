package io.terra.cpuusagemonitor.common.exception;

import io.terra.cpuusagemonitor.common.dto.ErrorResponse;
import io.terra.cpuusagemonitor.common.enums.ResponseCode;
import jakarta.persistence.EntityNotFoundException;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "io.terra.cpuusagemonitor")
public class ControllerAdvice {

    @ExceptionHandler(BizException.class)
    public ErrorResponse handleCustom(BizException e) {

        if (e.getMessage() != null) {
            return new ErrorResponse(e.getResponseCode(), e.getMessage());
        }

        log.info("err: {}", e.getResponseCode().getLabel());
        return new ErrorResponse(e.getResponseCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        log.error("err: ", e);
        return new ErrorResponse(ResponseCode.INVALID_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleENFException(EntityNotFoundException e) {
        log.error("err: ", e);
        return new ErrorResponse(ResponseCode.ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        log.error("err: ", e);
        return new ErrorResponse(ResponseCode.ERROR);
    }

    @ExceptionHandler({SQLException.class})
    public ErrorResponse handleSQLException(Exception e) {
        return new ErrorResponse(ResponseCode.ERROR_SQL);
    }
}
