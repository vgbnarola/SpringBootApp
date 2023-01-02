package com.narola.cron;

import com.narola.entity.ForgotPasswordLog;
import com.narola.repo.ForgotPasswordLogRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class PasswordChangesStatus {

    @Autowired
    private ForgotPasswordLogRepo forgotPasswordLogRepo;

    public void changeStatus() {
        List<ForgotPasswordLog> forgotPasswordList = forgotPasswordLogRepo.getExpireLinkList();
        if (forgotPasswordList != null && !forgotPasswordList.isEmpty()) {
            forgotPasswordList.forEach(x -> x.setStatus(1));
        }
        forgotPasswordLogRepo.saveAll(forgotPasswordList);
    }
}