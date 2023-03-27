package com.aanshik.AccountService.Controllers;

import com.aanshik.AccountService.Payloads.AccountDto;
import com.aanshik.AccountService.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/accounts")
@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping
    public ResponseEntity<Integer> createAccount(@RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable String accountId) {
        return new ResponseEntity<>(accountService.getAccountById(accountId), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<AccountDto>> getAllAccounts(@PathVariable String userId) {
        return new ResponseEntity<>(accountService.getAccountByUserId(userId), HttpStatus.OK);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Integer> updateAccount(@PathVariable String accountId, @RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountService.updateAccount(accountId, accountDto), HttpStatus.OK);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Integer> deleteAccount(@PathVariable String accountId) {
        return new ResponseEntity<>(accountService.deleteAccountByAccountId(accountId), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Integer> deleteAccountByUserId(@PathVariable String userId) {
        return new ResponseEntity<>(accountService.deleteAccountByUserId(userId), HttpStatus.OK);
    }

    @PutMapping("/deposit/{accountId}")
    public ResponseEntity<Integer> addMoney(@PathVariable("accountId") String accountId, @RequestBody Long amount) {
        return new ResponseEntity<>(accountService.depositBalance(accountId, amount), HttpStatus.OK);
    }

    @PutMapping("/withdraw/{accountId}")
    public ResponseEntity<Integer> withdrawMoney(@PathVariable("accountId") String accountId, @RequestBody Long amount) {
        return new ResponseEntity<>(accountService.withdrawBalance(accountId, amount), HttpStatus.OK);
    }

}
