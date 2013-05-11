package demo.dy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
/**
 * 回调类
 * @author fule
 *
 */
public class MyInvocationHandler implements InvocationHandler {
	private Object obj;

	public MyInvocationHandler(Object obj) {
		this.obj = obj;
	}

	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		System.out.println("Invoke method Before!");
		Object returnObject = method.invoke(obj, args);
		System.out.println("Invoke method After!");
		return returnObject;
	}

}
