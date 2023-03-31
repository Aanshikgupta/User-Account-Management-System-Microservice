package com.aanshik.AccountService.Enitity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;

    @BeforeEach
    public void createAccount() {
        account = new Account("1", "2", 100L);
    }

    @Test
    void getAccountId() {

        String accountId = "1";

        assertEquals(accountId, account.getAccountId());

    }

    @Test
    void getUserId() {
        String userId = "2";

        assertEquals(userId, account.getUserId());
    }

    @Test
    void getBalance() {
        long balance = 100L;

        assertEquals(balance, account.getBalance());
    }

    @Test
    void setAccountId() {
        String account_id = "10";
        String old_account_id = account.getAccountId();
        account.setAccountId(account_id);

        assertEquals(account_id, account.getAccountId());

        account.setAccountId(old_account_id);
    }

    @Test
    void setUserId() {
        String user_id = "110";
        String old_user_id = account.getUserId();
        account.setUserId(user_id);

        assertEquals(user_id, account.getUserId());

        account.setUserId(old_user_id);
    }

    @Test
    void setBalance() {
        long balance = 10L;
        long old_balance = account.getBalance();

        account.setBalance(balance);

        assertEquals(balance, account.getBalance());

        account.setBalance(old_balance);

    }
}