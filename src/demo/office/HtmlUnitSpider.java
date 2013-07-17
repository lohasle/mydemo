package demo.office;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;

public class HtmlUnitSpider {
    public static void main(String[] s) throws Exception {
        //目标网页
        String url = "http://192.168.123.20/serverWeb/print/order_print.jsp?orderId=167&orderType=pre_order";
        //模拟特定浏览器FIREFOX_10
        WebClient spider = new WebClient(BrowserVersion.getDefault());
        //获取目标网页
        Page page = spider.getPage(url);
        //打印网页内容
        System.out.println(page.getWebResponse().getContentAsString());
        //关闭所有窗口
        spider.closeAllWindows();
    }
}
