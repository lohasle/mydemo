package demo.net.tcpcall;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * sockets 通信 多线程服务端 一个客户端对应 一个线程
 * 
 * @author fule
 * 
 */
public class MutilThreadServer {
	private static List<Socket> socketList = new LinkedList<Socket>();// 客户端socket队列

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = new ServerSocket(3001);
		while (true) {
			final Socket socket = serverSocket.accept();
			socketList.add(socket);
			new Thread(new Runnable() {
				private Socket s = socket;
				BufferedReader bf = new BufferedReader(new InputStreamReader(
						s.getInputStream()));

				@Override
				public void run() {
					// TODO Auto-generated method stub
					String line = null;
					try {
						while ((line=readFromClient() )!= null) {
							for (Socket sc : MutilThreadServer.socketList) {
								PrintStream pStream = new PrintStream(
										sc.getOutputStream());
								pStream.print("服务器端："+line);
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// 得到客户端数据
				private String readFromClient() {
					try {
						System.out.println(bf.readLine());
						return bf.readLine();
					} catch (Exception e) {
						// 没有获取到说明客户端已经 关闭
						MutilThreadServer.socketList.remove(s);
					}
					return null;
				}
			}).start();
		}
	}

}
