package demo.spring.di.commons;

import demo.spring.di.svr.Command;
/**
 * LookUP方式
 * @author fule
 *
 */
public abstract class CommandManager2 {
	// 模拟业务处理的方法
	public Object process() {
		Command command = createCommand();
		return command.execute();
	}

	// 获取一个命令
	protected abstract Command createCommand();
}
