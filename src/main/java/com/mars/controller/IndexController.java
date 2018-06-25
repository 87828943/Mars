package com.mars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {


    @RequestMapping(value = "/index",method = RequestMethod.GET)
    private String index(){
        return "index";
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    private String layout(){
        return "layout";
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    private String login(){
        return "login";
    }

    @RequestMapping(value="/register",method=RequestMethod.GET)
    public String regist() {
        return "register";
    }

    @RequestMapping(value = "/forgotPassword",method = RequestMethod.GET)
    private String forgotPassword(){
        return "user/forgotPassword";
    }

    @RequestMapping(value = "/main",method = RequestMethod.POST)
    private String main(){
        return "main";
    }

}
