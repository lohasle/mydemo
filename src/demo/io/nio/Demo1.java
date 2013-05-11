package demo.io.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class Demo1 {
	
	private static String testfileUrl = "D://test//testbook//ImageWUtil.java";

	private static void nioReader1() throws IOException{
		FileInputStream fileInputStream = new FileInputStream(new File(testfileUrl));
		//用于读取、写入、映射和操作文件的通道
		FileChannel fc = fileInputStream.getChannel();
		//分配一个新的字节缓冲区
		ByteBuffer bb =  ByteBuffer.allocate(1024);
		fc.read(bb);
		byte[] by = bb.array();
		System.out.println(new String(by));
		fc.close();
	}
	
	private static void ioReader1() throws IOException{
		FileInputStream fileInputStream = new FileInputStream(new File(testfileUrl));
		byte[] b =new byte[1024];
		fileInputStream.read(b);
		System.out.println(new String(b));
		fileInputStream.close();
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		try {
			nioReader1();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("用时："+(end-startTime));
	}
}
