package com.indoorloc.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageDivitor {
	public BufferedImage image;
	Point minPos;
	Point maxPos;
	
	public ImageDivitor(String filename, Point _minPos, Point _maxPos) throws IOException {
        File file = new File(filename);
        image = ImageIO.read(file);
        
        minPos = _minPos;
        maxPos = _maxPos;
    }
	
	public BufferedImage getShopSignImage() {
	    int signWidth = maxPos.x - minPos.x;
	    int signHeight = maxPos.y - minPos.y;
	    BufferedImage shopSignImage = new BufferedImage(signWidth, signHeight, BufferedImage.TYPE_4BYTE_ABGR);

	    for (int i = 0; i < signHeight; i++) {
			for (int j = 0; j < signWidth; j++) {
				int oriH = minPos.y + i;
				int oriW = minPos.x + j;
				
				int argb = image.getRGB(oriW, oriH);
				int a = (argb >> 24) & 0xFF;
				int r = (argb >> 16) & 0xFF;          // R
				int g = (argb >> 8) & 0xFF;           // G
				int b = (argb >> 0) & 0xFF;           // B
				
				int new_argb = ((a << 24) & 0xFF000000) |
								((r << 16) & 0x00FF0000) |
								((g << 8) & 0x0000FF00) |
								((b << 0) & 0x000000FF);
				shopSignImage.setRGB(j, i, new_argb);
			}
		}
	    
        return shopSignImage;
	}
}
