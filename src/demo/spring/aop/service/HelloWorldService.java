package demo.spring.aop.service;

import java.math.BigDecimal;
import java.util.Date;

public interface HelloWorldService {

	void sayHello();

	void sayHello2();

	void sayParams(String params);

	String sayHasReturn(String name);

	String sayTheWorng(String msg);

	String sayFinally();

	void sayAround(String param);
	
	void sayAround2(String param,Date param2,int param3 ,Object param4);

	int getCount();

	void setCount(int count);
}
