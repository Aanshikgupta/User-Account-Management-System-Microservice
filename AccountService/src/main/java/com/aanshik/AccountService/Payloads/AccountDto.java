package com.aanshik.AccountService.Payloads;

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
    private UserDto userDetails;
}