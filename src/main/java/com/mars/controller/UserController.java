package com.mars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = "/forgotPassword",method = RequestMethod.GET)
    private String forgotPassword(){
        return "user/forgotPassword";
    }

}
