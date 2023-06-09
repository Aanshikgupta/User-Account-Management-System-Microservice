package com.aanshik.UserService.payloads;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    private String userId;
    @NotNull(message = "Username cannot be null")
    @NotEmpty(message = "Username cannot be empty")
    private String userName;

    @Size(min = 10, max = 10, message = "Invalid Phone Number")
    private String userMobile;
    private Date userDob;

    public UserDto(String userName, String userMobile, Date userDob) {
        this.userName = userName;
        this.userMobile = userMobile;
        this.userDob = userDob;
    }
}
