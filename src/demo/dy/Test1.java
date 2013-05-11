package demo.dy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import demo.dy.svr.Mammal;
import demo.dy.svr.Primate;
import demo.dy.svr.impl.Monkey;

public class Test1 {
	// @Test
	public void test1() {
		Class<List> clazz = List.class;
		Class<? extends List> subclazz = ArrayList.class.asSubclass(clazz);
		System.out.println(subclazz.getCanonicalName());
	}

	@Test
	public void test2() {
		// 第一种创建动态代理的方法
		Object proxy = Proxy.newProxyInstance(Monkey.class.getClassLoader(),
				Monkey.class.getInterfaces(), new MyInvocationHandler(
						new Monkey()));
		Mammal mammal = (Mammal) proxy; 
		mammal.eat("香蕉"); 
		mammal.type(); 
		 
		Primate primate = (Primate) proxy; 
		primate.think(); 
		
		//查看代理哪些方法
		Class<?> c = proxy.getClass(); 
		Method[] methods = c.getDeclaredMethods(); 
		for (Method m : methods) { 
		  System.out.println(m.getName()); 
		} 
	}
}
