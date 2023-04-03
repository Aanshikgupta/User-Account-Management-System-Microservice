package com.aanshik.AccountService.services.serviceImpl;

import com.aanshik.AccountService.enitity.Account;
import com.aanshik.AccountService.payloads.AccountDto;
import com.aanshik.AccountService.payloads.UserDto;
import com.aanshik.AccountService.respositories.AccountRepo;
import org.apache.catalina.User;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@RunWith(MockitoJUnitRunner.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepo accountRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RestTemplate restTemplate;

    private Account account;

    private AccountDto accountDto;

    private UserDto userDto;


    @BeforeEach
    void setUp() {

        modelMapper = new ModelMapper();
        accountService = new AccountServiceImpl(modelMapper);
        MockitoAnnotations.openMocks(this);

        account = new Account("1", "2", 1000L);
        accountDto = new AccountDto("1", "2", 1000L);
        userDto = new UserDto("1", "Aanshik", "9026758004", new Date());


        //account to dto
        given(modelMapper.map(Mockito.any(Account.class), Mockito.eq(AccountDto.class))).willReturn(accountDto);

        //dto to account
        given(modelMapper.map(Mockito.any(AccountDto.class), Mockito.eq(Account.class))).willReturn(account);

    }

    @Test
    void createAccount() {


        //Create Rest Template for Account-User call
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(UserDto.class))).thenReturn(new ResponseEntity<>(userDto, HttpStatus.OK));

        Mockito.when(accountRepo.getAccountById(Mockito.anyString())).thenReturn(account);

        Mockito.when(accountRepo.createAccount(Mockito.any(Account.class))).thenReturn(1);

        AccountDto createdAccount = accountService.createAccount(accountDto);

        assertEquals(createdAccount.getUserId(), accountDto.getUserId());
        assertEquals(createdAccount.getAccountId(), accountDto.getAccountId());
        assertEquals(createdAccount.getBalance(), accountDto.getBalance());

    }

    @Test
    void getAccountById() {

        //Create Rest Template for Account-User call
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(UserDto.class))).thenReturn(new ResponseEntity<>(userDto, HttpStatus.OK));

        Mockito.when(accountRepo.getAccountById(Mockito.anyString())).thenReturn(account);

        AccountDto createdAccount = accountService.getAccountById("1");

        assertEquals(createdAccount.getUserId(), accountDto.getUserId());
        assertEquals(createdAccount.getAccountId(), accountDto.getAccountId());
        assertEquals(createdAccount.getBalance(), accountDto.getBalance());

    }

    @Test
    void getAccountByUserId() {

        //Create Rest Template Account for User created
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(UserDto.class))).thenReturn(new ResponseEntity<>(userDto, HttpStatus.OK));

        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("1", "2", 1000L));
        accounts.add(new Account("1", "1", 1003L));

        Mockito.when(accountRepo.getAccountById(Mockito.anyString())).thenReturn(account);

        Mockito.when(accountRepo.getAccountByUserId(Mockito.anyString())).thenReturn(accounts);

        List<AccountDto> accountDtos = accountService.getAccountByUserId("1");

        assertEquals(accountDtos.size(), 2);

    }

    @Test
    void getAllAccounts() {

        //Create Rest Template Account for User created
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(UserDto.class))).thenReturn(new ResponseEntity<>(userDto, HttpStatus.OK));

        //Create list of mock response
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("1", "2", 1000L));
        accounts.add(new Account("1", "1", 1003L));


        Mockito.when(accountRepo.getAccountById(Mockito.anyString())).thenReturn(account);

        Mockito.when(accountRepo.getAccountByUserId(Mockito.anyString())).thenReturn(accounts);

        List<AccountDto> accountDtos = accountService.getAccountByUserId("1");

        assertEquals(accountDtos.size(), 2);


    }

    @Test
    void updateAccount() {

        AccountDto updateAccount = accountDto;

        //Create Rest Template Account for User created
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(UserDto.class))).thenReturn(new ResponseEntity<>(userDto, HttpStatus.OK));


        //for internal call in update user
        Mockito.when(accountRepo.getAccountById(Mockito.anyString())).thenReturn(account);

        //update
        Mockito.when(accountRepo.updateAccount(Mockito.anyString(), Mockito.any(Account.class))).thenReturn(1);

        AccountDto updatedAccount = accountService.updateAccount("1", updateAccount);

        assertEquals(updatedAccount.getUserId(), updateAccount.getUserId());
        assertEquals(updatedAccount.getAccountId(), updateAccount.getAccountId());
        assertEquals(updatedAccount.getBalance(), updateAccount.getBalance());
        assertEquals(updatedAccount.getUserDetails(), updateAccount.getUserDetails());
    }

    @Test
    void deleteAccountByAccountId() {
    }

    @Test
    void deleteAccountByUserId() {
    }

    @Test
    void depositBalance() {

        AccountDto updateAccount = new AccountDto("1", "2", 1010L, userDto);

        //Create Rest Template Account for User created
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(UserDto.class))).thenReturn(new ResponseEntity<>(userDto, HttpStatus.OK));

        //for internal call in update user
        Mockito.when(accountRepo.getAccountById(Mockito.anyString())).thenReturn(account);

        //for internal call in update user
        Mockito.when(accountRepo.updateAccount(Mockito.anyString(), Mockito.any(Account.class))).thenReturn(1);

        //update
        Mockito.when(accountRepo.updateAccount(Mockito.anyString(), Mockito.any(Account.class))).thenReturn(1);

        AccountDto updatedAccount = accountService.depositBalance("1", 10);

        long checkBalance = updateAccount.getBalance();

        assertEquals(updatedAccount.getUserId(), updateAccount.getUserId());
        assertEquals(updatedAccount.getAccountId(), updateAccount.getAccountId());
        assertEquals(updatedAccount.getBalance(), checkBalance);
        assertEquals(updatedAccount.getUserDetails(), updateAccount.getUserDetails());

    }


    @Test
    void withdrawBalance() {
        AccountDto updateAccount = new AccountDto("1", "2", 990L, userDto);

        //Create Rest Template Account for User created
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(UserDto.class))).thenReturn(new ResponseEntity<>(userDto, HttpStatus.OK));

        //for internal call in update user
        Mockito.when(accountRepo.getAccountById(Mockito.anyString())).thenReturn(account);

        //for internal call in update user
        Mockito.when(accountRepo.updateAccount(Mockito.anyString(), Mockito.any(Account.class))).thenReturn(1);

        //update
        Mockito.when(accountRepo.updateAccount(Mockito.anyString(), Mockito.any(Account.class))).thenReturn(1);

        AccountDto updatedAccount = accountService.withdrawBalance("1", 10);

        long checkBalance = updateAccount.getBalance();

        assertEquals(updatedAccount.getUserId(), updateAccount.getUserId());
        assertEquals(updatedAccount.getAccountId(), updateAccount.getAccountId());
        assertEquals(updatedAccount.getBalance(), checkBalance);
        assertEquals(updatedAccount.getUserDetails(), updateAccount.getUserDetails());
    }


}