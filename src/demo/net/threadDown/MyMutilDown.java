package demo.net.threadDown;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
/**
 * 
 * 多线程的下载
 * @author fule
 * 
 */
public class MyMutilDown {

	/**
	 * 
	 * 多线程的下载
	 * 
	 */
	public void downLoad(String url, String filePath, int threadNum)
			throws IOException {

		// 要写入的文件
		File file = new File(filePath + getFileExtName(url));
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		// 超时时间
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		// 设置通用的请求属性
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		int fileSize = conn.getContentLength();// 文件大小
		System.out.println(fileSize);
		RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
		accessFile.setLength(fileSize);// 设置长度;
		accessFile.close();

		// 每个线程负责下载的文件大小
		int block = (fileSize + threadNum - 1) / threadNum;
		if (conn.getResponseCode() == 200) {// 请求成功
			for (int i = 0; i < threadNum; i++) {
				new DownThread(i, file, block, u).start();
			}
		}
	}

	/**
	 * 文件后缀名
	 * 
	 * @param path
	 * @return
	 */
	public String getFileExtName(String path) {
		return path.substring(path.lastIndexOf("."));
	}

	/**
	 * 单线程的远程下载
	 */
	public void testOneTDown(String filePath, String url) {
		try {
			// 要写入的文件
			File file = new File(filePath + getFileExtName(url));
			FileWriter fWriter = new FileWriter(file);
			URL ul = new URL(url);
			URLConnection conn = ul.openConnection();
			conn.setConnectTimeout(2000);// 请求超时时间
			// int len = conn.getContentLength();
			InputStream in = conn.getInputStream();
			// byte[] by = new byte[1024];
			int temp = 0;
			while ((temp = in.read()) != -1) {
				fWriter.write(temp);
			}
			fWriter.close();
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 测试多线程
	 * 
	 * @param filePath
	 *            文件保存路径
	 * @param url
	 *            url
	 * @param tnum
	 *            线程数量
	 */
	public void testMoreTDown(String filePath, String url, int tnum) {
		try {
			// 要写入的文件
			final File file = new File(filePath + getFileExtName(url));
			RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");// 建立随机访问
			final URL ul = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) ul.openConnection();
			conn.setConnectTimeout(2000);// 请求超时时间
			conn.setRequestMethod("GET");
			int len = conn.getContentLength();// 文件长度
			accessFile.setLength(len);
			accessFile.close();
			final int block = (len + tnum - 1) / tnum;// 每个线程下载的快大小
			
			for (int i = 0; i < tnum; i++) {
				final int a = i;
				new Thread(new Runnable() {
					int start = block * a;// 开始位置
					int end = block * (a + 1) - 1;// 结束位置
					@Override
					public void run() {
						HttpURLConnection conn2 = null;//http连接
						RandomAccessFile accessFile2 = null;
						InputStream in = null;//从http连接拿到的流
						try {
							conn2 = (HttpURLConnection) ul.openConnection();
							conn2.setConnectTimeout(2000);// 请求超时时间
							conn2.setRequestMethod("GET");
							// TODO Auto-generated method stub
							conn2.setRequestProperty("Range", "bytes=" + start
									+ "-" + end + "");// 设置一般请求属性 范围
							in = conn2.getInputStream();
							byte[] data = new byte[1024];
							int len = 0;
							accessFile2 = new RandomAccessFile(file, "rwd");
							accessFile2.seek(start);
							
							while ((len = in.read(data)) != -1) {
								accessFile2.write(data, 0, len);
							}
							System.out.println("线程:" + a + "下载完成!");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} finally {
							try {
								accessFile2.close();
								in.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}).start();
			  
			}
			

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		MyMutilDown mydown = new MyMutilDown();
		String path = "http://static.ishare.down.sina.com.cn/5585234.txt?ssig=f7CrV3UL8%2B&Expires=1347724800&KID=sina,ishare&ip=1347592902,117.40.138.&fn=%E5%8E%9A%E9%BB%91%E5%AD%A6.TXT";
		// mydown.downLoad(path, "D:\\aa", 1);
		// mydown.testOneTDown("D:\\", path);
		mydown.testMoreTDown("D:\\aa", path, 3);
	}

	public class DownThread extends Thread {
		private int id; // 线程id

		private File file;// 目标文件

		private int block;// 每个线程下载文件的大小

		private URL url;

		public DownThread() {
		}

		public DownThread(int id, File file, int block, URL url) {
			this.id = id;
			this.file = file;
			this.block = block;
			this.url = url;
		}

		@Override
		public void run() {
			int start = block * id;// O kaishi
			int end = block * (id + 1) - 1;
			try {
				RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
				accessFile.seek(start);// 设置操作文件的入点
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5 * 1000);
				conn.setRequestMethod("GET");
				// 指定网络位置从什么位置开始下载,到什么位置结束
				conn.setRequestProperty("Range", "bytes=" + start + "-" + end
						+ "");// 设置一般请求属性 范围
				InputStream in = conn.getInputStream();// 获得输入流
				byte[] data = new byte[1024];
				int len = 0;
				while ((len = in.read(data)) != -1) {
					accessFile.write(data, 0, len);
				}
				accessFile.close();
				in.close();
				System.out.println("线程:" + id + "下载完成!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
