package demo.spring.di.commons;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 通过ApplicationContextAware获取bean
 * 
 * @author fule
 * 
 */
public class TestACA implements ApplicationContextAware {

	/**
	 * 以静态变量保存ApplicationContext,可在任意代码中取出ApplicaitonContext.
	 */
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		// TODO Auto-generated method stub
		System.out.println("注入"+this.getClass().getSimpleName());
		TestACA.context = context;
	}

	public static ApplicationContext getApplicationContext() {
		return context;
	}

	/**
	 * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(String name) {
		return (T) context.getBean(name);
	}

}
