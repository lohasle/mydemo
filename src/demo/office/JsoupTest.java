package demo.office;

import java.io.IOException;

import org.jsoup.Jsoup;
/**
 * 基于Jsoup抓取网页内容
 * @author 付乐
 * @createTime 2013-7-15
 */
public class JsoupTest {
    public static void main(String[] args) throws IOException {
        //目标页面
        String url = "http://192.168.123.20/serverWeb/print/order_print.jsp?orderId=167&orderType=pre_order";
        //使用Jsoup连接目标页面,并执行请求,获取服务器响应内容
        String html = Jsoup.connect(url).execute().body();
        //打印页面内容
        System.out.println(html);
        
        
    }
}
