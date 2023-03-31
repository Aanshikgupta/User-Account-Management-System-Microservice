package com.aanshik.UserService.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto implements Serializable {
    private String accountId;
    private String userId;
    private Long balance;

    public AccountDto(String userId, Long balance) {
        this.userId = userId;
        this.balance = balance;
    }
}