package com.mars.controller;

import com.mars.entity.User;
import com.mars.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class BaseController {

    @Value("${mars.session.user.key}")
    private String MARS_SESSION_USER_KEY;

    @Autowired
    private UserService userService;

    public User getUser(){
        return (User)getSession().getAttribute(MARS_SESSION_USER_KEY);
    }

    public Long getUserId(){
        User user = getUser();
        if(user!=null){
            return getUser().getId();
        }else{
            return 0L;
        }
    }

    public String getName(){
        User user = getUser();
        if(user!=null){
            return getUser().getName();
        }else{
            return "";
        }
    }

    public HttpSession getSession(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
    }

    void reLoadSession(){
        Long userId = getUserId();
        User user = userService.findById(userId);
        if(user != null){
            getSession().setAttribute(MARS_SESSION_USER_KEY,user);
        }
    }
}
