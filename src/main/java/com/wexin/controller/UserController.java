package com.wexin.controller;

import com.wexin.pojo.User;
import com.wexin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/2/7.
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/getUser/{id}")
    public User getUserById(@PathVariable(value = "id") Integer id){
        return userService.getUserById(id);
    }



}
