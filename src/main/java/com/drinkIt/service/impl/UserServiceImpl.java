package com.drinkIt.service.impl;

import org.springframework.stereotype.Service;

import com.drinkIt.dto.user.UserResponse;
import com.drinkIt.entity.User;
import com.drinkIt.repository.UserRepository;
import com.drinkIt.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository ;

    @Override
    public UserResponse getCurrentUser(String email)
     {
User user = userRepository.findByEmail(email).orElseThrow( () -> new RuntimeException( "User not found" ));

        return new UserResponse(
             user.getId(),
             user.getName(),
             user.getEmail(),
             user.getPhone(),
             user.getRole(),
             user.isVerified(),
             user.getCreatedAt()
        ); 
    
    }
    
}
