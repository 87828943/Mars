package com.mars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MarsApplication {

	public static void main(String[] args) {
		long starTime = System.currentTimeMillis();
        SpringApplication.run(MarsApplication.class,args);
        long endTime=System.currentTimeMillis();
        long Time=endTime-starTime;
        System.out.println("\n启动时间:"+ Time/1000 +"秒");
        System.out.println("...............................................................");
        System.out.println("..................Service starts successfully..................");
        System.out.println("...............................................................");
	}
}
