package demo.net.threadDown;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.*;

/**
 *  多线程 远程下载 带进度条
 */
public class DownUtil {
	// 定义下载资源的路径
	private String path;
	// 指定所下载的文件的保存位置
	private String targetFile;
	// 定义需要使用多少线程下载资源
	private int threadNum;
	// 定义下载的线程对象
	private DownThread[] threads;
	// 定义下载的文件的总大小
	private int fileSize;

	public DownUtil(String path, String targetFile, int threadNum) {
		this.path = path;
		this.threadNum = threadNum;
		// 初始化threads数组
		threads = new DownThread[threadNum];
		this.targetFile = targetFile;
	}

	public static void main(String[] args) throws Exception {
		// 初始化DownUtil对象
		String strPath = "http://117.34.0.47/2/ishare.down.sina.com.cn/18409101.zip?ssig=RPmHfTGQpX&Expires=1347724800&KID=sina,ishare&ip=1347612375,117.40.138.&fn=mysql-connector-java-5.1.17%281%29.zip";
		final DownUtil downUtil = new DownUtil(strPath, "D:\\aa"
				+ getFileExtName(strPath), 4);
		// 开始下载
		downUtil.download();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (downUtil.getCompleteRate() < 1) {
					// 每隔0.1秒查询一次任务的完成进度
					System.out.println("已完成：" + downUtil.getCompleteRate());
					try {
						Thread.sleep(1000);
					} catch (Exception ex) {
					}
				}
			}
		}).start();
	}

	// 后缀名
	public static String getFileExtName(String path) {
		return path.substring(path.lastIndexOf("."));
	}

	public void download() throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		conn.setRequestProperty(
				"Accept",
				"image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
						+ "application/x-shockwave-flash, application/xaml+xml, "
						+ "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
						+ "application/x-ms-application, application/vnd.ms-excel, "
						+ "application/vnd.ms-powerpoint, application/msword, */*");
		conn.setRequestProperty("Accept-Language", "zh-CN");
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Connection", "Keep-Alive");
		// 得到文件大小
		fileSize = conn.getContentLength();
		conn.disconnect();
		int currentPartSize = fileSize / threadNum + 1;
		RandomAccessFile file = new RandomAccessFile(targetFile, "rw");
		// 设置本地文件的大小
		file.setLength(fileSize);
		file.close();
		for (int i = 0; i < threadNum; i++) {
			// 计算每条线程的下载的开始位置
			int startPos = i * currentPartSize;
			// 每个线程使用一个RandomAccessFile进行下载
			RandomAccessFile currentPart = new RandomAccessFile(targetFile,
					"rw");
			// 定位该线程的下载位置
			currentPart.seek(startPos);
			// 创建下载线程
			threads[i] = new DownThread(startPos, currentPartSize, currentPart);
			// 启动下载线程
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

	private class DownThread extends Thread {
		// 当前线程的下载位置
		private int startPos;
		// 定义当前线程负责下载的文件大小
		private int currentPartSize;
		// 当前线程需要下载的文件块
		private RandomAccessFile currentPart;
		// 定义已经该线程已下载的字节数
		public int length;

		public DownThread(int startPos, int currentPartSize,
				RandomAccessFile currentPart) {
			this.startPos = startPos;
			this.currentPartSize = currentPartSize;
			this.currentPart = currentPart;
		}

		@Override
		public void run() {
			try {
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5 * 1000);
				conn.setRequestMethod("GET");
				conn.setRequestProperty(
						"Accept",
						"image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
								+ "application/x-shockwave-flash, application/xaml+xml, "
								+ "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
								+ "application/x-ms-application, application/vnd.ms-excel, "
								+ "application/vnd.ms-powerpoint, application/msword, */*");
				conn.setRequestProperty("Accept-Language", "zh-CN");
				conn.setRequestProperty("Charset", "UTF-8");
				InputStream inStream = conn.getInputStream();
				// 跳过startPos个字节，表明该线程只下载自己负责哪部分文件。
				inStream.skip(this.startPos);
				byte[] buffer = new byte[1024];
				int hasRead = 0;
				// 读取网络数据，并写入本地文件
				while (length < currentPartSize
						&& (hasRead = inStream.read(buffer)) != -1) {
					currentPart.write(buffer, 0, hasRead);
					// 累计该线程下载的总大小
					length += hasRead;
				}
				currentPart.close();
				inStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
