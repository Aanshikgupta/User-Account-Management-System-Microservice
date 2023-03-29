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
    @Caching(evict = {@CacheEvict(value = "accountsList-dto", allEntries = true),
            @CacheEvict(value = "account-user-dto", allEntries = true),
            @CacheEvict(value = "account-user-list-dto", allEntries = true),
            @CacheEvict(value = "account-dto", allEntries = true),})
    public AccountDto createAccount(AccountDto accountDto) {

        Account accountToBeSaved = this.modelMapper.map(accountDto, Account.class);

        //generate random accountId
        String accountId = UUID.randomUUID().toString();
        accountToBeSaved.setAccountId(accountId);

        int aff = accountRepo.createAccount(accountToBeSaved);

        AccountDto accountDtoSaved = this.modelMapper.map(accountToBeSaved, AccountDto.class);

        //get user details
        UserDto userDto = getUserByAccountId(accountId);
        //set user details to this account
        accountDtoSaved.setUserDetails(userDto);

        return accountDtoSaved;
    }


    @Override
    @Cacheable(value = "account-dto", key = "#accountId")
    public AccountDto getAccountById(String accountId) {

        AccountDto accountDto = getAccountDtoById(accountId);
        accountDto.setUserDetails(getUserByAccountId(accountId));

        return accountDto;
    }


    @Cacheable(value = "account-user-dto")
    public UserDto getUserByAccountId(String accountId) {

        AccountDto accountDto = getAccountDtoById(accountId);

        try {
            UserDto userDto = restTemplate.getForEntity(Constants.USER_SERVICE_BASE_URL + "/" + accountDto.getUserId(), UserDto.class).getBody();
            return userDto;
        } catch (HttpClientErrorException exception) {
            throw new ResourceNotFoundException("User", accountDto.getUserId());
        }

    }


    public AccountDto getAccountDtoById(String accountId) {

        Account account = accountRepo.getAccountById(accountId);
        if (account == null) {
            throw new ResourceNotFoundException("Account", accountId);
        }

        AccountDto accountDto = this.modelMapper.map(account, AccountDto.class);
        return accountDto;

    }


    @Override
    @Cacheable(value = "account-user-list-dto")
    public List<AccountDto> getAccountByUserId(String userId) {

        //check if user present or not
        userPresentOrNot(userId);

        List<Account> accounts = accountRepo.getAccountByUserId(userId);
        return getAccountDtos(accounts);

    }


    private UserDto userPresentOrNot(String userId) {

        try {
            UserDto userDto = restTemplate.getForObject(Constants.USER_SERVICE_BASE_URL + "/" + userId, UserDto.class);
            return userDto;
        } catch (HttpClientErrorException exception) {
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
            UserDto userDto = getUserByAccountId(account.getAccountId());
            AccountDto accountDto = this.modelMapper.map(account, AccountDto.class);
            accountDto.setUserDetails(userDto);
            return accountDto;
        })).collect(Collectors.toList());

        return accountDtoList;

    }


    @Override
    @Caching(evict = {@CacheEvict(value = "accountsList-dto", allEntries = true),
            @CacheEvict(value = "account-user-dto", allEntries = true),
            @CacheEvict(value = "account-user-list-dto", allEntries = true),},
            put = {@CachePut(value = "account-dto", key = "#accountId")})
    public AccountDto updateAccount(String accountId, AccountDto accountDto) {

        Account account = accountPresentOrNot(accountId);


        Account dto = this.modelMapper.map(accountDto, Account.class);
        account.setBalance(dto.getBalance());
        int aff = accountRepo.updateAccount(accountId, account);

        return getAccountById(accountId);

    }


    @Override
    @Caching(evict = {@CacheEvict(value = "accountsList-dto", allEntries = true),
            @CacheEvict(value = "account-user-dto", allEntries = true),
            @CacheEvict(value = "account-user-list-dto", allEntries = true),
            @CacheEvict(value = "account-dto", allEntries = true),})
    public Boolean deleteAccountByAccountId(String accountId) {
        //extra check
        accountPresentOrNot(accountId);
        return accountRepo.deleteAccountByAccountId(accountId) >= 1;
    }


    public Account accountPresentOrNot(String accountId) {
        Account account = accountRepo.getAccountById(accountId);
        if (account == null) {
            throw new ResourceNotFoundException("Account", accountId);
        }
        return account;
    }


    @Override
    @Caching(evict = {@CacheEvict(value = "accountsList-dto", allEntries = true),
            @CacheEvict(value = "account-user-dto", allEntries = true),
            @CacheEvict(value = "account-user-list-dto", allEntries = true),
            @CacheEvict(value = "account-dto", allEntries = true),})
    public Boolean deleteAccountByUserId(String userId) {
        Integer delOrNot = accountRepo.deleteAccountByUserId(userId);
        if (delOrNot == 0) {
            throw new ResourceNotFoundException("User", userId);
        }
        return true;
    }



    @Override
    @Caching(evict = {@CacheEvict(value = "accountsList-dto", allEntries = true),
            @CacheEvict(value = "account-user-dto", allEntries = true),
            @CacheEvict(value = "account-user-list-dto", allEntries = true),},
            put = {@CachePut(value = "account-dto", key = "#accountId")})
    public AccountDto depositBalance(String accountId, long balance) {
        AccountDto accountDto = getAccountById(accountId);
        long oldBalance = accountDto.getBalance();
        long newBalance = oldBalance + balance;

        accountDto.setBalance(newBalance);

        return updateAccount(accountId, accountDto);
    }


    @Override
    @Caching(evict = {@CacheEvict(value = "accountsList-dto", allEntries = true),
            @CacheEvict(value = "account-user-dto", allEntries = true),
            @CacheEvict(value = "account-user-list-dto", allEntries = true),},
            put = {@CachePut(value = "account-dto", key = "#accountId")})
    public AccountDto withdrawBalance(String accountId, long balance) {

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
