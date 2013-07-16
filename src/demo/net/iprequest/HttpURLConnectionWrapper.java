package demo.net.iprequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通过分析豆瓣的分析判断机器人的实际情况，发现豆瓣是根据ip以及cookie信息统计访问频率来确定是否为“机器人”， 有以下几种实际情况
 * <p>
 * 1.不带cookie信息访问 ，快速访问一段时间，ip会被禁掉；
 * </p>
 * <p>
 * 2.带cookie访问，快速访问一段时间，请求会被禁掉，这时候清掉cookie，可以恢复正常访问。
 * </p>
 * 基于这个测试结果，可以采用一个简单的方案：
 * <p>
 * 1.第一次请求没有cookie信息，但记录下返回的cookie.
 * </p>
 * <p>
 * 2.后面每个请求都带上这个cookie信息.
 * </p>
 * <p>
 * 3.如果请求被跳转到验证码页面，就不带cookie重试，并记录返回的cookie信息
 * </p>
 * <p>
 * 4.下一次请求带上新的cookie信息。 重复2到4步骤。
 * </p>
 * 
 * @author fule
 * 
 */
public class HttpURLConnectionWrapper extends HttpURLConnection {

	/**
	 * @param args
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws MalformedURLException,
			IOException {
		// TODO Auto-generated method stub
		HttpURLConnectionWrapper ht = new HttpURLConnectionWrapper(new URL(
				"http://192.168.123.20/serverWeb/print/order_print.jsp?orderId=167&orderType=pre_order"));
		BufferedReader in = new BufferedReader(new InputStreamReader(
				ht.getInputStream(), "utf-8"));
		String line;
		String result = "";
		while ((line = in.readLine()) != null) {
			result += "\n" + line;
			// wfWriter.write(line);
			System.out.println(result);
		}
		in.close();
		//System.out.println(result);
	}

	HttpURLConnection httpURLConnection;
	// 简单的CookieManager
	CookieManager cookieManager = CookieManager.getInstance();

	public HttpURLConnectionWrapper(URL u) throws IOException {
		super(u);
		httpURLConnection = (HttpURLConnection) u.openConnection();
		// setFollowRedirects(false);
		fillRequestHeadField();
	}

	/**
	 * 填充Request Header信息
	 */
	private void fillRequestHeadField() {
		httpURLConnection
				.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 5.1; rv:5.0) Gecko/20100101 Firefox/5.0");
		httpURLConnection
				.setRequestProperty("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpURLConnection.setRequestProperty("Accept-Language",
				"zh-cn,zh;q=0.5");
		httpURLConnection.setRequestProperty("Accept-Encoding",
				"GB2312,utf-8;q=0.7,*;q=0.7");
		httpURLConnection.setRequestProperty("Referer",
				"http://movie.douban.com/");
		httpURLConnection.setRequestProperty("Cache-Control", "max-age=0");
		httpURLConnection.setRequestProperty("Cookie",
				cookieManager.getCookies(url.getHost()));

	}

	@Override
	public InputStream getInputStream() throws IOException {
		InputStream is = httpURLConnection.getInputStream();
		// 取到输入流中后处理Cookie信息
		resolveCookies();
		int responseCode = getResponseCode();
		if (responseCode != 200 && responseCode != 404) {
			// 清除cookie并重新发请求
			CookieManager.getInstance().removeCookies(url.getHost());
			try {
				httpURLConnection.disconnect();
				is.close();
			} catch (Exception e) {
			}
			httpURLConnection = (HttpURLConnection) this.getURL()
					.openConnection();
			setFollowRedirects(false);
			fillRequestHeadField();
			is = httpURLConnection.getInputStream();
		}
		return is;
	}

	private void resolveCookies() {
		List<String> setCookies = getHeaderFields().get("Set-Cookie");
		if (setCookies != null && !setCookies.isEmpty()) {
			for (String setCookie : setCookies) {
				cookieManager.setCookies(this.url.getHost(), setCookie);
			}
		}
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		httpURLConnection.disconnect();
	}

	@Override
	public boolean usingProxy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void connect() throws IOException {
		// TODO Auto-generated method stub

	}

}

/**
 * 简单的Cookie Manager，按照顶级域名管理Cookie信息
 * 
 * @author <a href="mailto:jingyu@taohua.com">惊羽</a>
 * 
 */
class CookieManager implements Serializable {
	private static final long serialVersionUID = 292218695837624307L;
	private static CookieManager cookieManager = new CookieManager();
	private Map<String, Map<String, String>> cookies = new ConcurrentHashMap<String, Map<String, String>>();

	private CookieManager() {
	}

	/**
	 * 根据域名获取对应的Cookie
	 * 
	 * @param domain
	 * @return
	 */
	public String getCookies(String domain) {
		Map<String, String> domainCookies = cookies
				.get(getTopLevelDomain(domain));
		if (domainCookies != null) {
			StringBuilder sb = new StringBuilder();
			boolean isFirst = true;
			for (Map.Entry<String, String> cookieEntry : domainCookies
					.entrySet()) {
				if (!isFirst) {
					sb.append("; ");
				} else {
					isFirst = false;
				}
				sb.append(cookieEntry.getKey()).append("=")
						.append(cookieEntry.getValue());
			}
			return sb.toString();
		}
		return "";
	}

	/**
	 * 
	 * 设置Cookie值
	 * 
	 * @param domain
	 * @param cookiesString
	 */
	public void setCookies(String domain, String cookiesString) {
		Map<String, String> domainCookies = cookies
				.get(getTopLevelDomain(domain));
		if (domainCookies == null) {
			domainCookies = new ConcurrentHashMap<String, String>();
			cookies.put(getTopLevelDomain(domain), domainCookies);
		}
		String[] cookies = cookiesString.split("; ");
		for (String cookie : cookies) {
			if (cookie != null && !cookie.trim().isEmpty()
					&& cookie.indexOf("=") > 0) {
				int equalMarkIndex = cookie.indexOf("=");
				String key = cookie.substring(0, equalMarkIndex);
				String value = cookie.substring(equalMarkIndex + 1);
				domainCookies.put(key, value);
			}
		}
	}

	/**
	 * 删除域名下所有的Cookie
	 * 
	 * @param domain
	 */
	public void removeCookies(String domain) {
		cookies.remove(getTopLevelDomain(domain));
	}

	/**
	 * 获取CookieManager的实例
	 * 
	 * @return
	 */
	public static CookieManager getInstance() {
		return cookieManager;
	}

	/**
	 * 获取域名的顶级域名
	 * 
	 * @param domain
	 * @return
	 */
	public String getTopLevelDomain(String domain) {
		if (domain == null) {
			return null;
		}
		if (!domainToTopLevelDomainMap.containsKey(domain)) {
			String[] splits = domain.split("\\.");
			domainToTopLevelDomainMap.put(domain, (splits[splits.length - 2]
					+ "." + splits[splits.length - 1]));
		}
		return domainToTopLevelDomainMap.get(domain);
	}

	/**
	 * 存储域名与其顶级域名之间映射关系，避免重复的计算顶级域名
	 */
	private Map<String, String> domainToTopLevelDomainMap = new ConcurrentHashMap<String, String>();
}
