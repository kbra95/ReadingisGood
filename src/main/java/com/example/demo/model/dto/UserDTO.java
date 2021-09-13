package com.example.demo.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserDTO {
    private Long id;
    private String username;
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
}
