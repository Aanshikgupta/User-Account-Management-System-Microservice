package com.aanshik.AccountService.Services;

import com.aanshik.AccountService.Payloads.AccountDto;
import com.aanshik.AccountService.Payloads.UserDto;

import java.util.List;

public interface AccountService {

    //create
    public AccountDto createAccount(AccountDto accountDto);

    //get Account by Id
    public AccountDto getAccountById(String accountId);

    //get Account by UserId
    public List<AccountDto> getAccountByUserId(String userId);

    //get all Account
    public List<AccountDto> getAllAccounts();

    //update Account
    public AccountDto updateAccount(String accountId, AccountDto accountDto);

    //delete Account
    public Boolean deleteAccountByAccountId(String accountId);

    //delete account by user id
    public Boolean deleteAccountByUserId(String userId);

    //deposit balance
    public AccountDto depositBalance(String accountId, long balance);

    //withdraw balance
    public AccountDto withdrawBalance(String accountId, long balance);
}
