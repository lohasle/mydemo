package demo.net.tcpcall.shStream;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException,
			IOException {
		// TODO Auto-generated method stub
		Socket s = new Socket("localhost", 30000);
		Scanner scan = new Scanner(s.getInputStream());
		while (scan.hasNextLine()) {
			System.out.println(scan.nextLine());
		}
		PrintStream ps = new PrintStream(s.getOutputStream());
		ps.println("客户端的第一行数据");
		ps.println("客户端的第二行数据");
		ps.close();
		scan.close();
		s.close();
	}

}
