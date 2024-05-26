package io.terra.cpuusagemonitor.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.terra.cpuusagemonitor.common.enums.ResponseCode;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Schema(title = "최상위 응답 DTO")
public class Response<T> {

    @Schema(description = "데이터")
    T data;

    @Schema(description = "상태코드")
    int status;

    @Schema(description = "메시지")
    String message;


    public Response(T data, int status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public Response(T data, ResponseCode responseCode) {
        this.data = data;
        this.status = responseCode.getCode();
        this.message = responseCode.getLabel();
    }

    public Response(T data, int status) {
        this.data = data;
        this.status = status;
        this.message = "";
    }

    public Response(T data, int status, Exception e) {
        this.data = data;
        this.status = status;
        this.message = e.getClass() + " " + e.getMessage();
    }

    public Response(T data) {
        this.data = data;
        this.status = ResponseCode.OK.getCode();
        this.message = ResponseCode.OK.getLabel();
    }
}
