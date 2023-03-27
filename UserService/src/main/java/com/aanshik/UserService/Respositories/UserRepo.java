package com.aanshik.UserService.Respositories;

import com.aanshik.UserService.Enitity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
public interface UserRepo {


    @Insert("INSERT INTO users(user_id,user_name,user_mobile,user_dob) values (#{userId},#{userName},#{userMobile},#{userDob})")
    public Integer createUser(User user);

    @Update("Update users SET user_id=#{userId},user_name=#{user.userName},user_mobile=#{user.userMobile},user_dob=#{user.userDob} WHERE user_id=#{userId}")
    public Integer updateUser(String userId, User user);

    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "userName", column = "user_name"),
            @Result(property = "userMobile", column = "user_mobile"),
            @Result(property = "userDob", column = "user_dob")
    })
    @Select("SELECT * FROM users")
    public List<User> getAllUsers();


    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "userName", column = "user_name"),
            @Result(property = "userMobile", column = "user_mobile"),
            @Result(property = "userDob", column = "user_dob")
    })
    @Select("SELECT * FROM users WHERE user_id=#{userId}")
    public User getUserById(String userId);

    @Delete("DELETE FROM users WHERE user_id=#{userId}")
    public Integer deleteUser(String userId);

}
