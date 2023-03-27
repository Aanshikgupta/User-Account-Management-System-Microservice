package com.aanshik.UserService.Payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    private String userId;
    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be blank")
    @NotEmpty(message = "Username cannot be empty")
    private String userName;
    private String userMobile;
    private Date userDob;

}
