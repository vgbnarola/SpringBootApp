package com.narola.validator;

import com.narola.dto.UserDto;
import com.narola.util.MessageKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserSignupValidator implements Validator {

    private String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", messageSource.getMessage(MessageKey.VALIDATE_ERROR_EMPTY_USERNAME,null, LocaleContextHolder.getLocale()));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", messageSource.getMessage(MessageKey.VALIDATE_ERROR_EMPTY_EMAIL,null, LocaleContextHolder.getLocale()));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "about", messageSource.getMessage(MessageKey.VALIDATE_ERROR_EMPTY_ABOUT,null, LocaleContextHolder.getLocale()));

        if (errors.getErrorCount() == 0) {
            UserDto userDto = (UserDto) target;

            if (userDto.getUserName().length() < 4 || userDto.getUserName().length() > 25) {
                errors.rejectValue("userName", messageSource.getMessage(MessageKey.VALIDATE_ERROR_USERNAME,null, LocaleContextHolder.getLocale()));
            }
            if (!Pattern.compile(emailPattern).matcher(userDto.getEmail()).matches()) {
                errors.rejectValue("email", messageSource.getMessage(MessageKey.VALIDATE_ERROR_EMAIL,null, LocaleContextHolder.getLocale()));
            }
        }
    }
}