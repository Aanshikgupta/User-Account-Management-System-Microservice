package com.aanshik.UserService.controllers;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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