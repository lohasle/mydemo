package demo.spring.di.svr.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

/**
 * 自定义作用域
 * 
 * @author fule
 * 
 */
public class ThreadScope implements Scope {

	private final ThreadLocal<Map<String, Object>> THREAD_SCOPE = new ThreadLocal<Map<String, Object>>() {
		protected Map<String, Object> initialValue() {
			// 用于存放线程相关Bean
			return new HashMap<String, Object>();
		}
	};


	/**
	 * 用于从作用域中获取Bean，其中参数objectFactory是当在当前作用域没找到合适Bean时使用它创建一个新的Bean
	 * //如果当前线程已经绑定了相应Bean，直接返回 //使用objectFactory创建Bean并绑定到当前线程上
	 * @param name
	 * @param objectFactory
	 * @return
	 */
	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		// TODO Auto-generated method stub
		if (!THREAD_SCOPE.get().containsKey(name)) {
			THREAD_SCOPE.get().put(name, objectFactory);
		}
		return THREAD_SCOPE.get().get(name);
	}

	@Override
	public Object remove(String name) {
		// TODO Auto-generated method stub
		return THREAD_SCOPE.get().remove(name);
	}

	/**
	 * 用于注册销毁回调，如果想要销毁相应的对象则由Spring容器注册相应的销毁回调，而由自定义作用域选择是不是要销毁相应的对象
	 * 
	 * @param name
	 * @param callback
	 */
	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		// TODO Auto-generated method stub

	}

	/**
	 * 用于解析相应的上下文数据，比如request作用域将返回request中的属性
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public Object resolveContextualObject(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getConversationId() {
		// TODO Auto-generated method stub
		return null;
	}

}
