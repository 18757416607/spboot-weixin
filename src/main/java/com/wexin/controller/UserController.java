package com.wexin.controller;

import com.wexin.pojo.User;
import com.wexin.service.ParkingService;
import com.wexin.service.UserService;
import com.wexin.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/7.
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ParkingService parkingService;

    @GetMapping(value = "/getUser/{id}")
    public User getUserById(@PathVariable(value = "id") Integer id){
        return userService.getUserById(id);
    }

    @GetMapping(value = "/t")
    public void t(String t) throws  Exception{
       /* String str = SecurityUtils.encrypt("{\"a\":\"1\",\"b\":\"2\"}");
        System.out.println(str);
        System.out.println("---------------------------------");
        System.out.println(SecurityUtils.decrypt(str));*/
        System.out.println(SecurityUtils.decrypt(""));
    }

    public static void main(String[] args) throws  Exception{
        System.out.println(SecurityUtils.decrypt("rNNals2i29a0g6zEDn8IIA2f1aA8yqNJbvrmygjLdT/CxWGBHTLyGwNNRIULohLj/7n9Hdkvlsy2Bvh7D+UXgA9zDlKlI2ysI+pASMtdNRLQfT/wVKAeN4/ih2m8NXj2QquvoJIq7L8AQFiWGrMq5iqWxMc60f9xNsUvkzwIQXQ="));
    }


}
