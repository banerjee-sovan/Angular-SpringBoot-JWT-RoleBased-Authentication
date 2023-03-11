package com.ibm.jwt.controller;

import com.ibm.jwt.entity.User;
import com.ibm.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/registerNewUser")
    public User saveNewUser(@RequestBody User userobj){
       return  service.saveNewUser(userobj);
    }
}
