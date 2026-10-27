package com.micro.user.service;

import com.micro.user.dto.AddressDTO;
import com.micro.user.dto.UserRequest;
import com.micro.user.dto.UserResponse;
import com.micro.user.model.Address;
import com.micro.user.model.User;
import com.micro.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse addUser(UserRequest userRequest) {
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

        if (userRequest.getAddress() != null) {
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setCity(userRequest.getAddress().getCity());
            address.setState(userRequest.getAddress().getState());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setZipcode(userRequest.getAddress().getZipcode());
            user.setAddress(address);
        }

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    public UserResponse fetchUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToUserResponse)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public UserResponse updateUser(Long id, UserRequest updatedUserRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setFirstName(updatedUserRequest.getFirstName());
        existingUser.setLastName(updatedUserRequest.getLastName());
        existingUser.setEmail(updatedUserRequest.getEmail());
        existingUser.setPhone(updatedUserRequest.getPhone());

        if (updatedUserRequest.getAddress() != null) {
            if (existingUser.getAddress() == null) {
                Address newAddress = new Address();
                newAddress.setStreet(updatedUserRequest.getAddress().getStreet());
                newAddress.setCity(updatedUserRequest.getAddress().getCity());
                newAddress.setState(updatedUserRequest.getAddress().getState());
                newAddress.setCountry(updatedUserRequest.getAddress().getCountry());
                newAddress.setZipcode(updatedUserRequest.getAddress().getZipcode());

                existingUser.setAddress(newAddress);
            } else {
                existingUser.getAddress().setStreet(updatedUserRequest.getAddress().getStreet());
                existingUser.getAddress().setCity(updatedUserRequest.getAddress().getCity());
                existingUser.getAddress().setState(updatedUserRequest.getAddress().getState());
                existingUser.getAddress().setCountry(updatedUserRequest.getAddress().getCountry());
                existingUser.getAddress().setZipcode(updatedUserRequest.getAddress().getZipcode());
            }
        }

        User savedUser = userRepository.save(existingUser);
        return mapToUserResponse(savedUser);
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());

        if (user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setId(String.valueOf(user.getAddress().getId()));
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipcode(user.getAddress().getZipcode());

            userResponse.setAddress(addressDTO);
        }

        return userResponse;
    }
}