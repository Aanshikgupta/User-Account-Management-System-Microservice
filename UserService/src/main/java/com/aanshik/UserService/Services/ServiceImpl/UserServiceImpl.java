package com.aanshik.UserService.Services.ServiceImpl;

import com.aanshik.UserService.Enitity.User;
import com.aanshik.UserService.ExceptionHandling.ResourceNotFoundException;
import com.aanshik.UserService.Payloads.AccountDto;
import com.aanshik.UserService.Payloads.UserDto;
import com.aanshik.UserService.Respositories.UserRepo;
import com.aanshik.UserService.Services.UserServices;
import com.aanshik.UserService.Utils.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserServices {

    @Autowired
    UserRepo userRepo;


    @Autowired
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    RestTemplate restTemplate;


    @Override
    @Caching(evict = {
            @CacheEvict(value = "user-dto", allEntries = true),})
    public UserDto createUser(UserDto userDto) {

        User userToBeSaved = this.modelMapper.map(userDto, User.class);

        //generating random userId
        String userId = UUID.randomUUID().toString();
        userToBeSaved.setUserId(userId);

        //save user to db
        Integer aff = userRepo.createUser(userToBeSaved);

        //if user is successfully inserted
        if (aff >= 1) {
            //user will have automatic account creation for the first time
            long initBalance = 1000L;
            createAccountForUser(userId, initBalance);
        } else {
            return null;
        }

        //return the dto form of the user created
        return this.modelMapper.map(userToBeSaved, UserDto.class);
    }


    @Override
    @Cacheable(value = "user-dto", key = "#userId")
    public UserDto getUserById(String userId) {

        User userRetrieved = userRepo.getUserById(userId);

        //if user not present
        if (userRetrieved == null) {
            throw new ResourceNotFoundException("User", userId);
        }

        //convert to dto
        UserDto userRetrievedDto = this.modelMapper.map(userRetrieved, UserDto.class);

        return userRetrievedDto;
    }


    //TODO:Not required in redis
    @Override
    public List<UserDto> getAllUsers() {

        List<User> users = userRepo.getAllUsers();

        if (users == null) {
            return new ArrayList<UserDto>();
        }

        return convertUserListToDtoList(users);

    }


    @Override
    @Caching(evict = {
            @CacheEvict(value = "account-dto", allEntries = true),
            @CacheEvict(value = "account-user-dto", allEntries = true),},
            put = {@CachePut(value = "user-dto", key = "#userId")})
    public UserDto updateUser(String userId, UserDto userDto) {

        User userPresent = userRepo.getUserById(userId);
        if (userPresent == null) {
            throw new ResourceNotFoundException("User", userId);
        }

        User userUpdate = this.modelMapper.map(userDto, User.class);

        //update user
        int aff = userRepo.updateUser(userId, userUpdate);

        UserDto updatedDto = this.modelMapper.map(userUpdate, UserDto.class);
        updatedDto.setUserId(userId);

        return updatedDto;
    }


    @Override
    @Caching(evict = {
            @CacheEvict(value = "account-dto", allEntries = true),
            @CacheEvict(value = "account-user-dto", allEntries = true),
            @CacheEvict(value = "user-dto", allEntries = true),})
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
        restTemplate.delete(Constants.ACCOUNT_SERVICE_BASE_URL + "/users/" + userId);
    }


    //safe check for user presence
    private void userExistsOrNot(String userId) {
        UserDto userDto = getUserById(userId);
        if (userDto == null) {
            throw new ResourceNotFoundException("User", userId);
        }
    }


    //create an account for a user when user is created
    private AccountDto createAccountForUser(String userId, long initBalance) {
        // TOOD:RESET HERE
//        try {
        AccountDto accountDto = new AccountDto(userId, initBalance);
        accountDto = restTemplate.postForEntity(Constants.ACCOUNT_SERVICE_BASE_URL, accountDto, AccountDto.class).getBody();
        return accountDto;
//        } catch (HttpClientErrorException exception) {
//            throw new ResourceNotFoundException("User", userId);
//        }
    }


    //return all the accounts by userId
    public List<AccountDto> getAllAccounts(String userId) {
        userExistsOrNot(userId);
        AccountDto[] accountDtos = restTemplate.getForObject(Constants.ACCOUNT_SERVICE_BASE_URL + "/users/" + userId, AccountDto[].class);
        List<AccountDto> accounts = Arrays.asList(accountDtos);
        return accounts;
    }

    public List<UserDto> convertUserListToDtoList(List<User> users) {
        return users.stream()
                .map((user) -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }


}
