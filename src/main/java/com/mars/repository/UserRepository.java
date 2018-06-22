package com.mars.repository;

import com.mars.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;

public interface UserRepository  extends JpaRepository<User, Long>{
    User findByNameOrEmail(String name, String email);

    User findByEmail(String email);

    @Modifying(clearAutomatically=true)
    @Transactional
    @Query("update User set password=:newPassword,updateDate=:updateDate where id=:id")
    void setNewPasswordById(@Param("newPassword")String newPassword, @Param("updateDate")Date updateDate, @Param("id")Long id);
}
