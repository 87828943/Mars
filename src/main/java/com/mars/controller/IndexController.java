package com.mars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {


    @RequestMapping(value = "/index",method = RequestMethod.GET)
    private String index(Model model){
        return "index";
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    private String layout(Model model){
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

    @RequestMapping(value = "/test/test1",method = RequestMethod.POST)
    private String test1(Model model){
        return "bill/test1";
    }

    @RequestMapping(value = "/test/test2",method = RequestMethod.POST)
    private String test2(Model model){
        return "bill/test2";
    }
}
