package com.narola.validator;

public @interface ValidateForm {

    boolean required() default true;

}
