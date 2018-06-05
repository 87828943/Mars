package com.mars;

import com.mars.entity.User;
import com.mars.repository.UserRepository;
import com.mars.utils.MD5Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MarsApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void testUser(){
		User user = new User();
		user.setName("abc");
		user.setEmail("abc@qq.com");
		user.setPassword(MD5Util.encrypt("mimahenjiandan"));

		userRepository.save(user);
		//userRepository.findAll();
		//userRepository.delete(user);
		//userRepository.count();
		//...等等
	}

}
