package com.rakbank.userservice.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Validated
public class ModifyUserRequestDto {

    @JsonProperty("name")
    @NotBlank(message="user.name.mandatory")
    @Pattern(regexp = "[a-zA-Z ]*", message="user.name.format")
    @Size(max=50, message="user.name.maxLength")
    private String name;

    @JsonProperty("email")
    @NotBlank(message = "user.email.mandatory")
    @Email(message = "user.email.format")
    private String email;

}
