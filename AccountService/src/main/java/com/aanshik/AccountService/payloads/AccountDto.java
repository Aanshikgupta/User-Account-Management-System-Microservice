package com.aanshik.AccountService.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto implements Serializable {
    private String accountId;
    private String userId;
    @Range(min = 0, max = Long.MAX_VALUE, message = "Invalid Balance Amount!")
    private long balance;
    private UserDto userDetails;

    public AccountDto(String accountId, String userId, long balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }
}