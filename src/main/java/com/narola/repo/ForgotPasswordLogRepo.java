package com.narola.repo;

import com.narola.entity.ForgotPasswordLog;
import com.narola.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ForgotPasswordLogRepo  extends JpaRepository<ForgotPasswordLog, Long> {


    List<ForgotPasswordLog> findByUser(User user);

    @Query(value = "select * from reset_password_log where token_expiry < NOW() and status=0",nativeQuery = true )
    List<ForgotPasswordLog> getExpireLinkList();
}