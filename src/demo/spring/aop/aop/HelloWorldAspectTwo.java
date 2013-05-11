package demo.spring.aop.aop;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 有参数的aspect类
 * 
 * @author fule
 * 
 */
public class HelloWorldAspectTwo {
	/**
	 * params参数名和切面影响的方法名的参数名必须一致
	 * 
	 * @param params
	 */
	public void beforeAdvice(String params) {
		System.out.println("=========beforeadvice  带参数的   " + params);
	}

	/**
	 * 测试后置返回通知 name 为切入点 执行方法的返回值
	 * 
	 * @param name
	 */
	public void afterReturningAdvice(String name) {
		System.out.println("hello,nice to meet you !what's your name?\n" + name
				+ "!\n");
	}

	/**
	 * 测试后置异常通知
	 * 
	 * @param e
	 */
	public void afterThrowingAdvice(Exception e) {
		System.out.println("===========after throwing advice exception:" + e);
	}

	/**
	 * 测试后置最终通知
	 * 
	 * @param e
	 */
	public void afterFinallyAdvice() {
		System.out.println("===================afterFinallyAdvice");
	}

	/**
	 * 环绕通知
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 *             通过 ProceedingJoinPoint pjp.proceed(new Object[] { "replace"
	 *             })动态的加入了参数值  使用 replace替换了原来的参数值
	 * 
	 */
	public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("===========around before advice");
		Object retVal = pjp.proceed(new Object[] { "replace" });
		System.out.println("===========around after advice");
		return retVal;
	}
}
