package demo.net.tcpcall;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * sockets 通信 多线程客户端 有两个线程 一个是用来捕获键盘的输入 一个是接受服务器的消息
 * 
 * @author fule
 * 
 */
public class MutilThreadClient {

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException,
			IOException {
		// TODO Auto-generated method stub
	    final Socket socket = new Socket("127.0.0.1", 3001);
		//socket.connect(new InetSocketAddress("127.0.0.1", 3001), 2000);// 连超时时间
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					BufferedReader bf = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					String content;
					while ((content = bf.readLine()) != null) {
						System.out.println(content);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// 获取该Socket对应的输出流
		PrintStream ps = new PrintStream(socket.getOutputStream());
		String line = null;
		// 不断读取键盘输入
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while ((line = br.readLine()) != null) {
			// 将用户的键盘输入内容写入Socket对应的输出流
			ps.println("来至于 客户端:"+line);
		}
	}
}
