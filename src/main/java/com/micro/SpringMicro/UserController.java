package com.micro.SpringMicro;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
    private List<User> userList = new ArrayList<>();

    public List<User> getAllUsers() {
        return userList;
    }
}
