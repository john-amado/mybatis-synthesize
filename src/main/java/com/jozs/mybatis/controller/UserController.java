package com.jozs.mybatis.controller;

import com.jozs.mybatis.mapper.UserMapper;
import com.jozs.mybatis.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    @RequestMapping(value = "/saveUser", method = RequestMethod.GET)
    @ResponseBody
    public Object saveUser() {
        User user = new User();
        user.setName("aises");
        user.setGender(2);
        user.setHead_img("www.baidu.com");
        return userMapper.saveUser(user);
    }
    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    @ResponseBody
    public Object deleteUser() {
        return userMapper.deleteUser(3);
    }



    @RequestMapping(value = "/updateUser", method = RequestMethod.GET)
    @ResponseBody
    public Object updateUser() {
        User user = new User();
        user.setId(2);
        user.setName("amy");
        user.setGender(2);
        user.setHead_img("https://www.baidu.cn");
        return userMapper.updateUser(user);
    }



    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public Object getUser() {
        return userMapper.selectUser(1);
    }
}
