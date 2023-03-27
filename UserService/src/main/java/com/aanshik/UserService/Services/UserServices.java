package com.aanshik.UserService.Services;

import com.aanshik.UserService.Enitity.User;
import com.aanshik.UserService.Payloads.UserDto;

import java.util.List;

public interface UserServices {

    //create
    public Integer createUser(UserDto userDto);
    //get User by ID
    public UserDto getUserById(String userId);
    //get all Users
    public List<UserDto> getAllUsers();
    //update User
    public Integer updateUser(String userId,UserDto userDto);
    //delete User
    public Integer deleteUser(String userId);
}
