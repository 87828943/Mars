package com.mars.controller;

import com.mars.entity.User;
import com.mars.entity.result.ResponseData;
import com.mars.exception.MarsException;
import com.mars.service.UserService;
import com.mars.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/user")
public class UserController {

    private final static String MARS_MD5_KEY = "marsXXX";

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/forgotPassword",method = RequestMethod.GET)
    private String forgotPassword(){
        return "user/forgotPassword";
    }

    @ResponseBody
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    private ResponseData register(User user){
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
        user.setPassword(MD5Util.encrypt(MARS_MD5_KEY + user.getPassword()));
        userService.register(user);
        return new ResponseData();
    }

}
