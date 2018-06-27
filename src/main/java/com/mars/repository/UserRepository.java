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

	User findUserById(Long id);
	
    @Modifying(clearAutomatically=true)
    @Transactional
    @Query("update User set password=:newPassword,updateDate=:updateDate where id=:id")
    void setNewPasswordById(@Param("newPassword")String newPassword, @Param("updateDate")Date updateDate, @Param("id")Long id);

    @Modifying(clearAutomatically=true)
    @Transactional
    @Query("update User set logo=:savePath,updateDate=:updateDate where id=:id")
    void setLogoById(@Param("savePath") String savePath, @Param("updateDate")Date updateDate, @Param("id")Long id);
    
    @Modifying(clearAutomatically=true)
    @Transactional
    @Query("update User set sex=:sex,updateDate=:updateDate,description=:description where id=:id")
	void updateUserbyId(@Param("description") String description,@Param("sex") Integer sex,@Param("updateDate") Date date,@Param("id")Long userId);
}
