package com.weixin.jpa.service.impl;/*
package com.weixin.jpa.service.impl;

import com.weixin.jpa.dao.SpringDataJpaTestRepository;
import com.weixin.jpa.pojo.User;
import com.weixin.jpa.service.SrpingDataJpaServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpringDataJpaServiceTestImpl implements SrpingDataJpaServiceTest {

	*/
/**
	 * 此dao继承了SpringDataJpa,直接调用SpringDataJpa的内置方法
	 *//*

	@Autowired
	private SpringDataJpaTestRepository springDataJpaTestRepository;
	
	public List<User> findUserAll() {
		//查询User表中的所有数据
		return springDataJpaTestRepository.findAll();
	}
	
	@Transactional  //此注解为开启事务
	public User addUser(User user) {
		return springDataJpaTestRepository.save(user);
	}

}
*/
