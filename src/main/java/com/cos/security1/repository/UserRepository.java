package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//CRUD함수를 jpaRepository가 들고있음
//레파지토리 어노테이션 불필요

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    //findBy규칙 -
    public User findByUsername(String username);

}
