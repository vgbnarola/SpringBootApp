package com.narola.exception;

import com.narola.bean.ResponseVO;
import com.narola.util.CommonEnums;
import com.narola.util.MessageKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;

@RestControllerAdvice
@Slf4j
public class UserExceptionController {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = Exception.class)
    public ResponseVO exception(Exception exception) {
        ResponseVO responseVO = new ResponseVO<>();
        responseVO.setError(messageSource.getMessage(MessageKey.APP_ERROR_DEFAULT_MSG,null, LocaleContextHolder.getLocale()));
        responseVO.setCode(CommonEnums.APIResponseCode.FAIL.getCode());
        return responseVO;
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseVO exception(BadCredentialsException exception) {
        ResponseVO responseVO = new ResponseVO<>();
        responseVO.setError(messageSource.getMessage(MessageKey.API_LOGIN_FAIL_MSG,null, LocaleContextHolder.getLocale()));
        responseVO.setCode(CommonEnums.APIResponseCode.FAIL.getCode());
        return responseVO;
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseVO exception(EntityNotFoundException exception) {

        String message=messageSource.getMessage(MessageKey.JPA_ERROR_USERNOTFOUND_MSG,null, LocaleContextHolder.getLocale());
        log.info("Reqeust Id"+exception.getMessage()+" -------> "+message);
        ResponseVO responseVO = new ResponseVO<>();
        responseVO.setRequestId(exception.getMessage());
        responseVO.setError(message);
        responseVO.setCode(CommonEnums.APIResponseCode.FAIL.getCode());
        return responseVO;
    }

    @ExceptionHandler(value = NonUniqueResultException.class)
    public ResponseVO exception(NonUniqueResultException exception) {
        ResponseVO responseVO = new ResponseVO<>();
        responseVO.setError(messageSource.getMessage(MessageKey.JPA_ERROR_DATA_NOT_FOUND_MSG,null, LocaleContextHolder.getLocale()));
        responseVO.setCode(CommonEnums.APIResponseCode.FAIL.getCode());
        return responseVO;
    }

    @ExceptionHandler(value = EntityExistsException.class)
    public ResponseVO exception(EntityExistsException exception) {
        String message=messageSource.getMessage(MessageKey.JPA_ERROR_USER_ALREADY_EXIST_MSG,null, LocaleContextHolder.getLocale());
        log.info("Reqeust Id: "+exception.getMessage()+" -------> "+message);
        ResponseVO responseVO = new ResponseVO<>();
        responseVO.setRequestId(exception.getMessage());
        responseVO.setError(message);
        responseVO.setCode(CommonEnums.APIResponseCode.FAIL.getCode());
        return responseVO;
    }
}