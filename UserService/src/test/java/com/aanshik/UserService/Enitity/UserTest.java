package com.aanshik.UserService.Enitity;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    public void createAccount() {
        user = new User("1", "Aanshik", "9026758004", new Date("29/01/2001"));
    }

    @Test
    void getUserId() {
        String userId = "1";
        assertEquals(userId, user.getUserId());
    }

    @Test
    void getUserName() {
        String userName = "Aanshik";
        assertEquals(userName, user.getUserName());
    }

    @Test
    void getUserMobile() {
        String userMobile = "9026758004";
        assertEquals(userMobile, user.getUserMobile());
    }

    @Test
    void getUserDob() {
        Date date = new Date("29/01/2001");
        assertEquals(date, user.getUserDob());
    }

    @Test
    void setUserId() {
        String oldUserId = user.getUserId();
        String userId = "10";
        user.setUserId(userId);

        assertEquals(userId, user.getUserId());

        user.setUserId(oldUserId);

    }

    @Test
    void setUserName() {
        String oldUserName = user.getUserName();
        String userName = "Aanshik Gupta";
        user.setUserName(userName);

        assertEquals(userName, user.getUserName());

        user.setUserName(oldUserName);
    }

    @Test
    void setUserMobile() {
        String oldMobile = user.getUserMobile();
        String mobile = "8127136701";
        user.setUserMobile(mobile);

        assertEquals(mobile, user.getUserMobile());

        user.setUserMobile(mobile);
    }

    @Test
    void setUserDob() {
        Date oldDob = user.getUserDob();
        Date dob = new Date("01/01/2001");
        user.setUserDob(dob);

        assertEquals(dob, user.getUserDob());

        user.setUserDob(oldDob);
    }
}