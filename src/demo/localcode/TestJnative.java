package demo.localcode;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;

/**
 * 测试jnative 调用本地dll
 * 需要jar JNative.jar
 * @author fule
 * 
 */
public class TestJnative {
	public static int testJNative(int a, int b) throws NativeException,
			IllegalAccessException {
		JNative n = null;
		try {
			// test.dll add 方法
			n = new JNative("D:\\Dveprogram\\MinGW\\test\\testc\\test.dll",
					"add");
			n.setRetVal(Type.INT);
			n.setParameter(0, a);
			n.setParameter(1, b);
			n.invoke();
			System.out.println("返回：" + n.getRetVal());
			return Integer.parseInt(n.getRetVal());
		} finally {
			if (n != null)
				n.dispose();
		}
	}

	/**
	 * 测试三水源
	 * 
	 * @throws NativeException
	 * @throws IllegalAccessException
	 */
	public static void testSM3() throws NativeException, IllegalAccessException {
		JNative n = null;
		try {
			n = new JNative(
					"G:\\项目\\中小河流\\进度\\预警模型\\新安江模型\\水文局6个选用模型\\水文局6个选用模型\\testSMS\\SMS_3.dll",
					"SMS_3");
			n.setRetVal(Type.INT);
			n.setParameter(0, "SMS_3");
			n.invoke();
			System.out.println("返回：" + n.getRetVal());
		} catch (NativeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (n != null)
				n.dispose();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			 testJNative(1, 4);
			//testSM3();
		} catch (NativeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
