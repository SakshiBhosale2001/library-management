package com.projectrestapi.projectrestapi.repository;

import com.projectrestapi.projectrestapi.entity.Author;
import com.projectrestapi.projectrestapi.entity.login.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepo extends CrudRepository<Users, Long>{
     Optional<Users> findByEmail(String email);


}