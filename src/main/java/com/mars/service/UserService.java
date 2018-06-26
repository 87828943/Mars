package com.mars.service;

import com.mars.entity.User;
import com.mars.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        return userRepository.save(user);
    }

    public User findByNameOrEmail(String name, String email) {
        return userRepository.findByNameOrEmail(name,email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

	public User findById(Long id) {
		return userRepository.findUserById(id);
	}

    public void setNewPasswordById(String newPassword, Date updateTime, Long id) {
        userRepository.setNewPasswordById(newPassword,updateTime,id);
    }

    public void setLogoById(String savePath,Date updateTime, Long userId) {
        userRepository.setLogoById(savePath,updateTime,userId);
    }
}
