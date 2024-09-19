package com.rakbank.userservice.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Validated
public class ChangePasswordRequestDto {

    @JsonProperty("password")
    @NotBlank(message = "user.password.mandatory")
    @Size(min=8, message = "user.password.minLength")
    @Pattern(regexp = "[a-zA-Z0-9]*", message="user.password.format")
    private String password;
}
