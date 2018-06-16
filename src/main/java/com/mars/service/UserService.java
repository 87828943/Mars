package com.mars.service;

import com.mars.entity.User;
import com.mars.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void register(User user) {
        userRepository.save(user);
    }

    public User findByNameOrEmail(String name, String email) {
        return userRepository.findByNameOrEmail(name,email);
    }
}
