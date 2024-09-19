package com.rakbank.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakbank.userservice.controller.dto.request.CreateUserRequestDto;
import com.rakbank.userservice.controller.dto.request.ModifyUserRequestDto;
import com.rakbank.userservice.error.DomainException;
import com.rakbank.userservice.service.api.UserService;
import com.rakbank.userservice.service.model.PaginatedResponse;
import com.rakbank.userservice.service.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    /**
     * Should Receive HTTP 400 Bad Request when name is blank in signup request.
     * @throws Exception
     */
    @Test
    void shouldThrowBadRequestIfNameBlankInSignUp() throws Exception {

        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("");
        createUserRequestDto.setEmail("test@gmail.com");
        createUserRequestDto.setPassword("password");

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(createUserRequestDto);

        this.mockMvc.perform(
                post("/users")
                    .content(requestJson)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Should Receive HTTP 400 Bad Request when name is invalid format in signup request.
     * Name to only contain characters and space.
     * @throws Exception
     */
    @Test
    void shouldThrowBadRequestIfNameInvalidInSignUp() throws Exception {

        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("Deep*");
        createUserRequestDto.setEmail("test@gmail.com");
        createUserRequestDto.setPassword("password");

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(createUserRequestDto);

        this.mockMvc.perform(
                        post("/users")
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Should Receive HTTP 400 Bad Request when name is too long (more than max length) in signup request.
     * Name to only contain characters and space.
     * @throws Exception
     */
    @Test
    void shouldThrowBadRequestIfNameTooLongInSignUp() throws Exception {

        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("Deeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeep");
        createUserRequestDto.setEmail("test@gmail.com");
        createUserRequestDto.setPassword("password");

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(createUserRequestDto);

        this.mockMvc.perform(
                        post("/users")
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Should Receive HTTP 400 Bad Request when email is blank in signup request.
     * Name to only contain characters and space.
     * @throws Exception
     */
    @Test
    void shouldThrowBadRequestIfEmailBlankInSignUp() throws Exception {

        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("Deep");
        createUserRequestDto.setEmail("");
        createUserRequestDto.setPassword("password");

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(createUserRequestDto);

        this.mockMvc.perform(
                        post("/users")
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Should Receive HTTP 400 Bad Request when email is invalid in signup request.
     * Name to only contain characters and space.
     * @throws Exception
     */
    @Test
    void shouldThrowBadRequestIfEmailInvalidInSignUp() throws Exception {

        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("Deep");
        createUserRequestDto.setEmail("test@");
        createUserRequestDto.setPassword("password");

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(createUserRequestDto);

        this.mockMvc.perform(
                        post("/users")
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Should Receive HTTP 400 Bad Request when password is blank in signup request.
     * Name to only contain characters and space.
     * @throws Exception
     */
    @Test
    void shouldThrowBadRequestIfPasswordBlankInSignUp() throws Exception {

        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("Deep");
        createUserRequestDto.setEmail("test@gmail.com");
        createUserRequestDto.setPassword("");

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(createUserRequestDto);

        this.mockMvc.perform(
                        post("/users")
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Should Receive HTTP 400 Bad Request when password is invalid (contains other than alphabets or numbers) in signup request.
     * Name to only contain characters and space.
     * @throws Exception
     */
    @Test
    void shouldThrowBadRequestIfPasswordInvalidInSignUp() throws Exception {

        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("Deep");
        createUserRequestDto.setEmail("test@gmail.com");
        createUserRequestDto.setPassword("P@ssword");

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(createUserRequestDto);

        this.mockMvc.perform(
                        post("/users")
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Should Receive HTTP 400 Bad Request when password is less than min length in signup request.
     * Name to only contain characters and space.
     * @throws Exception
     */
    @Test
    void shouldThrowBadRequestIfPasswordTooSmallInSignUp() throws Exception {

        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("Deep");
        createUserRequestDto.setEmail("test@gmail.com");
        createUserRequestDto.setPassword("pass");

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(createUserRequestDto);

        this.mockMvc.perform(
                        post("/users")
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Should Receive HTTP 200 OK when all details are correct.
     * Name to only contain characters and space.
     * @throws Exception
     */
    @Test
    void shouldSignupSuccessfullyWhenAllDetailsOK() throws Exception {

        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("Deep");
        createUserRequestDto.setEmail("test@gmail.com");
        createUserRequestDto.setPassword("password");

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(createUserRequestDto);

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("Deep");
        mockUser.setEmail("test@gmail.com");

        when(userService.create(any())).thenReturn(mockUser);

        this.mockMvc.perform(
                post("/users")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));

    }

    @Test
    void shouldGetUserById() throws Exception {

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("Deep");
        mockUser.setEmail("test@gmail.com");

        when(userService.findById(1L)).thenReturn(mockUser);

        MvcResult result = this.mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Deep"))
                .andReturn();

    }

    @Test
    void shouldThrowNotFoundWhenGetUserForInvalidId() throws Exception {

        when(userService.findById(1L)).thenThrow(new DomainException("user.notExist", HttpStatus.NOT_FOUND));

        this.mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("No user found"))
                .andReturn();

    }

    @Test
    void shouldReturnUserPaginatedListWhenFindAll() throws Exception {

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("Deep");
        mockUser.setEmail("test@gmail.com");

        PaginatedResponse<User> mockPaginatedResponse = new PaginatedResponse<>();
        mockPaginatedResponse.setTotalPages(1);
        mockPaginatedResponse.setTotalRecords(1L);
        mockPaginatedResponse.setPageSize(10);
        mockPaginatedResponse.setPageNumber(1);
        mockPaginatedResponse.setRecords(Collections.singletonList(mockUser));

        when(userService.findAll(1)).thenReturn(mockPaginatedResponse);

        this.mockMvc.perform(get("/users?page=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRecords").value("1"))
                .andExpect(jsonPath("$.totalPages").value("1"))
                .andExpect(jsonPath("$.pageNumber").value("1"))
                .andExpect(jsonPath("$.pageSize").value("10"))
                .andReturn();

    }

    @Test
    void shouldReturnUserPaginatedListWhenFindAllWithNoPageNumber() throws Exception {

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("Deep");
        mockUser.setEmail("test@gmail.com");

        PaginatedResponse<User> mockPaginatedResponse = new PaginatedResponse<>();
        mockPaginatedResponse.setTotalPages(1);
        mockPaginatedResponse.setTotalRecords(1L);
        mockPaginatedResponse.setPageSize(10);
        mockPaginatedResponse.setPageNumber(1);
        mockPaginatedResponse.setRecords(Collections.singletonList(mockUser));

        when(userService.findAll(1)).thenReturn(mockPaginatedResponse);

        this.mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRecords").value("1"))
                .andExpect(jsonPath("$.totalPages").value("1"))
                .andExpect(jsonPath("$.pageNumber").value("1"))
                .andExpect(jsonPath("$.pageSize").value("10"))
                .andReturn();

        }

    @Test
    void shouldUpdateUserSuccessfullyWhenAllDetailsOk() throws Exception {

        ModifyUserRequestDto modifyUserRequestDto = new ModifyUserRequestDto();
        modifyUserRequestDto.setName("new name");
        modifyUserRequestDto.setEmail("test@gmail.com");

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(modifyUserRequestDto);

        User modifiedUser = new User();
        modifiedUser.setId(1L);
        modifiedUser.setName("new name");
        modifiedUser.setEmail("test@gmail.com");

        when(userService.updateUser(any(),any())).thenReturn(modifiedUser);

        this.mockMvc.perform(
                        put("/users/1")
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("new name"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"));

    }
}
