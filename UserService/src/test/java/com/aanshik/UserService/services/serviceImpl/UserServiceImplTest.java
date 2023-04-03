package com.aanshik.UserService.services.serviceImpl;

import com.aanshik.UserService.enitity.User;
import com.aanshik.UserService.payloads.AccountDto;
import com.aanshik.UserService.payloads.UserDto;
import com.aanshik.UserService.respositories.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RestTemplate restTemplate;

    private User user;

    private UserDto userDto;

    @BeforeEach
    void setUp() {

        modelMapper = new ModelMapper();
        userService = new UserServiceImpl(modelMapper);
        MockitoAnnotations.openMocks(this);

        user = new User("1", "Aanshik", "9026758004", new Date());
        userDto = new UserDto("1", "Aanshik", "9026758004", new Date());


        //user to dto
        given(modelMapper.map(Mockito.any(User.class), Mockito.eq(UserDto.class))).willReturn(userDto);

        //dto to user
        given(modelMapper.map(Mockito.any(UserDto.class), Mockito.eq(User.class))).willReturn(user);

    }


    @Test
    void createUser() throws Exception {

        AccountDto accountDto = new AccountDto("1", "2", 1000L);

        //Create Rest Template Account for User created
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(AccountDto.class), Mockito.eq(AccountDto.class))).thenReturn(new ResponseEntity<>(accountDto, HttpStatus.CREATED));

        Mockito.when(userRepo.createUser(Mockito.any(User.class))).thenReturn(1);

        UserDto createdUser = userService.createUser(userDto);

        assertEquals(userDto.getUserId(), createdUser.getUserId());
        assertEquals(userDto.getUserName(), createdUser.getUserName());
        assertEquals(userDto.getUserMobile(), createdUser.getUserMobile());
        assertEquals(userDto.getUserDob(), createdUser.getUserDob());


    }

    @Test
    void getUserById() {

        Mockito.when(userRepo.getUserById(Mockito.anyString())).thenReturn(user);

        UserDto userRecieved = userService.getUserById("1");

        assertEquals(userRecieved.getUserId(), user.getUserId());
        assertEquals(userRecieved.getUserMobile(), user.getUserMobile());
        assertEquals(userRecieved.getUserDob(), user.getUserDob());
        assertEquals(userRecieved.getUserName(), user.getUserName());

    }

    @Test
    void getAllUsers() {

        ArrayList<User> users = new ArrayList<User>();

        users.add(new User("1", "Aanshik", "9026758004", new Date()));
        users.add(new User("2", "Golu", "8127136701", new Date()));

        Mockito.when(userRepo.getAllUsers()).thenReturn(users);

        assertEquals(userService.getAllUsers().size(), 2);

    }

    @Test
    void updateUser() {

        UserDto updateUser = userDto;

        //for internal call in update user
        Mockito.when(userRepo.getUserById(Mockito.anyString())).thenReturn(user);

        //update
        Mockito.when(userRepo.updateUser(Mockito.anyString(), Mockito.any(User.class))).thenReturn(1);

        UserDto updatedUser = userService.updateUser("1", updateUser);

        assertEquals(updatedUser.getUserId(), updateUser.getUserId());
        assertEquals(updatedUser.getUserMobile(), updateUser.getUserMobile());
        assertEquals(updatedUser.getUserDob(), updateUser.getUserDob());
        assertEquals(updatedUser.getUserName(), updateUser.getUserName());

    }

    @Test
    void deleteUser() {

        //for internal call in update user
        Mockito.when(userRepo.getUserById(Mockito.anyString())).thenReturn(user);

        // delete user call
        Mockito.when(userRepo.deleteUser(Mockito.anyString())).thenReturn(1);

        Boolean deleteStatus = userService.deleteUser("1");

        assertEquals(deleteStatus, true);

    }


}