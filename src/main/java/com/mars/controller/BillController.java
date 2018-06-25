package com.mars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/bill")
public class BillController {


    @RequestMapping(value = "/main",method = RequestMethod.POST)
    public String main(){
        return "bill/main";
    }
}
