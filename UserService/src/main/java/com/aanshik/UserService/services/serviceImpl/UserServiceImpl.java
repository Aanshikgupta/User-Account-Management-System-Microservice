package com.aanshik.UserService.services.serviceImpl;

import com.aanshik.UserService.enitity.User;
import com.aanshik.UserService.exceptionhandling.ResourceNotFoundException;
import com.aanshik.UserService.payloads.AccountDto;
import com.aanshik.UserService.payloads.UserDto;
import com.aanshik.UserService.respositories.UserRepo;
import com.aanshik.UserService.services.UserServices;
import com.aanshik.UserService.utils.Constants;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
public class UserServiceImpl implements UserServices {


    @Autowired
    UserRepo userRepo;


    @Autowired
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    RestTemplate restTemplate;


    public UserServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    @Caching(evict = {@CacheEvict(value = Constants.USER_DTO, allEntries = true),})
    public UserDto createUser(UserDto userDto) {

        User userToBeSaved = modelMapper.map(userDto, User.class);

        //generating random userId
        String userId = UUID.randomUUID().toString();
        userToBeSaved.setUserId(userId);

        //save user to db
        Integer aff = userRepo.createUser(userToBeSaved);

        //if user is successfully inserted
        if (aff >= 1) {
            //user will have automatic account creation for the first time
            long initBalance = Constants.INITIAL_BALANCE;
            createAccountForUser(userId, initBalance);
        } else {
            return null;
        }

        //return the dto form of the user created
        return modelMapper.map(userToBeSaved, UserDto.class);

    }


    @Override
    @Cacheable(value = Constants.USER_DTO, key = "#userId")
    public UserDto getUserById(String userId) {

        User userRetrieved = userRepo.getUserById(userId);

        //if user not present
        if (userRetrieved == null) {
            throw new ResourceNotFoundException(Constants.USER, userId);
        }

        //convert to dto
        UserDto userRetrievedDto = modelMapper.map(userRetrieved, UserDto.class);

        return userRetrievedDto;

    }


    @Override
    public List<UserDto> getAllUsers() {

        List<User> users = userRepo.getAllUsers();

        if (users == null) {
            return new ArrayList<UserDto>();
        }

        return convertUserListToDtoList(users);

    }


    @Override
    @Caching(evict = {@CacheEvict(value = Constants.ACCOUNT_DTO, allEntries = true), @CacheEvict(value = Constants.ACCOUNT_USER_DTO, allEntries = true),}, put = {@CachePut(value = Constants.USER_DTO, key = "#userId")})
    public UserDto updateUser(String userId, UserDto userDto) {

        User userPresent = userRepo.getUserById(userId);
        if (userPresent == null) {
            throw new ResourceNotFoundException(Constants.USER, userId);
        }

        User userUpdate = modelMapper.map(userDto, User.class);

        //update user
        int aff = userRepo.updateUser(userId, userUpdate);

        UserDto updatedDto = modelMapper.map(userUpdate, UserDto.class);
        updatedDto.setUserId(userId);

        return updatedDto;

    }


    @Override
    @Caching(evict = {@CacheEvict(value = Constants.ACCOUNT_DTO, allEntries = true), @CacheEvict(value = Constants.ACCOUNT_USER_DTO, allEntries = true), @CacheEvict(value = Constants.USER_DTO, allEntries = true),})
    public boolean deleteUser(String userId) {

        userExistsOrNot(userId);

        //deleting user would delete all his/her accounts
        //currently handled by Foreign Key Relationship


        Integer aff = userRepo.deleteUser(userId);

        if (aff >= 1) {
            return true;
        } else {
            return false;
        }

    }


    @Override
    public AccountDto createAccount(String userId, AccountDto accountDto) {

        userExistsOrNot(userId);
        return createAccountForUser(userId, accountDto.getBalance());

    }


    //delete all accounts of the user if the user is deleted.
    private void deleteAllAccountsForUser(String userId) {

        restTemplate.delete(Constants.ACCOUNT_SERVICE_BASE_URL_WITH_SLASH_USERS_SLASH + userId);

    }


    //safe check for user presence
    private void userExistsOrNot(String userId) {

        UserDto userDto = getUserById(userId);
        if (userDto == null) {
            throw new ResourceNotFoundException(Constants.USER, userId);
        }

    }


    //create an account for a user when user is created
    private AccountDto createAccountForUser(String userId, long initBalance) {

        try {
            AccountDto accountDto = new AccountDto(userId, initBalance);
            accountDto = restTemplate.postForEntity(Constants.ACCOUNT_SERVICE_BASE_URL, accountDto, AccountDto.class).getBody();
            return accountDto;
        } catch (IllegalStateException e) {
            deleteUser(userId);
            throw new IllegalStateException();
        }

    }


    //return all the accounts by userId
    public List<AccountDto> getAllAccounts(String userId) {

        userExistsOrNot(userId);
        AccountDto[] accountDtos = restTemplate.getForObject(Constants.ACCOUNT_SERVICE_BASE_URL_WITH_SLASH_USERS_SLASH + userId, AccountDto[].class);
        List<AccountDto> accounts = Arrays.asList(accountDtos);
        return accounts;

    }


    public List<UserDto> convertUserListToDtoList(List<User> users) {

        return users.stream()
                .map((user) -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

    }


}
