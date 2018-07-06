package com.mars.scheduler;

import com.mars.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TestScheduler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private UserController userController;

    @Scheduled(cron = "*/6 * * * * ?")
    public void sendEmail(){
        //userController.sendHtmlEmail("XXXX@qq.com","[TestScheduler]","####TestScheduler####");
        System.out.println("send success!");
    }

    @Scheduled(fixedRate = 6000)
    public void test(){
        System.out.println("current time:"+dateFormat.format(new Date()));
    }

}
