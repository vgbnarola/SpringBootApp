package com.narola.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "spring.custom.app",ignoreInvalidFields = true)
public class AppConstant {

    public static int interval= 10000;

    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public List<String> servers;

}