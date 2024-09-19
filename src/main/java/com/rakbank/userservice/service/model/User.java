package com.rakbank.userservice.service.model;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String name;
    private String email;

    // This field is only added for assessment purpose. Will never be in a live project.
    private String password;

}
