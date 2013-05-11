package demo.net.iprequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
public class GetPostTest {
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，格式满足name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 * @throws IOException
	 */
	public static String sendGet(String url, String param) throws IOException {
		String result = "";
		String urlName = url + "?" + param;
		System.out.println(urlName);
		URL realUrl = new URL(urlName);
		// 打开和URL之间的连接
		URLConnection conn = realUrl.openConnection();
		// 设置通用的请求属性
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		// 建立实际的连接
		conn.connect();
		// 获取所有响应头字段
		Map<String, List<String>> map = conn.getHeaderFields();
		// 遍历所有的响应头字段
		for (String key : map.keySet()) {
			System.out.println(key + "--->" + map.get(key));
		}
		BufferedReader in = null;

		// 定义BufferedReader输入流来读取URL的响应
		in = new BufferedReader(new InputStreamReader(conn.getInputStream(),
				"utf-8"));
		String line;
		while ((line = in.readLine()) != null) {
			result += "\n" + line;
		}
		return result;
	}

	/**
	 * 向指定URL发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，格式应该满足name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 * @throws IOException 
	 */
	public static String sendPost(String url, String param) throws IOException {
		String result = "";

		URL realUrl = new URL(url);
		// 打开和URL之间的连接
		URLConnection conn = realUrl.openConnection();
		// 设置通用的请求属性
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);

		// 获取URLConnection对象对应的输出流
		PrintWriter out = new PrintWriter(conn.getOutputStream());
		// 发送请求参数
		out.print(param);
		// flush输出流的缓冲
		out.flush();

		// 定义BufferedReader输入流来读取URL的响应
		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "utf-8"));
		String line;
	//	FileWriter wfWriter = new FileWriter("D:\\test.html");//写入文件
		
		while ((line = in.readLine()) != null) {
			result += "\n" + line;
		//	wfWriter.write(line);
		}
	//	wfWriter.flush();
   //	wfWriter.close();
		return result;
	}

	// 提供主方法，测试发送GET请求和POST请求
	public static void main(String args[]) throws IOException {
		// 发送GET请求
		//String s = GetPostTest.sendGet("http://localhost:8888/wmis/manage/wms/Records/fristStatistics.view", "voteid=201001081917205007");
		///System.out.println(s);
		// 发送POST请求
		StringBuffer parbuf = new  StringBuffer();
		//parbuf.append("form_email=244104850@qq.com");
		//parbuf.append("&form_password=zfu4402179");
		//parbuf.append("&user_login=登录");
		//parbuf.append("&source=simple");
		//parbuf.append("&redir=http://www.douban.com");
		
		//parbuf.append("&captcha-solution:absent");//验证码
		//parbuf.append("&captcha-id:yotn17UANKVD4Rf2HYJs5q95");
		
		//String s1 = GetPostTest.sendGet("http://localhost:8888/metservice/update/update.show",
		//		parbuf.toString());
		//System.out.println(new String(s1.getBytes("ISO-8859-1"),"utf-8"));
		
		
		String string = GetPostTest.sendPost("http://www.jxsl.gov.cn/sltvote/checkLogin.jsp",
				"IDCard:1212121&password:12121212");
		
		System.out.println(string);
	}
}



