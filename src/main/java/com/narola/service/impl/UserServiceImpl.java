package com.narola.service.impl;

import com.narola.bean.JwtAuthRespBean;
import com.narola.dto.UserDto;
import com.narola.dto.UserForgotPassDto;
import com.narola.dto.UserLoginDto;
import com.narola.entity.ForgotPasswordLog;
import com.narola.entity.Role;
import com.narola.entity.User;
import com.narola.repo.ForgotPasswordLogRepo;
import com.narola.repo.UserRepo;
import com.narola.security.CustomUserDetails;
import com.narola.security.JwtTokenHelper;
import com.narola.service.UserService;
import com.narola.util.AppConstant;
import com.narola.util.AppUtil;
import com.narola.util.CommonEnums;
import com.narola.util.MessageKey;
import com.narola.util.email.EmailDetails;
import com.narola.util.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    EmailService emailService;

    @Autowired
    JwtTokenHelper jwtTokenHelper;

    @Value("${site.url}")
    private String siteUrl;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ForgotPasswordLogRepo forgotPasswordLogRepo;

    @Autowired
    private MessageSource messageSource;

    @Override
    public UserDto userToDto(User user) {
        return this.modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto createUser(UserDto userDto, String requestId) {

        if (userRepo.findByUserNameOrEmail(userDto.getUserName(), userDto.getEmail()) != null) {
            throw new EntityExistsException(requestId);
        }
        User user = this.dtoToUser(userDto);
        userDto.setPassword(String.valueOf(AppUtil.generatePassword(8)));
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.ADMIN);
        log.info("Request Id: " + requestId + " -------> user signup Password Generated Error.");

        User savedUser = this.userRepo.save(user);
        log.info("Request Id: " + requestId + " -------> user signup user added in DB.");
        savedUser.setPassword(userDto.getPassword());
        return this.userToDto(savedUser);
    }

    public User dtoToUser(UserDto userDto) {
        return this.modelMapper.map(userDto, User.class);
    }

    @Override
    public User getUserByName(String userName) {
        User user = userRepo.findByUserName(userName);
        return user;
    }

    @Override
    public User deleteUserByName(String userName)
    {
        User user = userRepo.findByUserName(userName);
        userRepo.delete(user);
        return user;
    }

    @Override
    public User updateUser(String userName, UserDto userDto) {
        User user = userRepo.findByUserName(userName);
        if(user!=null)
        {
            user.setEmail(userDto.getEmail());
            user.setUserName(userDto.getUserName());
            user.setAbout(userDto.getAbout());
            userRepo.save(user);
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public JwtAuthRespBean login(UserLoginDto userLoginDto) {

        User user = userRepo.findByUserName(userLoginDto.getUserName());
        if (user == null) {
            throw new EntityNotFoundException("User not Found");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginDto.getUserName(), userLoginDto.getPassword());
        authenticationManager.authenticate(authenticationToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginDto.getUserName());
        String token = this.jwtTokenHelper.generateToken(userDetails);

        JwtAuthRespBean jwtAuthRespBean = new JwtAuthRespBean();
        jwtAuthRespBean.setToken(token);
        jwtAuthRespBean.setExpiration(jwtTokenHelper.getExpirationDateFromToken(token).toString());
        jwtAuthRespBean.setStatus(CommonEnums.APIResponseCode.SUCCESS.getCode());

        return jwtAuthRespBean;
    }

    @Override
    public boolean forgotPasswordSendLink(UserForgotPassDto userForgotPassDto) {
        User user = userRepo.findByUserName(userForgotPassDto.getUserName());
        String token;
        if (user == null) {
            throw new EntityNotFoundException(messageSource.getMessage(MessageKey.JPA_ERROR_USERNOTFOUND_MSG, null, LocaleContextHolder.getLocale()));
        }
        List<ForgotPasswordLog> forgotPasswordLogList = forgotPasswordLogRepo.findByUser(user);
        ForgotPasswordLog objForgotPasswordLog = forgotPasswordLogList.stream().filter(field -> field.getStatus() == 0 && (field.getTokenExpiry() != null && field.getTokenExpiryDate().compareTo(new Date()) >= 0)).sorted(Comparator.comparing(ForgotPasswordLog::getId).reversed()).findFirst().orElse(null);

        if (objForgotPasswordLog != null) {
            token = objForgotPasswordLog.getToken();
        } else {
            CustomUserDetails customUserDetails = new CustomUserDetails(user);
            token = jwtTokenHelper.generateToken(customUserDetails);

            ForgotPasswordLog forgotPasswordLog = new ForgotPasswordLog();
            forgotPasswordLog.setToken(token);
            forgotPasswordLog.setUser(user);
            SimpleDateFormat formatter = new SimpleDateFormat(AppConstant.DATE_FORMAT);
            forgotPasswordLog.setTokenExpiry(formatter.format(jwtTokenHelper.getExpirationDateFromToken(token)));
            forgotPasswordLogRepo.save(forgotPasswordLog);
        }

        String forgotPassLink = siteUrl + "/" + "app" + "/" + "forgotpassword" + "/" + token;
        EmailDetails emailDetails = new EmailDetails();

        emailDetails.setSubject(messageSource.getMessage(MessageKey.EMAIL_FORGOTPASSWORD_SUBJECT_MSG, new Object[]{user.getUserName()}, LocaleContextHolder.getLocale()));
        emailDetails.setRecipient(user.getEmail());
        emailDetails.setMsgBody(forgotPassLink);
        emailDetails.setTemplateName("forgotpass-template.html");

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("userName", user.getUserName());
        templateData.put("resetLink", forgotPassLink);
        emailDetails.setTemplateData(templateData);

        if (emailService.sendSimpleHtmlMail(emailDetails)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean forgotPassword(String username, String password) {

        User user = userRepo.findByUserName(username);
        if (user == null) {
            throw new EntityNotFoundException(messageSource.getMessage(MessageKey.JPA_ERROR_USERNOTFOUND_MSG, null, LocaleContextHolder.getLocale()));
        }

        user.setPassword(this.passwordEncoder.encode(password));
        userRepo.save(user);
        return true;
    }
}