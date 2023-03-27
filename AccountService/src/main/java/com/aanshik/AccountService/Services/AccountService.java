package com.aanshik.AccountService.Services;

import com.aanshik.AccountService.Payloads.AccountDto;

import java.util.List;

public interface AccountService {

    //create
    public Integer createAccount(AccountDto accountDto);

    //get Account by Id
    public AccountDto getAccountById(String accountId);

    //get Account by UserId
    public List<AccountDto> getAccountByUserId(String userId);

    //get all Account
    public List<AccountDto> getAllAccounts();

    //update Account
    public Integer updateAccount(String accountId, AccountDto accountDto);

    //delete Account
    public Integer deleteAccountByAccountId(String accountId);

    //delete account by user id
    public Integer deleteAccountByUserId(String userId);

    //deposit balance
    public Integer depositBalance(String accountId, long balance);

    //withdraw balance
    public Integer withdrawBalance(String accountId, long balance);
}
