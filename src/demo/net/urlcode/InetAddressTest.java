package demo.net.urlcode;

import java.io.IOException;
import java.net.InetAddress;

/**
 * 表示ip地址的类  InetAddress
 * @author fule
 *
 */
public class InetAddressTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//根据主机名 获得InetAddress实例
		InetAddress ip = InetAddress.getByName("www.sina.com");
		
		InetAddress ip2 = InetAddress.getByAddress(new byte[]{10,81,66,2});
		//是否可达
		System.out.println(ip.isReachable(2000));
		//主机名
		String hostNameString = ip.getHostName();
		System.out.println("hostNameString:"+hostNameString);
		
		System.out.println("实例对应的全限定名:"+ip.getCanonicalHostName());
		
		System.out.println("HostAddress:"+ip.getHostAddress());
		
		
		System.out.println("实例对应的全限定名:"+ip2.getCanonicalHostName());
		System.out.println("hostNameString:"+ip2.getHostName());
	}

}
