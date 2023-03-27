package com.aanshik.AccountService.Services.ServiceImpl;

import com.aanshik.AccountService.Enitity.Account;
import com.aanshik.AccountService.ExceptionHandling.LowBalanceException;
import com.aanshik.AccountService.ExceptionHandling.ResourceNotFoundException;
import com.aanshik.AccountService.Payloads.AccountDto;
import com.aanshik.AccountService.Payloads.UserDto;
import com.aanshik.AccountService.Respositories.AccountRepo;
import com.aanshik.AccountService.Services.AccountService;
import com.aanshik.AccountService.Utils.Constants;
import org.apache.tomcat.util.bcel.Const;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepo accountRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RestTemplate restTemplate;

    @Override
    @CacheEvict(value="account-dto",allEntries = true)
    public Integer createAccount(AccountDto accountDto) {

        Account accountToBeSaved = this.modelMapper.map(accountDto, Account.class);
        String accountId = UUID.randomUUID().toString();
        accountToBeSaved.setAccountId(accountId);

        //set user details to this account

        return accountRepo.createAccount(accountToBeSaved);
    }

    @Override
    @Cacheable("account-dto")
    public AccountDto getAccountById(String accountId) {
        Account account = accountRepo.getAccountById(accountId);
        if (account == null) {
            throw new ResourceNotFoundException("Account", accountId);
        }
        AccountDto accountDto = this.modelMapper.map(account, AccountDto.class);

        accountDto.setUserDetails(getUserByAccount(accountId));
        return accountDto;
    }

    @Cacheable("user-dto")
    public UserDto getUserByAccount(String accountId) {

        Account account = accountRepo.getAccountById(accountId);

        if (account == null) {
            throw new ResourceNotFoundException("Account", accountId);
        }

        AccountDto accountDto = this.modelMapper.map(account, AccountDto.class);

        UserDto userDto = restTemplate.getForEntity(Constants.USER_SERVICE_BASE_URL + "/" + accountDto.getUserId(), UserDto.class).getBody();

        return userDto;
    }

    @Override
    @Cacheable("account-dto-list")
    public List<AccountDto> getAccountByUserId(String userId) {


        List<Account> accounts = accountRepo.getAccountByUserId(userId);
        return getAccountDtos(accounts);
    }

    //TODO: Handle User not Found in User Service with Appropriate Message
    private void userPresentOrNot(String userId) {
        Object userDto = restTemplate.getForObject("http://USER-SERVICE/users/" + userId, Object.class);
    }

    @Override
    @Cacheable("account-dto-list")
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepo.getAllAccounts();

        return getAccountDtos(accounts);
    }

    private List<AccountDto> getAccountDtos(List<Account> accounts) {

        List<AccountDto> accountDtoList = accounts.stream().map((account -> {
            UserDto userDto = getUserByAccount(account.getAccountId());
            AccountDto accountDto = this.modelMapper.map(account, AccountDto.class);
            accountDto.setUserDetails(userDto);
            return accountDto;
        })).collect(Collectors.toList());

        return accountDtoList;
    }

    @Override
    @CacheEvict(value="account-dto",allEntries = true)
    public Integer updateAccount(String accountId, AccountDto accountDto) {
        Account account = accountRepo.getAccountById(accountId);
        if (account == null) {
            throw new ResourceNotFoundException("Account", accountId);
        }
        Account dto = this.modelMapper.map(accountDto, Account.class);
        account.setBalance(dto.getBalance());
        return accountRepo.updateAccount(accountId, account);
    }

    @Override
    @CacheEvict(value="account-dto",allEntries = true)
    public Integer deleteAccountByAccountId(String accountId) {
        return accountRepo.deleteAccountByAccountId(accountId);
    }

    @Override
    @CacheEvict(value="account-dto",allEntries = true)
    public Integer deleteAccountByUserId(String userId) {
        return accountRepo.deleteAccountByUserId(userId);
    }


    //TODO:Not Working Yet
    @Override
    public Integer depositBalance(String accountId, long balance) {
        AccountDto accountDto = getAccountById(accountId);
        long oldBalance = accountDto.getBalance();
        long newBalance = oldBalance + balance;

        accountDto.setBalance(newBalance);

        return updateAccount(accountId, accountDto);
    }


    //TODO:Not Working Yet
    @Override
    public Integer withdrawBalance(String accountId, long balance) {

        AccountDto accountDto = getAccountById(accountId);
        long oldBalance = accountDto.getBalance();
        long newBalance = oldBalance - balance;

        if (newBalance < 0) {
            throw new LowBalanceException();
        }

        accountDto.setBalance(newBalance);

        return updateAccount(accountId, accountDto);
    }


}
