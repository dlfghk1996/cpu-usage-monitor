package io.terra.cpuusagemonitor.common.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import io.terra.cpuusagemonitor.common.enums.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Exception 발생 Response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "최상위 응답 에러 DTO")
public class ErrorResponse {
    @Schema(description = "상태 코드")
    int status;

    @Schema(description = "상태명")
    String label;

    @Schema(description = "메시지")
    String message;

    public ErrorResponse(ResponseCode responseCode) {
        this.label = String.valueOf(responseCode);
        this.status = responseCode.getCode();
        this.message = responseCode.getLabel();
    }

    public ErrorResponse(ResponseCode responseCode, String customMessage) {
        this.label = String.valueOf(responseCode);
        this.status = responseCode.getCode();
        this.message = customMessage;
    }
}
