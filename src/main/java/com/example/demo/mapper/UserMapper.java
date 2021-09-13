package com.example.demo.mapper;

import com.example.demo.model.entity.User;
import com.example.demo.model.dto.UserDTO;

public class UserMapper {

    public static UserDTO toUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setAddress(user.getAddress());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setId(user.getId());
        return userDTO;
    }
}
