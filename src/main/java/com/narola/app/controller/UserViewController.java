package com.narola.app.controller;

import com.narola.bean.ForgotPassReqBean;
import com.narola.security.JwtTokenHelper;
import com.narola.service.UserService;
import com.narola.util.MessageKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/app")
@Slf4j
public class UserViewController {

    @Autowired
    MessageSource messageSource;

    @Autowired
    JwtTokenHelper jwtTokenHelper;

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public ModelAndView home(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", messageSource.getMessage("app.error.default.msg", null, LocaleContextHolder.getLocale()));
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping("/forgotpassword/{token}")
    public ModelAndView forgotPassword(@PathVariable("token") String token) {

        ModelAndView modelAndView = new ModelAndView();

        try {
            if (jwtTokenHelper.isTokenExpired(token)) {
                modelAndView.addObject("message", messageSource.getMessage(MessageKey.JWT_TOKEN_EXPIRE_ERROR, null, LocaleContextHolder.getLocale()));
                modelAndView.setViewName("error");
                return modelAndView;

            } else {
                String username = jwtTokenHelper.getUserNameFromToken(token);

                modelAndView.addObject("username", username);
                modelAndView.addObject("token", token);
                modelAndView.setViewName("forgotPass");
                return modelAndView;
            }
        } catch (Exception e) {
            modelAndView.addObject("message", messageSource.getMessage(MessageKey.APP_ERROR_DEFAULT_MSG, null, LocaleContextHolder.getLocale()));
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    @PostMapping("/forgotpassword/submit")
    public ModelAndView forgotPasswordSubmit(@ModelAttribute ForgotPassReqBean reqBean) {

        ModelAndView modelAndView = new ModelAndView();
        try {
            if (reqBean.getPassword1() != null && reqBean.getPassword2() != null
                    && reqBean.getPassword1().equals(reqBean.getPassword2())) {

                String username = jwtTokenHelper.getUserNameFromToken(reqBean.getToken());

                if (username.equals(reqBean.getUsername())) {

                    if (userService.forgotPassword(reqBean.getUsername(), reqBean.getPassword1())) {
                        modelAndView.addObject("message", "Password SuccessFully Updated");
                        modelAndView.setViewName("success");
                    }
                } else {
                    modelAndView.addObject("message", "Invalid Token or Token are Tempared");
                    modelAndView.setViewName("error");
                }
            } else {
                modelAndView.addObject("message", "Password not match");
                modelAndView.setViewName("error");
            }
        } catch (Exception e) {
            log.warn(e.fillInStackTrace().toString());
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }
}