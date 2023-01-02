package com.narola.cron;

import com.narola.util.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class TaskSchedule {

    @Autowired
    private PasswordChangesStatus passwordChangesStatus;

    @Scheduled(fixedRateString = "${spring.custom.app.interval}" )
    public void performTask() {
        log.info("Regular Task : " + new SimpleDateFormat(AppConstant.DATE_FORMAT).format(new Date()));
        passwordChangesStatus.changeStatus();
    }

    @Scheduled(initialDelay = 100000,fixedRateString = "100000")
    public void performDelayTask() {
        log.info("Delay Regular Task : " + new SimpleDateFormat(AppConstant.DATE_FORMAT).format(new Date()));
    }


}