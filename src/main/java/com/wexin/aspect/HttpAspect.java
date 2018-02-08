package com.wexin.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 记录每次的http请求
 *   每次请求http之前 记录
 * @author Administrator
 *
 */
@Aspect
@Component
public class HttpAspect {

	private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);
	
	/**
	 * @Pointcut 可以减少重复代码,直接引用@Pointcut这个注解的方法名
	 */
	//@Pointcut("execution(public * com.wexin.controller.TestController.findUserAll(..))")
	@Pointcut("execution(public * com.wexin.controller.UserController.*(..))")
	public void log() {
	}
	//@Pointcut(value = "execution(public * com.wexin.controller.TestController.*(..))")
	@Pointcut(value = "execution(public * com.wexin.controller.UserController.*(..))")
	public void logAll() {
		
	}
	
	@Before("log()")
  	public void logBefore() {
		logger.info(">>>>>>>>>>>>>>");
	}
	
	
	@After("logAll()")
  	public void logAfter() {
		logger.info("<<<<<<<<<<<<<<");
	}
	
}
