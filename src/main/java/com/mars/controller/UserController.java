package com.mars.controller;

import com.mars.entity.User;
import com.mars.entity.result.ResponseData;
import com.mars.exception.MarsException;
import com.mars.service.UserService;
import com.mars.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${mars.md5.password.key}")
    private String MARS_MD5_PASSWORD_KEY;
    @Value("${mars.session.user.key}")
    private String MARS_SESSION_USER_KEY;
    @Value("${default.logo}")
    private String DEFAULT_LOGO;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    private ResponseData login(User user, HttpServletRequest request){
        if(user==null || StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getPassword())){
            return new ResponseData(MarsException.PARAM_EXCEPTION);
        }
        String name = user.getName();
        String password = user.getPassword();
        User loginUser = userService.findByNameOrEmail(name, name);
        if(loginUser == null){
            return new ResponseData(MarsException.USER_EXIST);
        }
        if(!MD5Util.encrypt(MARS_MD5_PASSWORD_KEY + password).equals(loginUser.getPassword())){
            return new ResponseData(MarsException.PASSWORD_ERROR);
        }

        HttpSession session = request.getSession();
        session.setAttribute(MARS_SESSION_USER_KEY,loginUser);
        return new ResponseData();
    }

    @ResponseBody
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    private ResponseData register(User user, HttpServletRequest request){
        if(user==null || StringUtils.isEmpty(user.getEmail()) || StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getPassword())){
            return new ResponseData(MarsException.PARAM_EXCEPTION);
        }
        String email = user.getEmail();
        String name = user.getName();
        User isExistUser = userService.findByNameOrEmail(name, email);
        if(isExistUser!=null){
            if(email.equals(isExistUser.getEmail())){
                return new ResponseData(MarsException.EMAIL_EXIST);
            }
            if(name.equals(isExistUser.getName())){
                return new ResponseData(MarsException.NAME_EXIST);
            }
        }
        user.setCreateDate(new Date());
        user.setLogo(DEFAULT_LOGO);
        user.setPassword(MD5Util.encrypt(MARS_MD5_PASSWORD_KEY + user.getPassword()));
        User registerUser = userService.register(user);
        HttpSession session = request.getSession();
        session.setAttribute(MARS_SESSION_USER_KEY,registerUser);
        return new ResponseData();
    }

}
