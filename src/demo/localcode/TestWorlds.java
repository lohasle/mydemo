package demo.localcode;

import java.io.File;

class TestTestJcob {
	/**
	 * 创建一个新的word 文件
	 */
	public static void createANewFileTest() {
		TestJcob wordBean = new TestJcob();
		// word.openWord(true);// 打开 word 程序
		wordBean.setVisible(false);
		wordBean.createNewDocument();// 创建一个新文档
		wordBean.setLocation();// 设置打开后窗口的位置
		wordBean.insertText("你好");// 向文档中插入字符
		wordBean.insertJpeg("D:\\test\\testbook" + File.separator + "222.jpg"); // 插入图片
		// 如果 ，想保存文件，下面三句
		wordBean.saveFileAs("D:\\test\\testbook\\createANewFileTest.doc");
		wordBean.closeDocument();
		wordBean.closeWord();
	}

	/**
	 * 打开一个存在的文件 文件需要存在
	 */
	public static void openAnExistsFileTest() {
		TestJcob wordBean = new TestJcob();
		wordBean.setVisible(false); // 是否前台打开word 程序，或者后台运行
		wordBean.openFile("D:\\test\\testbook\\createANewFileTest.doc");
		wordBean.insertJpeg("D:\\test\\testbook\\" + File.separator + "bg.jpg"); // 插入图片(注意刚打开的word
		// ，光标处于开头，故，图片在最前方插入)
		wordBean.save();
		wordBean.closeDocument();
		wordBean.closeWord();
	}

	/**
	 * 设置format
	 * 
	 * @param str
	 */
	public static void insertFormatStr(String str) {
		TestJcob wordBean = new TestJcob();
		wordBean.setVisible(false); // 是否前台打开word 程序，或者后台运行
		wordBean.createNewDocument();// 创建一个新文档
		wordBean.insertFormatStr(str);// 插入一个段落，对其中的字体进行了设置
	}

	/**
	 * 插入表格
	 */
	public static void insertTableTest() {
		TestJcob wordBean = new TestJcob();
		wordBean.setVisible(false); // 是否前台打开word 程序，或者后台运行
		wordBean.createNewDocument();// 创建一个新文档
		wordBean.setLocation();
		wordBean.insertTable("表名", 3, 2);
		wordBean.saveFileAs("D:\\test\\testbook\\createANewFileTest.doc");
		wordBean.closeDocument();
		wordBean.closeWord();
	}

	/**
	 * 合并表格
	 */
	public static void mergeTableCellTest() {
		insertTableTest();// 生成d://table.doc
		TestJcob wordBean = new TestJcob();
		wordBean.setVisible(false); // 是否前台打开word 程序，或者后台运行
		wordBean.openFile("D:\\test\\testbook\\createANewFileTest.doc");
		wordBean.mergeCellTest();
	}

	public static void main(String[] args) {
		// 进行测试前要保证d://a.jpg 图片文件存在
		createANewFileTest();// 创建一个新文件
		openAnExistsFileTest();// 打开一个存在 的文件
		insertFormatStr("格式 化字符串");// 对字符串进行一定的修饰
		insertTableTest();// 创建一个表格
		mergeTableCellTest();// 对表格中的单元格进行合并
	}
}
