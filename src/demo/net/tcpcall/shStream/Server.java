package demo.net.tcpcall.shStream;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * shutdownOutput用法
 * @author fule
 *
 */
public class Server {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

	
			ServerSocket serverSocket = new ServerSocket(30000);
			Socket socket = serverSocket.accept();
			PrintStream out = new PrintStream(socket.getOutputStream());
			out.print("the frist msg");
			socket.shutdownOutput();
			System.out.println(socket.isClosed());
			Scanner scanner = new Scanner(socket.getInputStream());
			while (scanner.hasNext()) {
				System.out.println(scanner.next());
			}
			scanner.close();
			socket.close();
			serverSocket.close();
		
	}

}
