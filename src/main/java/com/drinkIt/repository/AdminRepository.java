package com.drinkIt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.drinkIt.entity.Admin;
import com.drinkIt.entity.User;

public interface AdminRepository
        extends JpaRepository<Admin, Long> {


    Optional<Admin>
    findByUser(User user);


    Optional<Admin>
    findByUserId(Long userId);


    boolean existsByUserId(
            Long userId
    );

}