package com.mars;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldTest {

    @RequestMapping("/hello")
    private String hello(){
        return "hello World";
    }
}
