package com.aanshik.AccountService.Respositories;

import com.aanshik.AccountService.Enitity.Account;
import com.aanshik.AccountService.Payloads.UserDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AccountRepo {

    @Insert("INSERT INTO accounts(user_id,account_id,balance) values (#{userId},#{accountId},#{balance})")
    public Integer createAccount(Account account);

    @Update("Update accounts SET balance=#{account.balance} WHERE account_id=#{accountId}")
    public Integer updateAccount(String accountId, Account account);

    @Results({
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "balance", column = "balance"),
    })
    @Select("SELECT * FROM accounts")
    public List<Account> getAllAccounts();

    @Results({
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "balance", column = "balance"),
    })
    @Select("SELECT * FROM accounts WHERE account_id=#{accountId}")
    public Account getAccountById(String accountId);

    @Results({
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "balance", column = "balance"),
    })
    @Select("SELECT * FROM accounts WHERE user_id=#{userId}")
    public List<Account> getAccountByUserId(String userId);

    @Delete("DELETE FROM accounts WHERE account_id=#{accountId}")
    public Integer deleteAccountByAccountId(String accountId);

    @Delete("DELETE FROM accounts WHERE user_id=#{userId}")
    public Integer deleteAccountByUserId(String userId);

}
