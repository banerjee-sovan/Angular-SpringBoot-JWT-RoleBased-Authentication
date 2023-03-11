package com.ibm.jwt.controller;

import com.ibm.jwt.dto.JwtRequest;
import com.ibm.jwt.dto.JwtResponse;
import com.ibm.jwt.service.JwtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoginController {
    @Autowired
    private JwtUserService jwtUserService;

    @PostMapping("/authenticate")
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return jwtUserService.createJwtToken(jwtRequest);
    }
}
