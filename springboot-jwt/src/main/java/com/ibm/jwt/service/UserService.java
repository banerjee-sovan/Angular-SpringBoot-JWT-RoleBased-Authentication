package com.ibm.jwt.service;

import com.ibm.jwt.entity.Role;
import com.ibm.jwt.entity.User;
import com.ibm.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    PasswordEncoder encoder;

    public User saveNewUser(User userobj){
        userobj.setPassword(this.encoder.encode(userobj.getPassword()));
        Role userRole=new Role();
        userRole.setRoleId(1);
        userRole.setRoleName("USER");
        userRole.setRoleDescription("Default User Role");
        Set<Role> roles=new HashSet<>();
        roles.add(userRole);
        userobj.setRoles(roles);
       return  repository.save(userobj);
    }
}
