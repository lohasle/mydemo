package demo.net.threadDown;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 多线程下载带进度条
 * 
 * @author fule
 * 
 */
public class MutilDownBar {

	// 保存的文件路径
	private String path;
	// 远程url
	private String url;
	// 线程数量
	private int threadNum;

	// 定义下载的线程对象
	private DownThred[] threads;
	// 定义下载的文件的总大小
	private int fileSize;

	public MutilDownBar(String url, String path, int threadNum) {
		this.path = url;
		this.threadNum = threadNum;
		// 初始化threads数组
		threads = new DownThred[threadNum];
		this.path = path;
	}

	public MutilDownBar() {
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws MalformedURLException,
			IOException {
		// TODO Auto-generated method stub
		String surl = "http://static.ishare.down.sina.com.cn/5585234.txt?ssig=DZGCfrdpog&Expires=1347724800&KID=sina,ishare&ip=1347610652,117.40.138.&fn=%E5%8E%9A%E9%BB%91%E5%AD%A6.TXT";
		String spath = "D:\\a";
		int num = 3;
		final MutilDownBar mu = new MutilDownBar();
		mu.downLoad(spath, surl, num);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (mu.getCompleteRate() < 1) {
					// 每隔0.1秒查询一次任务的完成进度，
					System.out.println("已完成：" + mu.getCompleteRate());
					try {
						Thread.sleep(1000);
					} catch (Exception ex) {
					}
				}
			}
		}).start();
	}

	/**
	 * 
	 * @param path
	 *            保存的文件路径
	 * @param url
	 *            远程url
	 * @param threadNum
	 *            线程数量
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public void downLoad(String path, String url, int threadNum)
			throws MalformedURLException, IOException {
		this.path = path;
		this.url = url;
		this.threadNum = threadNum;
		this.threads = new DownThred[threadNum];
		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		conn.setConnectTimeout(2000);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		File file = new File(path + getFileExtName(url));// 保存的文件
		RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
		int fileSize = conn.getContentLength();
		accessFile.setLength(fileSize);
		accessFile.close();
		int block = (fileSize + threadNum - 1) / threadNum;// 每个线程负责下载的块大小
		for (int i = 0; i < threadNum; i++) {
			int startPos = i * block;
			RandomAccessFile tempAccessFile = new RandomAccessFile(file, "rwd");
			tempAccessFile.seek(startPos);
			threads[i] = new DownThred(startPos, block, tempAccessFile);
			threads[i].start();
		}
	}

	// 获取下载的完成百分比
	public double getCompleteRate() {
		// 统计多条线程已经下载的总大小
		int sumSize = 0;
		for (int i = 0; i < threadNum; i++) {
			sumSize += threads[i].length;
		}
		// 返回已经完成的百分比
		return sumSize * 1.0 / fileSize;
	}

	public String getFileExtName(String path) {
		return path.substring(path.lastIndexOf("."));
	}

	private class DownThred extends Thread {
		// 当前线程的开始的下载位置
		private int startPos;
		// 定义当前线程负责下载的文件大小
		private int currentPartSize;
		// 当前线程需要下载的文件块
		private RandomAccessFile currentPart;
		// 定义已经该线程已下载的字节数
		private int length;

		public DownThred(int startPos, int currentPartSize,
				RandomAccessFile currentPart) {
			super();
			this.startPos = startPos;
			this.currentPartSize = currentPartSize;
			this.currentPart = currentPart;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			try {
				URL u = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) u.openConnection();
				conn.setConnectTimeout(2000);
				conn.setRequestMethod("GET");
				inputStream = conn.getInputStream();
				// 跳过startPos个字节，表明该线程只下载自己负责哪部分文件。
				inputStream.skip(this.startPos);
				byte[] buffer = new byte[1024];
				int hasRead = 0;
				while ((length < currentPartSize)
						&& ((hasRead = inputStream.read()) != -1)) {
					currentPart.write(buffer, 0, hasRead);
					length += hasRead;
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					currentPart.close();
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
