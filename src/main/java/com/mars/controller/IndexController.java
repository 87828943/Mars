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

    @RequestMapping(value = "/main",method = RequestMethod.POST)
    @LoggerAnnotation(desc = "首页主页面")
    public String main(){
        return "main";
    }

    /*
    * user相关start
    * */

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

    @RequestMapping(value = "/newPassword",method = RequestMethod.GET)
    @LoggerAnnotation(desc = "设置新密码")
    public String newPassword(){
        return "user/newPassword";
    }

    @RequestMapping(value = "/editUserLogo",method = RequestMethod.POST)
    @LoggerAnnotation(desc = "更改头像页面")
    public String editUserLogo(){
        return "user/editUserLogo";
    }

    @RequestMapping(value = "/editUserInfo",method = RequestMethod.POST)
    @LoggerAnnotation(desc = "更改资料页面")
    public String editUserInfo(){
        return "user/editUserInfo";
    }

    @RequestMapping(value = "/editUserPassword",method = RequestMethod.POST)
    @LoggerAnnotation(desc = "更改密码页面")
    public String editUserPassword(){
        return "user/editUserPassword";
    }
    @RequestMapping(value = "/editUserInfo",method = RequestMethod.POST)
    @LoggerAnnotation(desc = "编辑用户信息")
    public String editUser(){
        return "user/editUserInfo";
    }

    /*
    * user相关end
    * */


    /*
    * bill相关
    * */

    @RequestMapping(value = "/addBill",method = RequestMethod.POST)
    @LoggerAnnotation(desc = "添加账单")
    public String addBill(){
        return "bill/addBill";
    }
}
