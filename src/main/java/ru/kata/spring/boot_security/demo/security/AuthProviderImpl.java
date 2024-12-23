package ru.kata.spring.boot_security.demo.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.Collections;

@Component
public class AuthProviderImpl implements AuthenticationProvider {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public AuthProviderImpl(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        UserDetails userDetails = userServiceImpl.loadUserByUsername(userName);
        String password = authentication.getCredentials().toString();
        System.out.println("Attempting to authenticate user: " + userName);
        System.out.println("Provided password: " + password);
        System.out.println("Stored password (hashed): " + userDetails.getPassword());
        if (!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("Incorrect password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password,
                userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
