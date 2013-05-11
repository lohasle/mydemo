package demo.myutils;

import java.util.Date;

/**
 * 得到程序运行的目录
 * @author fule
 *
 */
public class Demo1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.getProperties().list(System.out);//当前的系统属性
		System.out.println(System.getProperty("user.name"));
		System.out.println(System.getProperty("java.library.path"));
		
		String realPath = Demo1.class.getClassLoader()////运行目录 
			.getResource("").getFile();
		
	}
}
