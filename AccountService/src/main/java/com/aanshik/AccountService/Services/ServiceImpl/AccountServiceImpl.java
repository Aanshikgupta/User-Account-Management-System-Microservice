package com.aanshik.AccountService.Services.ServiceImpl;

import com.aanshik.AccountService.Enitity.Account;
import com.aanshik.AccountService.ExceptionHandling.LowBalanceException;
import com.aanshik.AccountService.ExceptionHandling.ResourceNotFoundException;
import com.aanshik.AccountService.Payloads.AccountDto;
import com.aanshik.AccountService.Payloads.UserDto;
import com.aanshik.AccountService.Respositories.AccountRepo;
import com.aanshik.AccountService.Services.AccountService;
import com.aanshik.AccountService.Utils.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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
    public Integer createAccount(AccountDto accountDto) {

        Account accountToBeSaved = this.modelMapper.map(accountDto, Account.class);
        String accountId = UUID.randomUUID().toString();
        accountToBeSaved.setAccountId(accountId);

        //set user details to this account

        return accountRepo.createAccount(accountToBeSaved);
    }

    @Override
    @Cacheable(value = "account-dto", key = "#accountId")
    public AccountDto getAccountById(String accountId) {
        Account account = accountRepo.getAccountById(accountId);
        if (account == null) {
            throw new ResourceNotFoundException("Account", accountId);
        }
        AccountDto accountDto = this.modelMapper.map(account, AccountDto.class);

        accountDto.setUserDetails(getUserByAccount(accountId));
        return accountDto;
    }

    @Cacheable(value = "account-user-dto")
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
    @Cacheable(value = "account-user-list-dto")
    public List<AccountDto> getAccountByUserId(String userId) {

        //check if user present or not
        userPresentOrNot(userId);

        List<Account> accounts = accountRepo.getAccountByUserId(userId);
        return getAccountDtos(accounts);
    }

    //TODO: Handle User not Found in User Service with Appropriate Message
    private void userPresentOrNot(String userId) {
        try {
            Object userDto = restTemplate.getForObject("http://USER-SERVICE/users/" + userId, Object.class);
        } catch (HttpClientErrorException exception) {
            System.out.println(exception);
            throw new ResourceNotFoundException("User", userId);
        }
    }

    @Override
    @Cacheable(value = "accountsList-dto")
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
    @Caching(evict = {@CacheEvict(value = "accountsList-dto", allEntries = true),
            @CacheEvict(value = "account-user-dto", allEntries = true),
            @CacheEvict(value = "account-user-list-dto", allEntries = true),})
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
    @Caching(evict = {@CacheEvict(value = "accountsList-dto", allEntries = true),
            @CacheEvict(value = "account-user-dto", allEntries = true),
            @CacheEvict(value = "account-user-list-dto", allEntries = true),})
    public Integer deleteAccountByAccountId(String accountId) {
        return accountRepo.deleteAccountByAccountId(accountId);
    }

    @Override
    public Integer deleteAccountByUserId(String userId) {
        Integer delOrNot = accountRepo.deleteAccountByUserId(userId);
        if (delOrNot == 0) {
            throw new ResourceNotFoundException("User", userId);
        }
        return delOrNot;
    }


    //TODO:Not Working Yet
    @Override
    @Caching(evict = {@CacheEvict(value = "accountsList-dto", allEntries = true),
            @CacheEvict(value = "account-user-dto", allEntries = true),
            @CacheEvict(value = "account-user-list-dto", allEntries = true),})
    public Integer depositBalance(String accountId, long balance) {
        AccountDto accountDto = getAccountById(accountId);
        long oldBalance = accountDto.getBalance();
        long newBalance = oldBalance + balance;

        accountDto.setBalance(newBalance);

        return updateAccount(accountId, accountDto);
    }


    @Override
    @Caching(evict = {@CacheEvict(value = "accountsList-dto", allEntries = true),
            @CacheEvict(value = "account-user-dto", allEntries = true),
            @CacheEvict(value = "account-user-list-dto", allEntries = true),})
    public Integer withdrawBalance(String accountId, long balance) {

        AccountDto accountDto = getAccountById(accountId);
        long oldBalance = accountDto.getBalance();
        long newBalance = oldBalance - balance;

        System.out.println(newBalance);
        if (newBalance < 0) {
            throw new LowBalanceException();
        }

        accountDto.setBalance(newBalance);

        return updateAccount(accountId, accountDto);
    }


}
