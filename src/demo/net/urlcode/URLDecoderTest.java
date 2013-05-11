package demo.net.urlcode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 字符串编码类URLDecoder
 * @author fule
 *
 */
public class URLDecoderTest {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		// 将application/x-www-form-urlencoded字符串
				// 转换成普通字符串
		String keyWord = URLDecoder.decode("%C7%B0%B3%CC%CE%DE%D3%C7","gbk");
		System.out.println(keyWord);
		String enCodeWords = URLEncoder.encode("我爱你","utf-8");
		System.out.println(enCodeWords);
		
		System.out.println(URLDecoder.decode("http://117.34.15.27/3/ishare.down.sina.com.cn/26556816.doc?ssig=KqV%2FrUPj4X&Expires=1347638400&KID=sina,ishare&ip=1347526643,117.40.138.&fn=%E4%BC%81%E4%B8%9A%E5%9F%BA%E6%9C%AC%E7%AB%9E%E4%BA%89%E6%88%98%E7%95%A5%E9%80%89%E6%8B%A9.doc","utf-8"));
		String string = "http://117.34.15.27/3/ishare.down.sina.com.cn/26556816.doc?ssig=%2B1F3P7%2BsST&Expires=1347638400&KID=sina,ishare&ip=1347527453,117.40.138.&fn=%E4%BC%81%E4%B8%9A%E5%9F%BA%E6%9C%AC%E7%AB%9E%E4%BA%89%E6%88%98%E7%95%A5%E9%80%89%E6%8B%A9.doc";
		System.out.println(URLDecoder.decode(string,"utf-8"));
	}

}
