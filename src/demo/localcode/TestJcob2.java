package demo.localcode;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComException;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class TestJcob2 {
	/** 声明一个word对象 */
	private ActiveXComponent objWord;
	/** 声明四个word组件 */
	private Dispatch custDocprops;

	private Dispatch builtInDocProps;

	private Dispatch document;

	private Dispatch wordObject;

	/**
	 * 打开word文挡
	 */
	public void open(String filename) {
		// 创建一个word对象
		objWord = new ActiveXComponent("Word.Application");
		// 为wordobject组件附值
		wordObject = (Dispatch) (objWord.getObject()); // 改了这里
		// 生成一个只读方式的word文挡组件
		Dispatch.put(wordObject, " Visible ", new Variant(false));
		// 获取文挡属性
		Dispatch documents = objWord.getProperty(" Documents ").toDispatch();
		// 打开激活文挡
		document = Dispatch.call(documents, " Open ", filename).toDispatch();
	}

	public void selectCustomDocumentProperitiesMode() {
		custDocprops = Dispatch.get(document, " CustomDocumentProperties ")
				.toDispatch();
	}

	public void selectBuiltinPropertiesMode() {
		builtInDocProps = Dispatch.get(document, " BuiltInDocumentProperties ")
				.toDispatch();
	}

	/**
	 * 关闭文挡
	 */
	public void close() {
		Dispatch.call(document, " Close ");
	}

	public String getCustomProperty(String cusPropName) {
		try {
			cusPropName = Dispatch.call((Dispatch) custDocprops, " Item ",
					cusPropName).toString();
		} catch (ComException e) {
			cusPropName = null;
		}
		return cusPropName;
	}

	public String getBuiltInProperty(String builtInPropName) {
		try {
			builtInPropName = Dispatch.call((Dispatch) builtInDocProps,
					" Item ", builtInPropName).toString();
		} catch (ComException e) {
			builtInPropName = null;
		}
		return builtInPropName;
	}

	public static void main(String[] args) {
		try {
			TestJcob2 jacTest = new TestJcob2();
			jacTest.open("D:\\test\\TestJod3\\testJc.doc");
			jacTest.selectCustomDocumentProperitiesMode();
			jacTest.selectBuiltinPropertiesMode();
			String custValue = jacTest
					.getCustomProperty(" Information Source ");
			String builtInValue = jacTest.getBuiltInProperty(" Author ");
			jacTest.close();
			System.out.println(" Document Val One:  " + custValue);
			System.out.println(" Document Author:  " + builtInValue);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
