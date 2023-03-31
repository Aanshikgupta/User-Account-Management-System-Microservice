package com.aanshik.UserService.Controllers;

import com.aanshik.UserService.Payloads.AccountDto;
import com.aanshik.UserService.Payloads.UserDto;
import com.aanshik.UserService.Services.UserServices;
import jakarta.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
@Setter
public class UserController {

    @Autowired
    UserServices userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    @PostMapping("/accounts/{userId}")
    public ResponseEntity<AccountDto> createAccount(@PathVariable String userId, @RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(userService.createAccount(userId, accountDto), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String userId, @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.updateUser(userId, userDto), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable String userId) {
        return new ResponseEntity<>(userService.deleteUser(userId), HttpStatus.OK);
    }

}
