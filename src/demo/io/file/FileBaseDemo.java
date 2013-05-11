package demo.io.file;

import java.io.File;
import java.io.IOException;

/**
 * 文件的基本操作
 * 
 * @author fule
 * 
 */
public class FileBaseDemo {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File file = new File(".");
		// 输出文件名 .
		System.out.println("file.getName()" + file.getName());
		// 父文件夹 null
		System.out.println("file.getparent()" + file.getParent());
		// 绝对路径
		System.out.println("file.getAbsolutePath()" + file.getAbsolutePath());
		// 上一级目录
		System.out.println("上一级目录" + file.getAbsoluteFile().getParent());

		// 在当前路径下创建一个临时文件
		// 在指定目录中创建一个新的空文件，使用给定的前缀和后缀字符串生成其名称。
		File tmpFile = File.createTempFile("aaa", ".txt", file);
		// JVm 退出时删除
		tmpFile.deleteOnExit();

		// 以系统当前时间作为新文件名来创建新文件
		File newFile = new File(System.currentTimeMillis() + "");
		System.out.println("newFile对象是否存在：" + newFile.exists());
		// 以指定newFile对象来创建一个文件
		newFile.createNewFile();
		// 以newFile对象来创建一个目录，因为newFile已经存在，
		// 所以下面方法返回false，即无法创建该目录
		newFile.mkdir();
		// 使用list()方法来列出当前路径下的所有文件和路径
		String[] fileList = file.list();
		System.out.println("====当前路径下所有文件和路径如下====");
		for (String fileName : fileList) {
			System.out.println(fileName);
		}
		// listRoots()静态方法列出所有的磁盘根路径。
		File[] roots = File.listRoots();
		System.out.println("====系统所有根路径如下====");
		for (File root : roots) {
			System.out.println(root);
		}
	}

}
