package com.ibm.jwt.filter;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.jwt.service.JwtUserService;
import com.ibm.jwt.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private JwtUserService jwtUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String header=request.getHeader("Authorization");
        String jwtToken=null;
        String username=null;
        if(header!=null && header.startsWith("Bearer ")) {
            jwtToken=header.substring(7);
            try {
                username=jwtUtility.getUsernameFromToken(jwtToken);


            }catch(IllegalArgumentException e) {
                System.out.println("Unable to get JWT token");
            }catch(ExpiredJwtException e) {
                System.out.println("JWT token expired");
            }
        }else {
            System.out.println("JWT token does not start with Bearer");
        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
            UserDetails userDetails=jwtUserService.loadUserByUsername(username);
            if(jwtUtility.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);

    }

}
