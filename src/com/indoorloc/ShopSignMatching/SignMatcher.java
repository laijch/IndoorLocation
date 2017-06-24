package com.indoorloc.ShopSignMatching;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.indoorloc.model.Point;

/**
 * @author Mo Haoran on 2017.06.24
 * @function 招牌匹配模块，用在后台服务器分割子图之后，利用模型得到店铺坐标
 *
 */
public class SignMatcher {
	public BufferedImage image;
	
	/**
	 * @param filename 当输入图像为路径时
	 */
	public SignMatcher(String filename) throws IOException {
        File file = new File(filename);
        image = ImageIO.read(file);
    }
	
	/**
	 * @param fileImg 当输入图像为BufferedImage时
	 */
	public SignMatcher(BufferedImage fileImg) throws IOException {
		int imgW = fileImg.getWidth();
		int imgH = fileImg.getHeight();
		image = new BufferedImage(imgW, imgH, BufferedImage.TYPE_4BYTE_ABGR);

	    for (int i = 0; i < imgH; i++) {
			for (int j = 0; j < imgW; j++) {
				int argb = image.getRGB(j, i);
				image.setRGB(j, i, argb);
			}
		}
    }
	
	/**
	 * 招牌分类匹配、获取店铺坐标主函数
	 * @return
	 */
	public Point getShopPosition_MainProcess() {
		Point shopPosition = new Point(-1, -1);
		
		//将图片输入模型获取店铺坐标
		
		return shopPosition;
	}
}
