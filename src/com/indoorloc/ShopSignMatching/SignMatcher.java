package com.indoorloc.ShopSignMatching;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.indoorloc.model.Point;

/**
 * @author Mo Haoran on 2017.06.24
 * @function ����ƥ��ģ�飬���ں�̨�������ָ���ͼ֮������ģ�͵õ���������
 *
 */
public class SignMatcher {
	public BufferedImage image;
	
	/**
	 * @param filename ������ͼ��Ϊ·��ʱ
	 */
	public SignMatcher(String filename) throws IOException {
        File file = new File(filename);
        image = ImageIO.read(file);
    }
	
	/**
	 * @param fileImg ������ͼ��ΪBufferedImageʱ
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
	 * ���Ʒ���ƥ�䡢��ȡ��������������
	 * @return
	 */
	public Point getShopPosition_MainProcess() {
		Point shopPosition = new Point(-1, -1);
		
		//��ͼƬ����ģ�ͻ�ȡ��������
		
		return shopPosition;
	}
}
