package com.rakbank.userservice.service;

import com.rakbank.userservice.controller.dto.request.CreateUserRequestDto;
import com.rakbank.userservice.controller.dto.request.ModifyUserRequestDto;
import com.rakbank.userservice.dao.entity.UserEntity;
import com.rakbank.userservice.dao.repository.UserRepository;
import com.rakbank.userservice.error.DomainException;
import com.rakbank.userservice.service.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;


    @Test
    void shouldThrowExceptionWhenUserSignupWithExistingEmail() {

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new UserEntity()));

        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("Deep");
        createUserRequestDto.setEmail("existing@gmail.com");
        createUserRequestDto.setPassword("password");

        Assertions.assertThrows(DomainException.class, () -> userService.create(createUserRequestDto));

    }

    @Test
    void shouldCreateUserSuccessfullyWhenAllDetailsAreOk() {


        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn(anyString());

        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("Deep");
        createUserRequestDto.setEmail("existing@gmail.com");
        createUserRequestDto.setPassword("password");

        User user = userService.create(createUserRequestDto);

        Assertions.assertEquals(user.getName(), "Deep");
        Assertions.assertEquals(user.getEmail(), "existing@gmail.com");

    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(DomainException.class, () -> userService.findById(1L));
    }

    @Test
    void shouldReturnUserWhenIdFound() {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Deep");
        userEntity.setEmail("test@gmail.com");
        when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));

        User user = userService.findById(1L);
        Assertions.assertEquals(user.getName(), "Deep");
        Assertions.assertEquals(user.getEmail(), "test@gmail.com");
        Assertions.assertEquals(user.getId(), 1L);

    }

    @Test
    void shouldUpdateUserWhenAllDetailsAreCorrect() {

        UserEntity existingUser = new UserEntity();
        existingUser.setName("Deep");
        existingUser.setEmail("test@gmail.com");

        when(userRepository.findById(any())).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        ModifyUserRequestDto modifyUserRequestDto = new ModifyUserRequestDto();
        modifyUserRequestDto.setName("new name");
        modifyUserRequestDto.setEmail("updated@gmail.com");

        User user = userService.updateUser(1L, modifyUserRequestDto);

        Assertions.assertEquals(user.getName(), "new name");
        Assertions.assertEquals(user.getEmail(), "updated@gmail.com");

    }

    @Test
    void shouldThrowErrorIfUserUpdatesWithExitingEmail() {

        UserEntity existingUser = new UserEntity();
        existingUser.setName("Deep");
        existingUser.setEmail("test@gmail.com");

        when(userRepository.findById(any())).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new UserEntity()));

        ModifyUserRequestDto modifyUserRequestDto = new ModifyUserRequestDto();
        modifyUserRequestDto.setName("new name");
        modifyUserRequestDto.setEmail("updated@gmail.com");

        Assertions.assertThrows(DomainException.class, ()
                -> userService.updateUser(1L, modifyUserRequestDto));

    }

}
