package com.drinkIt.service.impl;

import org.springframework.stereotype.Service;

import com.drinkIt.dto.user.UserResponse;
import com.drinkIt.dto.user.UserUpdateRequest;
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

    @Override
    public UserResponse updateUser(String email, UserUpdateRequest request) {

        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User Not Found"));

           if (request.getName() != null && !request.getName().trim().isEmpty()) {
            user.setName( request.getName().trim());
        }
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            user.setEmail( request.getEmail().trim());
        }
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            user.setPhone( request.getPhone().trim());
        }
        
          User updatedUser =
                userRepository.save(user);

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getPhone(),
                updatedUser.getRole(),
                updatedUser.isVerified(),
                updatedUser.getCreatedAt()
        );
    }
}
