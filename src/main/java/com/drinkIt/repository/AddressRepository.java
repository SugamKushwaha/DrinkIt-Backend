package com.drinkIt.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.drinkIt.entity.Address;
import com.drinkIt.entity.User;

public interface AddressRepository extends JpaRepository<Address, Long>{

     List<Address> findByUser(User user);

     Optional<Address> findByIdAndUser(Long id, User user);
     
    
}
