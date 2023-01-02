package com.narola.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.narola.bean.JwtAuthRespBean;
import com.narola.bean.ResponseVO;
import com.narola.bean.View;
import com.narola.dto.UserDto;
import com.narola.dto.UserForgotPassDto;
import com.narola.dto.UserLoginDto;
import com.narola.service.UserService;
import com.narola.util.AppUtil;
import com.narola.util.MessageKey;
import com.narola.validator.UserForgotPassValidator;
import com.narola.validator.UserLoginValidator;
import com.narola.validator.UserSignupValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping("/auth")
@Slf4j
@Tag(name = "01.AUTHENTICATION API", description = "The API for authentication User.")
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserSignupValidator userSignupValidator;

    @Autowired
    private UserLoginValidator userLoginValidator;

    @Autowired
    private UserForgotPassValidator userForgotPassValidator;

    @InitBinder("userDto")
    public void initBinderUserSignupValidator(WebDataBinder binder) {
        binder.setValidator(userSignupValidator);
    }

    @InitBinder("userLoginDto")
    public void initBinderUserLoginValidator(WebDataBinder binder) {
        binder.setValidator(userLoginValidator);
    }

    @InitBinder("userForgotPassDto")
    public void initBinderUserForgotPassValidator(WebDataBinder binder) {
        binder.setValidator(userForgotPassValidator);
    }

    private class MySignUpResponse extends ResponseVO<UserDto>{};

    private class MyLoginResponse extends ResponseVO<JwtAuthRespBean>{};

    @PostMapping("/signup")
    @Operation(summary = "Sign up Api for new User.")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "*/*", schema = @Schema(implementation =MySignUpResponse.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "*/*", schema = @Schema(implementation = ResponseVO.class)))
    public ResponseEntity<ResponseVO<UserDto>> signUp(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        ResponseVO<UserDto> responseVO = new ResponseVO<>();
        responseVO.setRequestId(AppUtil.generateRequestId());
        log.info("Request ID: " + responseVO.getRequestId() + " -------> user signup Request Created.");
        if (bindingResult.hasErrors()) {
            log.info("Request ID: " + responseVO.getRequestId() + " -------> user signup validation Error.");
            return ResponseEntity.ok(processErrorResponse(responseVO, bindingResult));
        } else {
            UserDto createUserDto = userService.createUser(userDto, responseVO.getRequestId());
            log.info("Request ID: " + responseVO.getRequestId() + " -------> user signup Success.");
            return ResponseEntity.ok(processSuccessResponse(responseVO, createUserDto));
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login Api.")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "*/*", schema = @Schema(implementation =MyLoginResponse.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "*/*", schema = @Schema(implementation = ResponseVO.class)))
    public ResponseEntity<ResponseVO<JwtAuthRespBean>> login(@Valid @RequestBody UserLoginDto userLoginDto, BindingResult bindingResult, Locale locale) {

        ResponseVO responseVO = new ResponseVO<>();
        responseVO.setRequestId(AppUtil.generateRequestId());
        log.info(messageSource.getMessage(MessageKey.APP_ERROR_DEFAULT_MSG, null, locale));

        if (bindingResult.hasErrors()) {
            return ResponseEntity.ok(processErrorResponse(responseVO, bindingResult));
        } else {
            JwtAuthRespBean jwtAuthRespBean = userService.login(userLoginDto);
            return ResponseEntity.ok(processSuccessResponse(responseVO, jwtAuthRespBean));
        }
    }

    @PostMapping("/forgotpassword")
    @JsonView(View.Base.class)
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "*/*", schema = @Schema(implementation =ResponseVO.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "*/*", schema = @Schema(implementation = ResponseVO.class)))
    @ApiResponse(responseCode = "500", content = @Content(mediaType = "*/*", schema = @Schema(implementation = String.class)),description = "Internal server Error")
    public ResponseEntity<ResponseVO> forgotPassword(@Valid @RequestBody UserForgotPassDto userForgotPassDto,
                                                     BindingResult bindingResult) {
        ResponseVO responseVO = new ResponseVO<>();
        responseVO.setRequestId(AppUtil.generateRequestId());

        if (bindingResult.hasErrors()) {
            return ResponseEntity.ok(processErrorResponse(responseVO, bindingResult));

        } else {
            if (userService.forgotPasswordSendLink(userForgotPassDto)) {
                return ResponseEntity.ok(processSuccessResponse(responseVO, messageSource.getMessage(MessageKey.API_LOGIN_PASSWORD_SEND_LINK_MSG, null, LocaleContextHolder.getLocale())));
            } else {
                return ResponseEntity.ok(processErrorDefaultResponse(responseVO));
            }
        }
    }
}