package demo.io.file;

import java.io.File;
import java.io.FilenameFilter;
/**
 * 文件过滤器
 * command 设计模式
 * @author fule
 *
 */
public class FileFilterDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("E:/MyLife/专属 乐/书籍/文学、小说");
		String[] str = file.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub

				return name.endsWith(".txt") || new File(name).isDirectory();
			}
		});
		for (String string : str) {
			System.out.println(string);
		}
	}

}
