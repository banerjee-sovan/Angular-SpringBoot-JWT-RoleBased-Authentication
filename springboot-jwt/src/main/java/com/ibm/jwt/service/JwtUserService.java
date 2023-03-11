package com.ibm.jwt.service;

import com.ibm.jwt.dto.JwtRequest;
import com.ibm.jwt.dto.JwtResponse;
import com.ibm.jwt.entity.User;
import com.ibm.jwt.repository.UserRepository;
import com.ibm.jwt.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
@Service
public class JwtUserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;


    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String userName = jwtRequest.getUsername();
        String userPassword = jwtRequest.getPassword();
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        UserDetails userDetails=(UserDetails) authentication.getPrincipal();

        //generate JWT Token
        String jwtToken = jwtUtility.generateToken(userDetails);

        User user = userRepository.findUserByUserName(userName);
        return new JwtResponse(user,jwtToken);

    }


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities(user));
    }


    @SuppressWarnings("unchecked")
    private Set getAuthorities(User user) {
        Set authorities= new HashSet<>();
        user.getRoles().forEach(role->{
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        });
        return authorities;
    }

//    private void authenticate(String userName,String userPassword) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
//        }catch(DisabledException e) {
//            throw new Exception("User is Disabled");
//        }catch(BadCredentialsException e) {
//            throw new Exception("Bad Credentials");
//        }
//    }
}
