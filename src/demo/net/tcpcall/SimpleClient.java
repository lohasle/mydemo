package demo.net.tcpcall;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * sockets 通信 简单客户端
 * 
 * @author fule
 * 
 */
public class SimpleClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Socket socket = new Socket("127.0.0.1", 3000);
			// socket.setSoTimeout(2000);//设置超时 ①
			socket.connect(new InetSocketAddress("127.0.0.1", 3000), 2000);// 设置超时②
																			// 真正的与主机
																			// 连接超时
			InputStream inputStream = socket.getInputStream();
			BufferedReader bfBufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String lineString = bfBufferedReader.readLine();
			System.out.println("此条消息来至于服务器:" + lineString);
			bfBufferedReader.close();
			socket.close();
		} catch (SocketTimeoutException e) {
			System.out.println("请求超时");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
