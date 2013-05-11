package demo.localcode;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * 测试jna
 * 跨平台的难点在于 各个平台 数据类型的适配 
 * @author fule
 * 
 */
public class TestJna {

	/**
	 * 如果dll是以stdcall方式输出函数，那么就继承StdCallLibrary。否则就继承默认的Library接口。
	 * 
	 * 接口内部需要一个公共静态常量：instance。
	 * 
	 * 
	 * 通过这个常量，就可以获得这个接口的实例，从而使用接口的方法。也就是调用外部dll的函数！
	 * 
	 * @author fule
	 * 
	 */
	private interface TestAdd extends Library {
		TestAdd INSTANCE = (TestAdd) Native.loadLibrary(
				"D:\\Dveprogram\\MinGW\\test\\testc\\test", TestAdd.class);// NA通过这个Class类型，根据指定的dll/.so文件，动态创建接口的实例NA通过这个Class类型，根据指定的dll/.so文件，动态创建接口的实例

		// dll 对外发布的方法
		int add(int a, int b);
	}

	// 静态方法调用
	private static void test(int a, int b) {
		int c = TestAdd.INSTANCE.add(a, b);
		System.out.println(c);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test(2, 3);
	}

}
