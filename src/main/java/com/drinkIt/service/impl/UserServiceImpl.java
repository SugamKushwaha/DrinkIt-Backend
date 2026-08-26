package com.drinkIt.service.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.drinkIt.dto.user.UserAddressResponse;
import com.drinkIt.dto.user.UserAddressSaveRequest;
import com.drinkIt.dto.user.UserAddressUpdateRequest;
import com.drinkIt.dto.user.UserResponse;
import com.drinkIt.dto.user.UserUpdateRequest;
import com.drinkIt.entity.Address;
import com.drinkIt.entity.User;
import com.drinkIt.repository.AddressRepository;
import com.drinkIt.repository.UserRepository;
import com.drinkIt.security.CurrentUserService;
import com.drinkIt.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository ;

    private final AddressRepository addressRepository;

    private final CurrentUserService currentUserService;

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

   @Override
public UserAddressResponse saveAddress(
        UserAddressSaveRequest request,
        Authentication authentication
) {

    String email = authentication.getName();

    User user = userRepository
            .findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found")
            );

    Address address = Address.builder()
            .user(user)
            .addressType(request.getAddressType())
            .fullName(request.getFullName())
            .phone(request.getPhone())
            .addressLine(request.getAddressLine())
            .city(request.getCity())
            .state(request.getState())
            .pincode(request.getPincode())
            .isDefault(false)
            .build();

    Address savedAddress =
            addressRepository.save(address);

    return new UserAddressResponse(
            savedAddress.getId(),
            savedAddress.getAddressType(),
            savedAddress.getFullName(),
            savedAddress.getPhone(),
            savedAddress.getAddressLine(),
            savedAddress.getCity(),
            savedAddress.getState(),
            savedAddress.getPincode()
    );
}

     @Override
     public UserAddressResponse updateAddress( Long addressId, UserAddressUpdateRequest request, Authentication authentication) {

    User user = userRepository
            .findByEmail(authentication.getName())
            .orElseThrow(() ->new RuntimeException("User not found"));

    Address address = addressRepository
            .findByIdAndUser(addressId, user)
            .orElseThrow(() -> new RuntimeException("Address not found"));

    if (request.getAddressType() != null) {
        address.setAddressType(request.getAddressType());
    }

    if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
        address.setFullName( request.getFullName().trim());
    }

    if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
        address.setPhone( request.getPhone().trim());
    }

    if (request.getAddressLine() != null && !request.getAddressLine().trim().isEmpty()) {
        address.setAddressLine( request.getAddressLine().trim());
    }

    if (request.getCity() != null && !request.getCity().trim().isEmpty()) {
        address.setCity( request.getCity().trim());
    }

    if (request.getState() != null && !request.getState().trim().isEmpty()) {
        address.setState( request.getState().trim());
    }

    if (request.getPincode() != null && !request.getPincode().trim().isEmpty()) {
        address.setPincode( request.getPincode().trim());
    }

    if (request.getIsDefault() != null) {
        address.setDefault(request.getIsDefault());
    }

    Address updatedAddress =
            addressRepository.save(address);

    return new UserAddressResponse(
            updatedAddress.getId(),
            updatedAddress.getAddressType(),
            updatedAddress.getFullName(),
            updatedAddress.getPhone(),
            updatedAddress.getAddressLine(),
            updatedAddress.getCity(),
            updatedAddress.getState(),
            updatedAddress.getPincode()
    );
}


          @Override
          public void deleteAddress( Long addressId, Authentication authentication) {

             User user = userRepository
                          .findByEmail(authentication.getName())
                          .orElseThrow(() -> new RuntimeException("User not found"));

            Address address = addressRepository
                    .findByIdAndUser(addressId, user)
                    .orElseThrow(() -> new RuntimeException("Address not found"));

            addressRepository.delete(address);
        }


        @Override
public List<UserAddressResponse> getAddresses(
        Authentication authentication
) {

    User user = userRepository
            .findByEmail(authentication.getName())
            .orElseThrow(() ->
                    new RuntimeException("User not found")
            );

    List<Address> addresses =
            addressRepository.findByUser(user);

    return addresses.stream()
            .map(address -> new UserAddressResponse(
                    address.getId(),
                    address.getAddressType(),
                    address.getFullName(),
                    address.getPhone(),
                    address.getAddressLine(),
                    address.getCity(),
                    address.getState(),
                    address.getPincode()
            ))
            .toList();
}


}
