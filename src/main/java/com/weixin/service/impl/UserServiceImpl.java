package com.weixin.service.impl;

import com.weixin.dao.UserMapper;
import com.weixin.pojo.User;
import com.weixin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2018/2/7.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public User getUserById(Integer id){
        return userMapper.getUserById(id);
    }

}
