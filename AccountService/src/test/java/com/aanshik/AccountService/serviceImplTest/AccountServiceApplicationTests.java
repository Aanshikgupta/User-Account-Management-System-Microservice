package com.aanshik.AccountService.serviceImplTest;

import com.aanshik.AccountService.respositories.AccountRepo;
import com.aanshik.AccountService.services.serviceImpl.AccountServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AccountServiceApplicationTests {

    @Mock
    AccountRepo accountRepo;

    @InjectMocks
    AccountServiceImpl accountService;



    @BeforeAll
    static void beforeAll() {

    }

    @Before
    public void init() {
        MockitoAnnotations.openMocks(AccountServiceImpl.class);
        accountRepo = mock(AccountRepo.class);
    }

    @Test
    void contextLoads() {
    }







}
