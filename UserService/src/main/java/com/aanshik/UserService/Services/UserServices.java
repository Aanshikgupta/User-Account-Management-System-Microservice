package com.aanshik.UserService.Services;

import com.aanshik.UserService.Enitity.User;
import com.aanshik.UserService.Payloads.AccountDto;
import com.aanshik.UserService.Payloads.UserDto;

import java.util.List;

public interface UserServices {

    //Create User
    public UserDto createUser(UserDto userDto);

    //Get User by ID
    public UserDto getUserById(String userId);

    //get all Users
    public List<UserDto> getAllUsers();

    //Update User
    public UserDto updateUser(String userId, UserDto userDto);

    //Delete User
    public boolean deleteUser(String userId);

    public AccountDto createAccount(String userId,AccountDto accountDto);
}
