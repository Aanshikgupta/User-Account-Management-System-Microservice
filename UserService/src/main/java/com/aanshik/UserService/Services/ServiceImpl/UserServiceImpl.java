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
    public Integer createUser(UserDto userDto) {
        User userToBeSaved = this.modelMapper.map(userDto, User.class);
        String userId = UUID.randomUUID().toString();

        //user will have automatic account creation
        userToBeSaved.setUserId(userId);
        Integer aff = userRepo.createUser(userToBeSaved);

        if (aff >= 1) {
            automaticAccountCreationForUser(userId);
        }

        return aff;
    }

    private void automaticAccountCreationForUser(String userId) {
        AccountDto accountDto = new AccountDto(userId, 1000L);
        restTemplate.postForEntity(Constants.ACCOUNT_SERVICE_BASE_URL, accountDto, Integer.class).getBody();
    }

    @Override
    @Cacheable(value = "user-dto", key = "#userId")
    public UserDto getUserById(String userId) {
        User userRetrieved = userRepo.getUserById(userId);
        System.out.println("Called");
        if (userRetrieved == null) {
            throw new ResourceNotFoundException("User", userId);
        }

        UserDto userRetrievedDto = this.modelMapper.map(userRetrieved, UserDto.class);


        return userRetrievedDto;
    }


    public List<AccountDto> getAllAccounts(String userId) {
        AccountDto[] accountDtos = restTemplate.getForObject(Constants.ACCOUNT_SERVICE_BASE_URL + "/users/" + userId, AccountDto[].class);
        List<AccountDto> accounts = Arrays.asList(accountDtos);
        return accounts;
    }

    @Override
    @Cacheable(value = "usersList-dto")
    public List<UserDto> getAllUsers() {
        System.out.println("GEt all users called");
        try {
            List<User> usersList = userRepo.getAllUsers();
            if (usersList == null) {
                return new ArrayList<UserDto>();
            }
            return usersList.stream().map((user) -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<>();
        }

    }

    @Override
    @Caching(evict = {@CacheEvict(value = "usersList-dto", allEntries = true),})
    public Integer updateUser(String userId, UserDto userDto) {
        User userPresent = userRepo.getUserById(userId);
        if (userPresent == null) {
            throw new ResourceNotFoundException("User", userId);
        }
        User userUpdate = this.modelMapper.map(userDto, User.class);
        return userRepo.updateUser(userId, userUpdate);
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "usersList-dto", allEntries = true), @CacheEvict(value = "user-dto", allEntries = true),})
    public Integer deleteUser(String userId) {
        User userPresent = userRepo.getUserById(userId);

        //deleting user would delete all his/her accounts

        return userRepo.deleteUser(userId);
    }

    private void deleteAllAccountsForUser(String userId) {
        restTemplate.delete(Constants.ACCOUNT_SERVICE_BASE_URL + "/users/" + userId);
    }


}
