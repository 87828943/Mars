package com.mars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping(value = "/test1",method = RequestMethod.POST)
    private String test1(Model model){
        return "bill/test1";
    }

    @RequestMapping(value = "/test2",method = RequestMethod.POST)
    private String test2(Model model){
        return "bill/test2";
    }
}
