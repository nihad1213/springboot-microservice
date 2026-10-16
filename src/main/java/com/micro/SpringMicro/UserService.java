package com.micro.SpringMicro;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final List<User> userList = new ArrayList<>();

    public List<User> fetchAllUsers() {
        return userList;
    }

    public User addUser(User user) {
        userList.add(user);
        return user;
    }

    public User fetchUserById(Long id) {
        for (User user : userList) {
            if (user.getId().equals(id)) {
                return user;
            }
        }

        return null;
    }

    public User updateUser(Long id, User updatedUser) {
        for (User user : userList) {
            if (user.getId().equals(id)) {
                user.setFirstName(updatedUser.getFirstName());
                return user;
            }
        }

        return null;
    }
}