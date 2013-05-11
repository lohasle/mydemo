package demo.spring.aop.test;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.spring.aop.service.HelloWorldService;

/**
 * 依赖的jar org.springframework.aop-3.0.5.RELEASE.jar
 * com.springsource.org.aspectj.weaver-1.6.8.RELEASE.jar
 * com.springsource.org.aopalliance-1.0.0.jar
 * com.springsource.net.sf.cglib-2.2.0.jar
 * 
 * @author fule
 * @AspectJ风格的命名切入点使用org.aspectj.lang.annotation包下的@Pointcut+方法（方法必须是返回void类型）实现
 */

public class TestAopAnnotate {

	private ApplicationContext applicationContext;

	@Before
	public void before() {
		applicationContext = new ClassPathXmlApplicationContext(
				"classpath:config/spring/aop/runaop2annotate.xml");
	}

	// @Test
	public void Test1() {
		HelloWorldService hser = applicationContext.getBean("helloser",
				HelloWorldService.class);
		hser.sayParams("hello,aspetctj");
	}

	 //@Test
	// 测试后置返回通知
	public void test2() {
		HelloWorldService hser = applicationContext.getBean("helloser",
				HelloWorldService.class);
		hser.sayHasReturn("乔丹");
	}

	// @Test
	// 测试后置异常通知
	public void test3() {
		HelloWorldService hser = applicationContext.getBean("helloser",
				HelloWorldService.class);
		System.out.println(hser.sayTheWorng("胡惊涛"));
	}

	// @Test
	// 测试后置最终通知
	public void test4() {
		HelloWorldService hser = applicationContext.getBean("helloser",
				HelloWorldService.class);
		System.out.println(hser.sayFinally());
	}

	//@Test
	// 测试环绕通知
	public void test5() {
		HelloWorldService hser = applicationContext.getBean("helloser",
				HelloWorldService.class);
		hser.setCount(99999);
		hser.sayAround("hello around");
	}
	
	@Test
	// 测试环绕通知  多参数
	public void test6() {
		HelloWorldService hser = applicationContext.getBean("helloser",
				HelloWorldService.class);
		hser.setCount(99999);
		System.out.println(hser);
		hser.sayAround2("11111111", new Date(), 45, hser);   
		//hser.sayAround2("11111111", new Date(), 45, new BigDecimal(12121));
	}
}
