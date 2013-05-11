package demo.spring.aop.test;

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
 * 
 */
public class TestAop {

	private ApplicationContext applicationContext;

	@Before
	public void before() {
		applicationContext = new ClassPathXmlApplicationContext(
				"classpath:config/spring/aop/runaop.xml");
	}

	// @Test
	public void Test1() {
		HelloWorldService hser = applicationContext.getBean("helloser",
				HelloWorldService.class);
		hser.sayHello();
		hser.sayHello2();
	}

	// @Test //被两个aop影响了 两个切面配置有重叠
	public void Test2() {
		HelloWorldService hser = applicationContext.getBean("helloser",
				HelloWorldService.class);
		// hser.sayHello();
		hser.sayParams("这里传入了参数");// 测试前置通知
		// 后置返回通知在切入点选择的方法正常返回时执行，通过<aop:aspect>标签下的<aop:after-returning>标签声明

	}

	// @Test //测试后置返回通知
	public void test3() {
		HelloWorldService hser = applicationContext.getBean("helloser",
				HelloWorldService.class);
		hser.sayHasReturn("乔丹");
	}

	//@Test //测试后置异常通知
	public void test4() {
		HelloWorldService hser = applicationContext.getBean("helloser",
				HelloWorldService.class);
		System.out.println(hser.sayTheWorng("胡惊涛"));
	}
	
	//@Test //测试后置最终通知
	public void test5() {
		HelloWorldService hser = applicationContext.getBean("helloser",
				HelloWorldService.class);
		System.out.println(hser.sayFinally());
	}
	
	@Test //测试环绕通知
	public void test6() {
		HelloWorldService hser = applicationContext.getBean("helloser",
				HelloWorldService.class);
		hser.sayAround("hello around");
	}
	

}
