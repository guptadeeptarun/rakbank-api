package com.rakbank.userservice.controller;

import com.rakbank.userservice.controller.dto.request.ChangePasswordRequestDto;
import com.rakbank.userservice.controller.dto.request.CreateUserRequestDto;
import com.rakbank.userservice.controller.dto.request.ModifyUserRequestDto;
import com.rakbank.userservice.controller.dto.response.CreateUserResponseDto;
import com.rakbank.userservice.controller.dto.response.UserDetailsResponseDto;
import com.rakbank.userservice.service.api.UserService;
import com.rakbank.userservice.service.model.PaginatedResponse;
import com.rakbank.userservice.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public CreateUserResponseDto createUser(@Validated @RequestBody CreateUserRequestDto createUserRequestDto) {

        User user = userService.create(createUserRequestDto);
        return CreateUserResponseDto.builder()
                .id(user.getId())
                .build();
    }

    @GetMapping(path = "/{id}")
    public UserDetailsResponseDto getUserDetailsById(@PathVariable Long id) {

        User user = userService.findById(id);
        return mapUserToUserDetailsResponseDto(user);

    }

    @GetMapping
    public PaginatedResponse<UserDetailsResponseDto> getUsers(@RequestParam(required = false) Integer page) {

        // If the "page" param is missing, API will default it as 1.
        Integer pageNumber = Optional.ofNullable(page)
                .orElse(1);

        PaginatedResponse<User> userPage = userService.findAll(pageNumber);

        List<UserDetailsResponseDto> userDetailsResponseDtoList = new ArrayList<>();
        userPage.getRecords()
                .stream()
                .forEach(user -> userDetailsResponseDtoList.add(mapUserToUserDetailsResponseDto(user)));

        PaginatedResponse<UserDetailsResponseDto> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setRecords(userDetailsResponseDtoList);
        paginatedResponse.setPageSize(userPage.getPageSize());
        paginatedResponse.setPageNumber(userPage.getPageNumber());
        paginatedResponse.setTotalRecords(userPage.getTotalRecords());
        paginatedResponse.setTotalPages(userPage.getTotalPages());

        return paginatedResponse;
    }

    @PatchMapping(path="/{id}")
    public void changePassword(@PathVariable Long id,
                               @Validated @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {

        userService.changePassword(id, changePasswordRequestDto.getPassword());
    }

    @PutMapping(path="/{id}")
    public UserDetailsResponseDto modifyUser(@PathVariable Long id,
                               @Validated @RequestBody ModifyUserRequestDto modifyUserRequestDto) {

        User user = userService.updateUser(id, modifyUserRequestDto);
        return mapUserToUserDetailsResponseDto(user);
    }

    @DeleteMapping(path="/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }


    private UserDetailsResponseDto  mapUserToUserDetailsResponseDto(User user) {
        return UserDetailsResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .password(user.getPassword()) // password only included for assessment purpose.
                .email(user.getEmail())
                .build();
    }

}
