package demo.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 图片文字合成工具测试类
 * @author fule
 *
 */
public class ImageWUtil {

	private static Graphics2D getGraphics(BufferedImage bi) {
		Graphics2D g = bi.createGraphics();
		// 字体平滑
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		return g;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File bgFile = new File("D:/bgg.jpg");
		BufferedImage bi  = null;
		try {
			bi = ImageIO.read(bgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int imgWidth = bi.getWidth();
		int ImgHeight = bi.getHeight();
		Graphics2D g = getGraphics(bi);
		Font font = new Font("微软雅黑", Font.PLAIN, 40);// 字体
		g.setFont(font);
		
	}

}
