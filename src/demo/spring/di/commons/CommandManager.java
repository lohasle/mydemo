package demo.spring.di.commons;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import demo.spring.di.svr.Command;

public class CommandManager implements ApplicationContextAware {
	// 用于保存ApplicationContext的引用，set方式注入
	private ApplicationContext applicationContext;
	private  int count = 1;

	// 模拟业务处理的方法
	public Object process() {
		System.out.println("本类的地址为" + this);
		Command command = createCommand();
		return "第"+ (count++)+"次执行Command的地址是" + command.execute();
	}

	// 获取一个命令
	private Command createCommand() {
		return (Command) this.applicationContext.getBean("asyncCommand"); //
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		System.out.println(applicationContext);
		this.applicationContext = applicationContext;
	}
}
