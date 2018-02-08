package com.wexin.jpa.controller;/*
package main.java.com.wexin.jpa.controller;

import com.wexin.jpa.pojo.User;
import com.wexin.jpa.pojo.WeiXin;
import com.wexin.jpa.service.SrpingDataJpaServiceTest;
import com.wexin.jpa.pojo.User;
import com.wexin.jpa.pojo.WeiXin;
import com.wexin.jpa.service.SrpingDataJpaServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
public class TestController {

	*/
/**
	 * SpringBoot 配置文件里配置
	 *//*

	@Autowired
	private WeiXin weixin;
	
	@Autowired
	private SrpingDataJpaServiceTest srpingDataJpaServiceTest;
	
	@GetMapping(value = "/test")
	public String test() {
		return weixin.getAppid()+","+weixin.getKey();
	}
	
	@GetMapping(value = "/findUserAll")
	public List<User> findUserAll(){
		return srpingDataJpaServiceTest.findUserAll();
	}
	
	*/
/**
	 *  ： 验证参数时必须加上此注解,同时必须要在对应的实体字段上加上验证规则
	 * @param user   参数实体
	 * @param result  验证出错时，获取错误显示
	 * @return
	 *  PS:
	 *   @Valid   实体属性上的注解如：@Notull   BindingResult类     三个配合使用
	 *//*

	@GetMapping(value = "/addUser")
	public Object addUser(@Valid User user, BindingResult result) {
		if(result.hasErrors()) {  //说明没有通过验证
			System.out.println(result.getFieldError().getDefaultMessage());  //此方法打印在User实体属性上加注解的message
			return result.getFieldError().getDefaultMessage();
		}
		user.setDate(new Date());
		return srpingDataJpaServiceTest.addUser(user);
	}
	
	
}
*/
