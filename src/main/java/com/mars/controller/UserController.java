package com.mars.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.mars.entity.User;
import com.mars.entity.result.ResponseData;
import com.mars.exception.MarsException;
import com.mars.service.UserService;
import com.mars.utils.CookieUtil;
import com.mars.utils.MD5Util;

@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${mars.md5.password.key}")
    private String MARS_MD5_PASSWORD_KEY;
    @Value("${mars.session.user.key}")
    private String MARS_SESSION_USER_KEY;
    @Value("${mars.cookie.user.key}")
    private String MARS_COOKIE_USER_KEY;
    @Value("${default.logo}")
    private String DEFAULT_LOGO;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    private ResponseData login(User user, HttpServletRequest request,HttpServletResponse response){
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
        String cookieStr = "userId:"+loginUser.getId().toString()+"name:"+loginUser.getName();
        String userCookie = JSONObject.toJSONString(loginUser);
        String userCookie2 = loginUser.toString();
        //设置cookie
        CookieUtil.addCookie(response, MARS_COOKIE_USER_KEY, userCookie2, 24*60*7);
        //设置session
        HttpSession session = request.getSession();
        session.setAttribute(MARS_SESSION_USER_KEY,loginUser);
        session.setMaxInactiveInterval(3600*2);
        //但是当cookie关闭后，用于保存SessionID的JSESSIONID会消失(此时cookie并没有过期) ，所以得将JSESESSION持久化
        CookieUtil.addCookie(response, "JSESESSIONID", session.getId(), 2*60);
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
    
    
    @ResponseBody
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    private ResponseData logout(User user, HttpServletRequest request,HttpServletResponse response) throws Exception{
    	//首先是考虑编码问题
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
    	//false代表：不创建session对象，只是从request中获取。  
        HttpSession session = request.getSession(false);
        if(session == null){
            return new ResponseData();  
        }  
        session.removeAttribute(MARS_SESSION_USER_KEY);
        CookieUtil.addCookie(response, MARS_COOKIE_USER_KEY, null, 0);
        return new ResponseData();
    }
}
