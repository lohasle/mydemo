package demo.spring.aop.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import demo.spring.aop.service.HelloWorldService;

public class HelloWorldServiceImpl implements HelloWorldService {

	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public void sayHello() {
		// TODO Auto-generated method stub
		System.out.println("=========hello Aop!");
	}

	public void sayHello2() {
		System.out.println("=========hello aop2222222222222222222!");
	}

	@Override
	public void sayParams(String params) {
		// TODO Auto-generated method stub
		System.out.println("===================say" + params + "\n");
	}

	@Override
	public String sayHasReturn(String name) {
		// TODO Auto-generated method stub
		return "hello,my name is" + name + ",nice to meet you too";
	}

	@Override
	public String sayTheWorng(String msg) {
		// TODO Auto-generated method stub
		if ("胡惊涛".equals(msg)) {
			throw new NullPointerException("敏感词汇");
		}
		;
		return msg;
	}

	@Override
	public String sayFinally() {
		// TODO Auto-generated method stub
		throw new NullPointerException("敏感词汇");
	}

	@Override
	public void sayAround(String param) {
		System.out.println("============around param:" + param);
	}

	@Override
	public void sayAround2(String param, Date param2, int param3,
			Object param4) {
		// TODO Auto-generated method stub
		System.out.println(param+"  "+param2+"  "+param3+"  "+param4);
	}
}
