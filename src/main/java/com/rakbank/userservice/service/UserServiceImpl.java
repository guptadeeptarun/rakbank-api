package com.rakbank.userservice.service;

import com.rakbank.userservice.controller.dto.request.CreateUserRequestDto;
import com.rakbank.userservice.controller.dto.request.ModifyUserRequestDto;
import com.rakbank.userservice.dao.entity.UserEntity;
import com.rakbank.userservice.dao.repository.UserRepository;
import com.rakbank.userservice.error.DomainException;
import com.rakbank.userservice.service.api.UserService;
import com.rakbank.userservice.service.model.PaginatedResponse;
import com.rakbank.userservice.service.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private final Log LOGGER = LogFactory.getLog(this.getClass());

    private int DEFAULT_PAGE_SIZE=10;

    /**
     * Method to create a new User.
     * @param createUserRequestDto
     * @return
     */
    @Override
    public User create(CreateUserRequestDto createUserRequestDto) {

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(createUserRequestDto.getEmail());

        if(userEntityOptional.isPresent()) {
            LOGGER.debug(String.format("User creation failed. Duplicate Email [%s]", createUserRequestDto.getEmail()));
            throw new DomainException("user.email.alreadyRegistered");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setName(createUserRequestDto.getName());
        userEntity.setPassword(passwordEncoder.encode(createUserRequestDto.getPassword()));
        userEntity.setEmail(createUserRequestDto.getEmail());

        userRepository.save(userEntity);
        LOGGER.debug(String.format("User created successfully. Email [%s]", createUserRequestDto.getEmail()));

        return mapUserEntityToUser(userEntity);
    }

    /**
     * Method to get user by ID.
     * @param id
     * @return
     */
    @Override
    public User findById(Long id) {

        Optional<UserEntity> userEntityOptional = userRepository.findById(id);

        if(!userEntityOptional.isPresent()) {
            throw new DomainException("user.notExist", HttpStatus.NOT_FOUND);
        }

        return mapUserEntityToUser(userEntityOptional.get());
    }

    /**
     * Method to get all users for the given page number.
     * @param pageNumber
     * @return
     */
    @Override
    public PaginatedResponse<User> findAll(Integer pageNumber) {

        if(pageNumber < 1L) {
            throw new DomainException("Invalid Page Number. Must be minimum 1");
        }

        Pageable page = PageRequest.of(pageNumber-1, DEFAULT_PAGE_SIZE);
        Page<UserEntity> userEntityPage = userRepository.findAll(page);

        List<User> users = userEntityPage.stream()
                .map(this::mapUserEntityToUser)
                .collect(Collectors.toList());

        PaginatedResponse<User> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setRecords(users);
        paginatedResponse.setPageSize(DEFAULT_PAGE_SIZE);
        paginatedResponse.setPageNumber(pageNumber);
        paginatedResponse.setTotalRecords(userEntityPage.getTotalElements());
        paginatedResponse.setTotalPages(userEntityPage.getTotalPages());

        return paginatedResponse;
    }

    /**
     * Method to change user password.
     * @param userId
     * @param newPassword
     */
    @Override
    public void changePassword(Long userId, String newPassword) {

        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);

        if(!userEntityOptional.isPresent()) {
            throw new DomainException("user.notExist", HttpStatus.NOT_FOUND);
        }

        UserEntity userEntity = userEntityOptional.get();
        userEntity.setPassword(passwordEncoder.encode(newPassword));

        try {
            userRepository.save(userEntity);
        } catch(ObjectOptimisticLockingFailureException optimisticEntityLockException) {
            throw new DomainException("concurrentModificationError");
        }
    }

    /**
     * Method to modify user details.
     * @param userId
     * @param modifyUserRequestDto
     * @return
     */
    @Override
    public User updateUser(Long userId, ModifyUserRequestDto modifyUserRequestDto) {

        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);

        if(!userEntityOptional.isPresent()) {
            throw new DomainException("user.notExist", HttpStatus.NOT_FOUND);
        }

        UserEntity userEntity = userEntityOptional.get();

        // If user has modified the email, verify that it must be unique.
        if(!userEntity.getEmail().equals(modifyUserRequestDto.getEmail())) {
            userEntityOptional = userRepository.findByEmail(modifyUserRequestDto.getEmail());
            if(userEntityOptional.isPresent()) {
                LOGGER.debug(String.format("User update failed. Duplicate Email [%s]", modifyUserRequestDto.getEmail()));
                throw new DomainException("user.email.alreadyRegistered");
            }
        }

        userEntity.setName(modifyUserRequestDto.getName());
        userEntity.setEmail(modifyUserRequestDto.getEmail());

        try {
            userRepository.save(userEntity);
        } catch(ObjectOptimisticLockingFailureException optimisticEntityLockException) {
            throw new DomainException("concurrentModificationError");
        }

        return mapUserEntityToUser(userEntity);
    }

    @Override
    public void deleteUser(Long userId) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);

        if(!userEntityOptional.isPresent()) {
            throw new DomainException("user.notExist", HttpStatus.NOT_FOUND);
        }

        userRepository.delete(userEntityOptional.get());
    }

    private User mapUserEntityToUser(UserEntity userEntity) {

        User user = new User();
        user.setId(userEntity.getId());
        user.setName(userEntity.getName());
        user.setEmail(userEntity.getEmail());

        // This field is only included for assessment. Will never be set in a live project.
        user.setPassword(userEntity.getPassword());
        return user;
    }

}
