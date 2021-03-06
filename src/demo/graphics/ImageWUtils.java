﻿package demo.graphics;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

/**
 * 文字写入图层背景工具类
 * 
 * 内容过多自动分页
 * 
 * 落款自动写入格式
 * 
 * @author fule
 */
public class ImageWUtils {

	// 常量字体大小
	public final static int LARGESIZE = 100; // 大字体
	public final static int NORMALSIZE = 50; // 普通字体
	public final static int SMALLSIZE = 30; // 小字体
	// 段间距
	public final static int SECTSIZE = 80;
	// 文字区边距
	public final static int PADDING = 100;
	public final static Color DEAUFT_COLOR = Color.WHITE;

	// 连续书写时的记录位
	int pointIndex;

	// title书写之后的记录位
	int pointOnTitle;

	// 当前第几页
	int indexPage = 1;

	Map<Integer, String> map;

	/**
	 * 设置当前书写的像素位子
	 * 
	 * @return
	 */
	private int setPointIndex(int increment) {
		pointIndex += increment;
		return pointIndex;
	}

	/**
	 * 内容区字体大小
	 */
	private int contentFontSize = NORMALSIZE;

	/**
	 * 标题字体大小
	 */
	private int titleFontSize = LARGESIZE;

	/**
	 * 段间距
	 */
	private int secttSize = SECTSIZE;

	/**
	 * 文本域边距大小
	 */
	private int contentPadding = PADDING;

	/**
	 * 画笔颜色
	 */

	private Color color;

	/**
	 * 背景图片
	 */
	private File bgFile;

	/**
	 * 设置背景图
	 * 
	 * @param bgFile
	 */
	public void setBgFile(File bgFile) {
		this.bgFile = bgFile;
	}

	/**
	 * 设置画笔颜色 默认白色
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * 设置内容区字体大小 默认NORMALSIZE = 50
	 */
	public void setContentFontSize(int contentFontSize) {
		this.contentFontSize = contentFontSize;
	}

	/**
	 * 设置标题字体大小 默认LARGESIZE = 100
	 */
	public void setTitleFontSize(int titleFontSize) {
		this.titleFontSize = titleFontSize;
	}

	/**
	 * 设置段间距 默认SECTSIZE = 70
	 * 
	 * @param secttSize
	 */
	public void setSecttSize(int secttSize) {
		this.secttSize = secttSize;
	}

	/**
	 * 设置文本边距 默认PADDING70
	 * 
	 * @param contentPadding
	 */
	public void setContentPadding(int contentPadding) {
		this.contentPadding = contentPadding;
	}

	/**
	 * 合成图片 title 标题 testContext 段落
	 * 分页产生的图片名
	 * 递增1  
	 * @return
	 * @throws Exception
	 */

