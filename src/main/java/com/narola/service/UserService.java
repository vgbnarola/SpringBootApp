package com.narola.service;

import com.narola.bean.JwtAuthRespBean;
import com.narola.dto.UserDto;
import com.narola.dto.UserForgotPassDto;
import com.narola.dto.UserLoginDto;
import com.narola.entity.User;

import java.util.List;

public interface UserService {

	UserDto userToDto(User user);

	UserDto createUser(UserDto userDto,String requestId);

	User getUserByName(String userName);

	User deleteUserByName(String userName);

	User updateUser(String userName,UserDto userDto);

	List<User> getUsers();

	JwtAuthRespBean login(UserLoginDto userLoginDto);

	boolean forgotPasswordSendLink(UserForgotPassDto userForgotPassDto);

	boolean forgotPassword(String username, String password);

}