package com.narola.validator;

import com.narola.dto.UserLoginDto;
import com.narola.util.MessageKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserLoginValidator implements Validator {

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserLoginDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", messageSource.getMessage(MessageKey.VALIDATE_ERROR_EMAIL,null, LocaleContextHolder.getLocale()));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", messageSource.getMessage(MessageKey.VALIDATE_ERROR_EMPTY_PASSWORD,null, LocaleContextHolder.getLocale()));
    }
}