	public int compound(String strTitleString, String context,
			String[] luokuan) throws Exception {
		int resultCount=0;//生成的图片张数
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(bgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int imgWidth = bi.getWidth();
		int ImgHeight = bi.getHeight();
		Graphics2D g = getGraphics(bi);
		Font titleFont = new Font("微软雅黑", Font.PLAIN, titleFontSize);// 标题
		Font contentFont = new Font("微软雅黑", Font.PLAIN, contentFontSize);// 内容
		Font luoKuanFont = new Font("微软雅黑", Font.PLAIN, contentFontSize);// 落款
		if (strTitleString == null && context == null && luokuan == null) {
			throw new Exception("没有数据，没有写入任何数据");
		} else {
			g.setFont(contentFont);
			Map<Integer, String> pagemap = getPageStrings(imgWidth, ImgHeight,
					strTitleString, context);
			resultCount = pagemap.size();
			for (int i = 1; i <= resultCount; i++) {
				pointIndex = 0;// 初始化
				String pageContent = pagemap.get(i);
				if (strTitleString != null) {
					drawTitle(titleFont, imgWidth, strTitleString, g);
				}
				String[] testContext = getContents(pageContent);// 每一页段数据
				if (testContext != null) {
					for (int j = 0; j < testContext.length; j++) {
						drawContent(imgWidth, testContext[j], g);
					}
					if (pagemap.size() == 1 || i == pagemap.size()) {
						setPointIndex(2 * secttSize);// 设置落款 开始位置
						if (luokuan != null) {
							drawLuoKuan(imgWidth, luokuan, luoKuanFont, g);
						}
					}
				}
				try {
					String fatherPath = bgFile.getParent();
					String fileAbName = bgFile.getName();
					String fn = fileAbName.substring(0,fileAbName.lastIndexOf("."));//文件名前缀
					String filenameString = fatherPath+"/"+fn + i + ".jpg";
					File f = new File(filenameString);
					ImageIO.write(bi, "jpg", f);
					bi = ImageIO.read(bgFile);
					g = getGraphics(bi);
					g.setFont(contentFont);
				} catch (IOException e) {
					e.printStackTrace();
					return 0;
				}
			}
			g.dispose();
			bgFile.delete();//删除背景
			System.out.println("合成完毕");
			return resultCount;
		}

	}//end

	private Graphics2D getGraphics(BufferedImage bi) {
		Graphics2D g = bi.createGraphics();
		// 字体平滑
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(color);
		return g;
	}

	/**
	 * 写入标题 居中 字数过多换行
	 * 
	 * @param width
	 * @param title
	 * @param g
	 */
	public void drawTitle(Font font, int width, String title, Graphics2D g) {
		AttributedString as = null;
		title = ToSBC(title);
		int fullLen = (width - contentPadding * 2) / titleFontSize;// 一行的最多容量
		int titleLen = title.length();
		if (titleLen > fullLen) {
			String s = title;
			int writeLen = (s.length() + fullLen - 1) / fullLen;// 可写入行数
			String string;
			int sum = 0;
			for (int i = 0; i < writeLen - 1; i++) {
				int begin = i * fullLen;
				int end = begin + fullLen;
				string = s.substring(begin, end);// TODO
				int sl = string.length();// 实际每行写入字数
				as = new AttributedString(string);
				as.addAttribute(TextAttribute.FONT, font);
				g.drawString(as.getIterator(), width / 2 - sl / 2
						* titleFontSize, pointIndex
						+ (titleFontSize + secttSize / 2) * (i + 1));// i+1
																		// 去除二段重行
			}
			string = s.substring((writeLen - 1) * fullLen);
			as = new AttributedString(string);
			as.addAttribute(TextAttribute.FONT, font);
			g.drawString(as.getIterator(), width / 2 - string.length() / 2
					* titleFontSize, pointIndex
					+ (titleFontSize + secttSize / 2) * writeLen);// 最后一行
			sum += (titleFontSize + secttSize) * writeLen;
			setPointIndex(sum);
		} else {
			as = new AttributedString(title);
			as.addAttribute(TextAttribute.FONT, font);
			g.drawString(as.getIterator(), width / 2 - titleLen / 2
					* titleFontSize, setPointIndex(titleFontSize + secttSize));

		}
		pointOnTitle = pointIndex;
	}

	/**
	 * 写入落款
	 * 
	 * 从后面往前写
	 * 
	 * @param width
	 * @param wsString
	 *            内容
	 * @param g
	 * @throws Exception
	 */
	public void drawLuoKuan(int width, String[] wsString, Font font,
			Graphics2D g) throws Exception {
		AttributedString as = null;
		int padding = 2;
		int fontsize = font.getSize();
		int fullLen = (width - contentPadding * 2) / fontsize;// 一行的最多容量
		for (int i = 0; i < wsString.length; i++) {
			wsString[i] = ToSBC(wsString[i]);
			if (wsString[i].length() > fullLen) {
				throw new Exception("落款长度过长");
			}
			as = new AttributedString(wsString[i]);
			as.addAttribute(TextAttribute.FONT, font);
			if (wsString[0].length() > wsString[1].length()) {
				if (i == 0) {
					g.drawString(as.getIterator(),
							(fullLen - wsString[0].length() + padding)
									* fontsize, setPointIndex(fontsize
									+ secttSize));
					setPointIndex(-(secttSize / 2));
				} else {
					g.drawString(
							as.getIterator(),
							(fullLen
									- wsString[1].length()
									+ ((wsString[1].length() - wsString[0]
											.length()) / 2) + padding)
									* fontsize, setPointIndex(fontsize
									+ secttSize));
				}
			} else if (wsString[0].length() < wsString[1].length()) {
				if (i == 0) {
					g.drawString(
							as.getIterator(),
							(fullLen
									- wsString[0].length()
									+ ((wsString[0].length() - wsString[1]
											.length()) / 2) + padding)
									* fontsize, setPointIndex(fontsize
									+ secttSize));
					setPointIndex(-(secttSize / 2));
				} else {
					g.drawString(as.getIterator(),
							(fullLen - wsString[1].length() + padding)
									* fontsize, setPointIndex(fontsize
									+ secttSize));
				}
			}else {
				g.drawString(as.getIterator(),
						(fullLen - wsString[0].length() + padding)
								* fontsize, setPointIndex(fontsize
								+ secttSize));
				setPointIndex(-(secttSize / 2));
			}
		}

	}

	/**
	 * 文本边距为1个字符，首行缩进2个字符
	 * 
	 * 写入段落 内容区 一个字的像素为SMALLSIZE
	 * 
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @param content
	 * @param g
	 */
	public void drawContent(int width, String content, Graphics2D g) {
		content = ToSBC(content);
		int fullFontLen = (width - contentPadding * 2) / contentFontSize;// 满行写入
																			// 可写长度(字数)
		String duanshou;
		int writeLen;
		int sum = 0;
		if (content.length() < fullFontLen - 2) {
			duanshou = content;
			writeLen = 1;
			g.drawString(duanshou, contentPadding + 2 * contentFontSize,
					setPointIndex(2 * secttSize));
		} else {
			duanshou = content.substring(0, fullFontLen - 2);// 段首 缩进两个字
			writeLen = (content.substring(duanshou.length() + 1).length()
					+ fullFontLen - 1)
					/ fullFontLen; // 可写入行数 不含段首行

			// 写入段首 段的开始 2*secttSize 段分割行
			g.drawString(duanshou, contentPadding + 2 * contentFontSize,
					setPointIndex(2 * secttSize));
			// 除去段首的内容区
			String str = content.substring(duanshou.length());
			// 写入内容
			String string;
			for (int i = 0; i < writeLen - 1; i++) {
				int begin = i * fullFontLen;
				int end = begin + fullFontLen;
				string = str.substring(begin, end);// TODO
				g.drawString(string, contentPadding, pointIndex + secttSize
						+ secttSize * i);
			}
			string = str.substring((writeLen - 1) * fullFontLen);
			g.drawString(string, contentPadding, pointIndex + secttSize
					+ secttSize * (writeLen - 1));// 最后一行d
			sum += ((secttSize) * (writeLen));// 段落之间的间隙
			setPointIndex(sum);
		}

	}

	/**
	 * 是否存在下一页
	 * 
	 * @param point
	 * @param height
	 * @return
	 */
	public boolean hasNextPage(int height) {
		if (pointIndex > (height - 2 * secttSize)) {
			// 存在下一页新页开始
			pointIndex = 0;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 写入内容时是否产生分页 TODO 第一段就超出页 除第一段之后的超页
	 * 
	 * @param height
	 *            高度
	 * @param width
	 *            宽度
	 * @param testContext
	 * @return map<页码,内容>
	 */
	private Map<Integer, String> getPageStrings(int width, int height,
			String title, String testContext) {
		title = ToSBC(title);
		int titleFullLen = (width - contentPadding * 2) / titleFontSize;// 一行标题的最多容量
		int titleWriteLen = (title.length() + titleFullLen - 1) / titleFullLen;// 标题占有行
		int titleHeight = titleWriteLen * (titleFontSize + secttSize);// 标题高度

		int fullFontLen = (width - contentPadding * 2) / contentFontSize;// 满行写入可写长度(字数)

		int contentHeight = getDuanLuoHeight(fullFontLen, titleHeight, height,
				testContext);

		// int contentHeight = getContents(testContext).length * 2 *
		// secttSize;// 满区内容段落间距总高度

		int bgTotalRows = (height - titleHeight - contentHeight) / secttSize
				- 3;// 除去落款,背景可书写总行数

		int vTotalRows = (testContext.length() + fullFontLen - 1) / fullFontLen;// 实际内容所需行数

		// TDOD
		int fullWord = bgTotalRows * fullFontLen;// 满屏写入内容字数
		if (map == null) {
			map = new HashMap<Integer, String>();
		}
		if (vTotalRows < bgTotalRows) {
			map.put(indexPage, testContext);
		} else {
			int count = 0;

			int pageCount = (vTotalRows + bgTotalRows - 1) / bgTotalRows;// 需要总页数
			String string;
			for (int i = count; i < pageCount; i++) {
				if (i == pageCount - 1) {
					string = testContext.substring(i * fullWord);// 最后一页
				} else {
					string = testContext.substring(i * fullWord, (i + 1)
							* fullWord);
				}
				// System.out.println("当页内容:\n"+string);
				map.put(indexPage, string);
				indexPage++;

			}
		}
		System.out.println("总页数" + map.size());
		return map;
	}

	// 每页段落总间隙
	// 如果是第一页 写入 如果是其他的 算满屏额
	private int getDuanLuoHeight(int fullFontLen, int titleHeight, int height,
			String testContext) {
		int bgTotalRows = (height - titleHeight) / secttSize - 4;// 满页可写入行数
		int fullWord = bgTotalRows * fullFontLen;// 满屏写入内容字数
		int wlen = testContext.length();
		if (wlen < fullWord) {
			return getContents(testContext).length * 2 * secttSize;
		} else {
			return getContents(testContext.substring(0, fullWord)).length * 2
					* secttSize;
		}
	}

	/**
	 * 字符串处理
	 */
	private String[] getContents(String str) {
		while (str.startsWith("\r\n")) {
			str = str.substring(2);
		}
		String[] a = str.split("\\s{1,}");
		return a.length == 0 ? null : a;
	}

	/**
	 * 字符串处理
	 * 
	 * @param str
	 * @return
	 * @deprecated
	 */
	private String[] getContent(String str) {

		String string = str.replace("\r\n", "");
		string = string.replace("<p>", "{");
		string = string.replace("</p>", "}");
		List<String> list = new ArrayList<String>();
		while (string.indexOf("{") > -1) {
			int start = string.indexOf("{") + 1;
			int end = string.indexOf("}");
			String st = string.substring(start, end);
			list.add(st);
			string = string.substring(end + 1);
		}
		for (int i = 0; i < list.size(); i++) {
			if ("".equals(list.get(i)) || list.get(i) == null) {
				list.remove(i);
			}
		}
		String[] a = list.toArray(new String[0]);
		return a.length == 0 ? null : a;
	}

	// 半角转全角：
	public static String ToSBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}
			if (c[i] < 127)
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}

	
	public static char conversionNum(int c){
		char rc = 0;
		switch (c) {
			case 1: rc='一';
				break;
			case 2:rc='二';
				break;
			case 3:rc='三';
				break;
			case 4:rc='四';
				break;
			case 5:rc='五';
				break;
			case 6:rc='六';
				break;
			case 7:rc='七';
				break;
			case 8:rc='八';
				break;
			case 9:rc='九';
				break;
			case 10:rc='十';
			break;
			case 0:rc='〇';
		}
		return rc;
	}
	
	//解析月/日
	public static String conversionMouth(int a){
		if (a<=10) {
			return String.valueOf(conversionNum(a));
		}else {
			int gw=a%10;
			int sw=a/10;
			String swString = String.valueOf(conversionNum(sw));
			String gwString = String.valueOf(conversionNum(gw));
			if (a<20) {
				return "十"+gwString;
			}else {
				return swString+"十"+gwString;
			}
		}
	}
	
	//首零的处理
	public String removeBeginZ(String str){
		if (str.startsWith("0")) {
			int index = str.indexOf("0");
			return str.substring(index);
		}else {
			return str;
		}
		
	}
	
	/**
	 * 转化为中文日期格式
	 * 形式如：二〇一〇年十月二十一
	 * 转入格式：yyyy-MM-dd
	 * @param str
	 * @return 中文日期格式
	 * @throws Exception 
	 */
	public static String toCN_Date(String str) throws Exception{
		String eL = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";//匹配正则
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(str);
		boolean dateFlag = m.matches();
		if (dateFlag) {
			String[] strings = str.split("-");
			char[] y = strings[0].toCharArray();//年
			StringBuffer cndate = new StringBuffer();
			for (int i = 0; i < y.length; i++) {
				char cy =conversionNum((int)y[i]-48);
				cndate.append(cy);
			}
				String mounth = conversionMouth(Integer.parseInt(String.valueOf(strings[1])));
				String daString = conversionMouth(Integer.parseInt(String.valueOf(strings[2])));
				cndate.append("年"+mounth+"月");
				cndate.append(daString+"日");
			return cndate.toString();
		}else {
			throw new Exception("请使用正确格式的日期字符串");
		}
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 * 
	 */

	public static void main(String[] args) throws Exception {
	/*	
		ImageWUtil iUtil = new ImageWUtil();
		try {
			System.out.println(iUtil.toCN_Date("2012-12-22"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		iUtil.setBgFile(new File("D:/testbook/bg.jpg"));
		iUtil.setColor(Color.WHITE);
		iUtil.setTitleFontSize(60);
		iUtil.setContentFontSize(45);
		iUtil.setContentPadding(70);
		iUtil.setSecttSize(70);
		String d = "";
		FileInputStream fInputStream = new FileInputStream(new File(
				"D:/testbook/testbook.txt"));
		byte[] by2 = new byte[5 * 1024];

		int temp = fInputStream.read(by2);

		d = new String(by2, 0, temp);

		// System.out.println(d);
		fInputStream.close();
		String t = "编程改革";
		String lk1 = "卡卡卡办公厅发";
		String lk2 = "二零一二年世界的风行 谨在此";
		try {
			iUtil.compound(t, d, new String[] { lk1, lk2 });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 String
		  test="\r\n\r\n\r\n\r\n\r\nasasa\r\n\r\nsasa\r\n\r\nsdasda\r\n\r\n";
		 String s[] =iUtil.getContents(test); System.out.println(s.length);
		 for (String string : s) { System.out.println(string); }
		 */
	}

}
