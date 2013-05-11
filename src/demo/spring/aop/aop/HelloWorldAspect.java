package demo.spring.aop.aop;

/**
 * HelloWorldAspect类不是真正的切面实现，只是定义了通知实现的类，在此我们可以把它看作就是缺少了切入点的切面
 * 
 * @author fule
 * 
 */
public class HelloWorldAspect {
	public void beforeAdvice() {
		System.out.println("=========beforeadvice\n");
	}

	public void afterFinallyAdvice() {
		System.out.println("=========afterFinallyAdvice\n");
	}
}
