package demo.spring.di;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;

import com.sun.accessibility.internal.resources.accessibility;

import demo.spring.di.commons.CommandManager;
import demo.spring.di.commons.CommandManager2;
import demo.spring.di.commons.TestACA;
import demo.spring.di.svr.DoSometing;
import demo.spring.di.svr.ac.DiaoSiAc;
import demo.spring.di.vo.Diaosi;
import demo.spring.di.vo.Diaosi2;

public class TestDI {
	// 使用BeanFactory
	private static BeanFactory beanFactory;

	// 使用ClassPathXmlApplicationContext
	private static final ClassPathXmlApplicationContext appContext;

	static {
		appContext = new ClassPathXmlApplicationContext(
				new String[] { "classpath:config/spring/appconfig.xml" });
	}

	// @BeforeClass
	// public static void init() {
	// /*beanFactory = new XmlBeanFactory(new FileSystemResource(
	// "src/config/spring/simpleSpring.xml"));*/
	// beanFactory = new XmlBeanFactory(new FileSystemResource(
	// "src/config/spring/appconfig.xml"));//使用beanfactory
	// 不能使用classpath：qianzhui
	// }

	//@Test
	public void testdi() {
		// 一点要注册销毁回调，否则我们定义的销毁方法不执行
		appContext.registerShutdownHook();
		// Diaosi d = appContext.getBean("diaosi",Diaosi.class);
		// Diaosi d2 = (Diaosi) appContext.getBean("diaosi2");
		// depends-on 测试bean 的销毁和初始化的顺序
		Diaosi d3 = (Diaosi) appContext.getBean("diaosi3");
		//Diaosi2 ds = appContext.getBean("ds", Diaosi2.class);
		// Assert.assertEquals("张三",d3.getName());
		// DiaoSiAc dac = (DiaoSiAc) beanFactory.getBean("diaosiac");
		// dac.getDs().ws(d);
		// DiaoSiAc dac = (DiaoSiAc)appContext.getBean("diaosiac");
		// dac.getDs().ws(d);
		// System.out.println(dac.getSss());
		//System.out.println(System.getProperty("user.dir"));
	}

	/**
	 * ////需要cglib-node.jar来实现动态代理 当一个类为单列是 单例中依赖到了其他的类 而又想要此类是个原型 即可使用动态代理这个方法
	 */
	// @Test
	public void testLookup() {
		System.out.println("=======singleton sayHello======");
		DoSometing helloApi1 = appContext.getBean("doSi1", DoSometing.class);
		helloApi1.say();
		helloApi1 = appContext.getBean("doSi1", DoSometing.class);
		helloApi1.say();
		System.out.println("=======prototype sayHello======");
		DoSometing helloApi2 = appContext.getBean("doSi2", DoSometing.class);
		helloApi2.say();
		helloApi2 = appContext.getBean("doSi2", DoSometing.class);
		helloApi2.say();
	}

	// 通过ApplicationContextAware获取bean
	// @Test
	public void testACA() {
		Diaosi d = TestACA.getBean("diaosi");
		//System.out.println(System.getProperties().getProperty("java.io.tmpdir"));
		System.out.println(d.getInfo());
	}

	// 使用ApplicationContextAware解决 方法中对象的动态代理 容器管理的bean 的生命周期默认为singleton
	// @Test
	public void testACA2() {
		// CommandManager com =
		// appContext.getBean("commandManager",CommandManager.class);
		CommandManager com = TestACA.getBean("commandManager");
		System.out.println("①" + com.process());
		System.out.println("②" + com.process());
	}

	// 使用lookup 解决上一个问题 动态生成子类的解决
	// @Test
	public void testLookUp2() {
		CommandManager2 com = appContext.getBean("commandManager2",
				CommandManager2.class);
		System.out.println("①" + com.process());
		System.out.println("②" + com.process());
	}

	/**
	 * 自定义作用域测试 同一个线程
	 */
	// @Test
	public void testDiyScope() {
		Diaosi ds1 = appContext.getBean("testDiyScope", Diaosi.class);
		// System.out.println(ds1);
		Diaosi ds2 = appContext.getBean("testDiyScope", Diaosi.class);
		Assert.assertEquals(ds1, ds2);
	}

	/**
	 * 自定义作用域测试 同一个线程
	 * 
	 * @throws InterruptedException
	 */
	//@Test
	public void testDiyScopeMutilThread() throws InterruptedException {
		final Diaosi[] ds = new Diaosi[2];
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// ds[0] = appContext.getBean("testDiyScope", Diaosi.class);

				ds[0] = appContext.getBean("diaosi", Diaosi.class);
				System.out.println(ds[0]);
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// ds[1] = appContext.getBean("testDiyScope", Diaosi.class);
				ds[1] = appContext.getBean("diaosi", Diaosi.class);
				System.out.println(ds[1]);
			}
		});
		t1.start();
		t1.sleep(1000);
		t2.start();
		t2.sleep(1000);
		Assert.assertEquals(ds[0], ds[1]);
	}

}
