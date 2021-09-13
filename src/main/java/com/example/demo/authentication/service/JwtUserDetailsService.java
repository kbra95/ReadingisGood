package com.example.demo.authentication.service;

import com.example.demo.authentication.model.SignupRequest;
import com.example.demo.exceptions.GeneralException;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }

    public SignupRequest save(SignupRequest signupRequest) {
        User newUser = new User();
        newUser.setUsername(signupRequest.getUsername());
        newUser.setPassword(bcryptEncoder.encode(signupRequest.getPassword()));
        newUser.setFirstName(signupRequest.getFirstName());
        newUser.setLastName(signupRequest.getLastName());
        newUser.setEmail(signupRequest.getEmail());
        newUser.setAddress(signupRequest.getAddress());
        if(userRepository.existsByUsername(signupRequest.getUsername()) ){
            throw new GeneralException("Given username is already exist", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())){
            throw new GeneralException("Given email is already exist", HttpStatus.BAD_REQUEST);
        }
        User savedUser = userRepository.save(newUser);
        return new SignupRequest(savedUser.getUsername(),savedUser.getPassword()
                ,savedUser.getEmail(),savedUser.getFirstName(),savedUser.getLastName(),savedUser.getAddress());
    }
}
