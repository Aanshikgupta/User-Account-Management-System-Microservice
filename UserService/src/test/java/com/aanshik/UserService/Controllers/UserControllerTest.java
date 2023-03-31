package com.aanshik.UserService.Controllers;

import com.aanshik.UserService.Payloads.AccountDto;
import com.aanshik.UserService.Payloads.UserDto;
import com.aanshik.UserService.Services.ServiceImpl.UserServiceImpl;
import com.aanshik.UserService.Services.UserServices;
import com.aanshik.UserService.UserServiceApplication;
import com.aanshik.UserService.Utils.AbstractTest;
import com.netflix.discovery.converters.Auto;
import org.apache.catalina.security.SecurityConfig;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

//    @InjectMocks
//    UserController userController;
//
//    UserServices userServices;



//    private MockMvc mockMvc;
//
//
//    String userId;
//    AccountDto accountDto;
//



    @Before
    public void setup() {
//        userController = new UserController();
//        userController.setUserService(userServices);
//        accountDto = new AccountDto("1", "2", 100L);
    }

    @Test
    void createUser() {

    }

    @Test
    void createAccount() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void getAllUsers() throws Exception {
//        String uri = "/users";
//        userServices=mock(UserServiceImpl.class);
//        ResponseEntity<List<UserDto>> users=userController.getAllUsers();
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
//                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
//
//        Mockito.when(userServices.createAccount(Mockito.any(String.class), Mockito.any(AccountDto.class))).thenReturn(accountDto);
//
//        int status = mvcResult.getResponse().getStatus();
//        assertEquals(200, status);
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}