package com.narola.controller;

import com.narola.bean.JwtAuthRespBean;
import com.narola.bean.ResponseVO;
import com.narola.dto.UserDto;
import com.narola.util.CommonEnums;
import com.narola.util.MessageKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class BaseController {

    @Autowired
    private MessageSource messageSource;

    public ResponseVO processErrorDefaultResponse(ResponseVO responseVO) {
        responseVO.setCode(CommonEnums.APIResponseCode.FAIL.getCode());
        responseVO.setError(messageSource.getMessage(MessageKey.APP_ERROR_DEFAULT_MSG,null, LocaleContextHolder.getLocale()));
        return responseVO;
    }
    public ResponseVO<UserDto> processErrorResponse(ResponseVO<UserDto> responseVO, BindingResult bindingResult) {
        responseVO.setCode(CommonEnums.APIResponseCode.FAIL.getCode());
        responseVO.setErrors(bindingResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getCode).collect(Collectors.toList()));
        return responseVO;
    }

    public ResponseVO<UserDto> processSuccessResponse(ResponseVO<UserDto> responseVO, UserDto userDto) {
        if (userDto != null) {
            responseVO.setCode(CommonEnums.APIResponseCode.SUCCESS.getCode());
            responseVO.setSuccess(messageSource.getMessage(MessageKey.API_SIGNUP_SUCCESS_MSG,null, LocaleContextHolder.getLocale()));
            responseVO.setData(userDto);
        } else {
            responseVO.setCode(CommonEnums.APIResponseCode.FAIL.getCode());
            responseVO.setError(messageSource.getMessage(MessageKey.APP_ERROR_DEFAULT_MSG,null, LocaleContextHolder.getLocale()));
        }
        return responseVO;
    }

    public ResponseVO processSuccessResponse(ResponseVO responseVO, JwtAuthRespBean jwtAuthRespBean) {
        if (jwtAuthRespBean != null) {
            responseVO.setCode(CommonEnums.APIResponseCode.SUCCESS.getCode());
            responseVO.setSuccess(messageSource.getMessage(MessageKey.API_LOGIN_SUCCESS_MSG,null, LocaleContextHolder.getLocale()));
            responseVO.setData(jwtAuthRespBean);
        } else {
            responseVO.setCode(CommonEnums.APIResponseCode.FAIL.getCode());
            responseVO.setError(messageSource.getMessage(MessageKey.APP_ERROR_DEFAULT_MSG,null, LocaleContextHolder.getLocale()));
        }
        return responseVO;
    }

    public ResponseVO processSuccessResponse(ResponseVO responseVO, String successMessage) {
            responseVO.setCode(CommonEnums.APIResponseCode.SUCCESS.getCode());
            responseVO.setSuccess(successMessage);
            return responseVO;
    }
}
