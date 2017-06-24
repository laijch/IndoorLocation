package com.indoorloc.model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MyImageIO {
	public BufferedImage image;
	
	public void myRead(String filepath) {
		File file = new File(filepath);
        try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void myWrite(Image img, String filepath) {
		try{
			File imgFile = new File(filepath);
			BufferedImage buffer = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D graph = buffer.createGraphics(); 
			graph.drawImage(img, 0, 0, null);
			graph.dispose();
			ImageIO.write(buffer, "png", imgFile);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
