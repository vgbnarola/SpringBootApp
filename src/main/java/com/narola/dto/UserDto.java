package com.narola.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    @Schema(hidden = true)
    private int id;

    @Schema(description = "Provide Username when you set in Registration", required = true)
    private String userName;

    @Schema(description = "Provide Email when you set in Registration", required = true)
    private String email;

    @Schema(hidden = true)
    private String password;

    @Schema(description = "Provide about when you set in Registration", required = true)
    private String about;
}