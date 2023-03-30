package com.aanshik.AccountService.ControllersTest;

import com.aanshik.AccountService.Respositories.AccountRepo;
import com.aanshik.AccountService.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class ControllerClassTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountRepo accountRepo;






}
