package com.example.demo.service;

import com.example.demo.model.dto.UserDTO;
import com.example.demo.model.entity.User;

public interface UserService {

    User getUserByUserId(long id);

}
