package com.drinkIt.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.drinkIt.dto.user.UserResponse;
import com.drinkIt.entity.User;
import com.drinkIt.repository.UserRepository;
import com.drinkIt.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> allCustomers() {

        return userRepository.findAll()
                .stream()
                .map(user -> {

                    UserResponse response = new UserResponse();

                    response.setId(user.getId());
                    response.setName(user.getName());
                    response.setEmail(user.getEmail());
                    response.setPhone(user.getPhone());
                    response.setCreatedAt(user.getCreatedAt());

                    return response;
                })
                .toList();
    }
}