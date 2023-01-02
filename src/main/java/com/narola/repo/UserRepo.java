package com.narola.repo;

import com.narola.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByUserName(String userName);

    User findByEmail(String email);


    User findByUserNameOrEmail(String userName,String email);

}