package com.rakbank.userservice.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDetailsResponseDto {

    private Long id;
    private String name;
    private String email;

    // This field is only added for assessment purpose. Will never be in a live project.
    private String password;


}
