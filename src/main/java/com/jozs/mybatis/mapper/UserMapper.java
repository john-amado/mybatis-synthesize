package com.jozs.mybatis.mapper;

import com.jozs.mybatis.pojo.User;

public interface UserMapper {
    User selectUser(Integer id);
    int saveUser(User user);
    int updateUser(User user);
    int deleteUser(Integer id);
}
