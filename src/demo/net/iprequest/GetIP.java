package demo.net.iprequest;

import java.net.*;
import java.util.*;
/**
 * liunx 地址
 * @author fule
 *
 */
public class GetIP {
	public static void main(String[] args) throws SocketException {
		InetAddress local;
		for (Enumeration<NetworkInterface> i = NetworkInterface
				.getNetworkInterfaces(); i.hasMoreElements();) {
			NetworkInterface ni = i.nextElement();
			for (Enumeration<InetAddress> j = ni.getInetAddresses(); j
					.hasMoreElements();) {
				local = j.nextElement();

				if (local.isSiteLocalAddress() && !local.isLoopbackAddress()
						&& (local.getHostAddress().indexOf(":") == -1)) {
					System.out.println(local.getHostAddress());
				}
			}
		}
	}
}