package demo.spring.aop.aop;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.tribes.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import demo.spring.aop.service.HelloWorldService;

/**
 * @AspectJ 注解风格
 * 
 * @author fule
 * 
 */
@Aspect
public class HelloWorldAspectAnnotate {

	/**
	 * 必须为final String类型的,注解里要使用的变量只能是静态常量类型的
	 * params必须和前置通知方法参数名一样
	 */
	public static final String EDP = "execution(* demo.spring.aop.service..*.sayParams(..)) and args(params)";

	/**
	 * 定义通知 params参数名和切面影响的方法名的参数名必须一致
	 * 
	 * @param params
	 */
	@Before(EDP)
	public void beforeAdvice(String params) {
		System.out.println(params + "		logBefore:现在时间是:" + new Date());
	}

	/**
	 * 后置返回
	 * @param name
	 */
	@AfterReturning(pointcut = "execution(* demo.spring.aop.service..*.sayHasReturn(..))", argNames = "name", returning = "name")
	public void afterReturningAdvice(String name) {
		System.out.println("hello,nice to meet you !what's your name?\n" + name
				+ "!\n");
	}

	/**
	 * 测试后置异常通知
	 * 
	 * @param e
	 */
	@AfterThrowing(pointcut="execution(* demo.spring.aop.service..*.sayTheWorng(..))",argNames="exception",throwing="exception")
	public void afterThrowingAdvice(Exception e) {
		System.out.println("===========after throwing advice exception:" + e);
	}

	/**
	 * 测试后置最终通知
	 * 
	 * @param e
	 */
	@After("execution(* demo.spring.aop.service..*.sayFinally(..))")
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
	 *             })动态的加入了参数值 使用 replace替换了原来的参数值
	 * 
	 */
	@Around(value="execution(* demo.spring.aop.service..*.sayAround(..))",argNames="param")
	public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("===========around before advice");
		HelloWorldService hsr = (HelloWorldService)pjp.getThis();  //得到当前代理的类
		System.out.println(Arrays.toString(pjp.getArgs()));//获得当前切入点方法参数值
		System.out.println(hsr.getCount());
		hsr.setCount(hsr.getCount()+1);
		System.out.println(hsr.getCount());
		Object retVal = pjp.proceed(new Object[] { "replace" });
		
		System.out.println("===========around after advice");
		return retVal;
	}
	
	/**
	 * 当切面方法多个参数  arg-params 用,隔开
	 * 这里 参数名 没有限制
	 * 返回时使用返回map
	 * @param pjp
	 * @return
	 * @throws Throwable
	 * 可以为void  也可以有返回值
	 */
	@Around(value="execution(* demo.spring.aop.service..*.sayAround2(..))",argNames="param,ppp,sss,aaa")
	public Map<String, Object> aroundAdvice2(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("===========around before advice");
		HelloWorldService hsr = (HelloWorldService)pjp.getThis();  //得到当前代理的类
		System.out.println(Arrays.toString(pjp.getArgs()));//获得当前切入点方法参数值
		System.out.println(hsr.getCount());
		hsr.setCount(hsr.getCount()+1);
		System.out.println(hsr.getCount());
		//Object retVal = pjp.proceed(new Object[] { "replace" });
		//System.out.println("===========around after advice");
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@222"+pjp.proceed());//目标类连接点对象
		Map<String, Object> retVal = new HashMap<String, Object>();
		/*retVal.put("param", "222222");
		retVal.put("param2", new Date());
		retVal.put("param3", 54);
		retVal.put("param5", new BigDecimal(32323));*/
		return retVal;
	}
}
