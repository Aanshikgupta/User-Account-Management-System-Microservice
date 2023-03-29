package com.aanshik.AccountService.Enitity;

import com.aanshik.AccountService.Payloads.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private String accountId;
    private String userId;
    private Long balance;

}
