package com.mars.repository;

import com.mars.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long>{
    User findByNameOrEmail(String name, String email);
}
