package com.tf2center.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContextProvider {
	public static final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
			"applicationContext.xml");
}