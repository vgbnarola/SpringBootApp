package com.narola.bean;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseVO<T> {

    @JsonView(View.Base.class)
    @Schema(description = "Auto Generated Request Id which are unique for all Request")
    private String requestId;

    @JsonView(View.Base.class)
    @Schema(description = "Provide Response Code")
    private String code;

    @JsonView(View.Base.class)
    @Schema(description = "Error Details")
    private String error;

    @JsonView(View.Base.class)
    @Schema(description = "Success Details")
    private String success;

    @JsonView(View.User.class)
    @Schema(description = "List of Error Details if get multiple error")
    private List<String> errors;

    @JsonView(View.User.class)
    @Schema(description = "Generic Data")
    private T data;
}