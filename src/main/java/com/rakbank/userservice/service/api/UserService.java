package com.rakbank.userservice.service.api;

import com.rakbank.userservice.controller.dto.request.CreateUserRequestDto;
import com.rakbank.userservice.controller.dto.request.ModifyUserRequestDto;
import com.rakbank.userservice.service.model.PaginatedResponse;
import com.rakbank.userservice.service.model.User;

public interface UserService {


    /**
     * Method to create a new user.
     * @param createUserRequestDto
     * @return
     */
    public User create(CreateUserRequestDto createUserRequestDto);

    /**
     * Method to find a user by the id as paramter.
     * @param id
     * @return
     */
    public User findById(Long id);

    /**
     * Method to find all users for the provided page number.
     * @param pageNumber
     * @return
     */
    public PaginatedResponse<User> findAll(Integer pageNumber);

    /**
     * Method to change the user password.
     * @param userId
     * @param newPassword
     */
    public void changePassword(Long userId, String newPassword);

    /**
     * Method to update user details.
     * @param modifyUserRequestDto
     */
    public User updateUser(Long userId, ModifyUserRequestDto modifyUserRequestDto);

    /**
     * Method to delete a user.
     * @param userId
     */
    public void deleteUser(Long userId);

}
