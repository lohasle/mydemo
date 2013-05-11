package demo.spring.el;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class HelloSEL {

	/**
	 * 　　1）创建解析器：SpEL使用ExpressionParser接口表示解析器，提供SpelExpressionParser默认实现；
	 * 　　2）解析表达式：使用ExpressionParser的parseExpression来解析相应的表达式为Expression对象。
	 * 　　3）构造上下文：准备比如变量定义等等表达式需要的上下文数据。
	 * 4）求值：通过Expression接口的getValue方法根据上下文获得表达式值
	 */
	//@Test
	public void testHello() {
		ExpressionParser ep = new SpelExpressionParser();
		Expression ex = ep
				.parseExpression("('hello'+' springel').concat(#end)");
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("end", "!");
		Assert.assertEquals("hello springel!", ex.getValue(context));
		System.out.println(ex.getValue(context));
	}
	
	@Test
	public void testParserContext(){
		ExpressionParser eParser = new SpelExpressionParser();
		ParserContext pc = new ParserContext() {           //使用模板	
			@Override
			public boolean isTemplate() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public String getExpressionSuffix() {
				// TODO Auto-generated method stub
				return "}";
			}
			
			//前缀
			@Override
			public String getExpressionPrefix() {
				// TODO Auto-generated method stub
				return "#{";
			}
		};
		String str = "#{'hello '}#{'el'}";//表达式里的字符串 需要用单引号包括
		Expression expression = eParser.parseExpression(str, pc);
		System.out.println(expression.getValue());
		Assert.assertEquals("hello el", expression.getValue());
	}

}
