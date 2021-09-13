package com.example.demo.service.impl;

import com.example.demo.exceptions.GeneralException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserByUserId(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User is given with id " + id + " is not found", HttpStatus.NOT_FOUND));

    }
}
