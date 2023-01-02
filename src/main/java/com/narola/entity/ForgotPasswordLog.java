package com.narola.entity;

import com.narola.util.AppConstant;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "reset_password_log")
public class ForgotPasswordLog {

    @ManyToOne
    User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "status")
    private int status;

    @Column(name = "token_expiry")
    private String tokenExpiry;

    public Date getTokenExpiryDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(AppConstant.DATE_FORMAT);
        try {
            return formatter.parse(tokenExpiry);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}