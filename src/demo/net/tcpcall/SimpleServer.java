package demo.net.tcpcall;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * sockets 通信 简单服务端
 * @author fule
 *
 */
public class SimpleServer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = new ServerSocket(3000);//新建端口为3000的服务端
		while(true){
			Socket socket = serverSocket.accept();//接受socket请求，客户端请求就创建一个新的socket
			PrintStream pStream =  new PrintStream(socket.getOutputStream());
			pStream.print("hello 我是服务器！");
			pStream.close();;
			socket.close();
		}
	}

}
