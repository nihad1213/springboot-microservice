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
            user.setAddress(mapToAddress(userRequest.getAddress()));
        }

        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    public UserResponse fetchUserById(String id) {
        return userRepository.findById(id)
                .map(this::mapToUserResponse)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + id)
                );
    }

    public UserResponse updateUser(
            String id,
            UserRequest updatedUserRequest
    ) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + id)
                );

        existingUser.setFirstName(updatedUserRequest.getFirstName());
        existingUser.setLastName(updatedUserRequest.getLastName());
        existingUser.setEmail(updatedUserRequest.getEmail());
        existingUser.setPhone(updatedUserRequest.getPhone());

        if (updatedUserRequest.getAddress() != null) {
            existingUser.setAddress(
                    mapToAddress(updatedUserRequest.getAddress())
            );
        }

        User savedUser = userRepository.save(existingUser);

        return mapToUserResponse(savedUser);
    }

    private Address mapToAddress(AddressDTO addressDTO) {
        Address address = new Address();

        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setZipcode(addressDTO.getZipcode());

        return address;
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());

        if (user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();

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