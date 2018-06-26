package com.mars.controller;

import com.mars.common.aop.LoggerAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {


    @RequestMapping(value = "/index",method = RequestMethod.GET)
    @LoggerAnnotation(desc = "mars首页")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @LoggerAnnotation(desc = "首页")
    public String layout(){
        return "layout";
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    @LoggerAnnotation(desc = "登录页面")
    public String login(){
        return "login";
    }

    @RequestMapping(value="/register",method=RequestMethod.GET)
    @LoggerAnnotation(desc = "注册页面")
    public String regist() {
        return "register";
    }

    @RequestMapping(value = "/forgotPassword",method = RequestMethod.GET)
    @LoggerAnnotation(desc = "忘记密码")
    public String forgotPassword(){
        return "user/forgotPassword";
    }

    @RequestMapping(value = "/main",method = RequestMethod.POST)
    @LoggerAnnotation(desc = "首页主页面")
    public String main(){
        return "main";
    }

    @RequestMapping(value = "/editUserLogo",method = RequestMethod.POST)
    @LoggerAnnotation(desc = "编辑头像页面")
    public String editUserLogo(){
        return "user/editUserLogo";
    }
}
