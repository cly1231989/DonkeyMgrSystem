package com.hose.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtil {
	public static boolean makeThumbImgage(MultipartFile image, String imageFilePath, String thumbFilePath){
		long beginTime = System.currentTimeMillis();   
		try {
			File imageFile = new File(imageFilePath);
			image.transferTo(imageFile);
			
			System.out.println("**********************************time userd: " + Long.toString(System.currentTimeMillis()-beginTime));
			String suffix = null;              
            if(imageFile.getName().indexOf(".") > -1) {
                suffix = imageFile.getName().substring(imageFile.getName().lastIndexOf(".") + 1);
            }
            
            Image img = ImageIO.read(imageFile);
            int width = img.getWidth(null);
            int height = img.getHeight(null);
            
            double ratio = 1.0;
			int height1 = 0;
			int width1 = 0;
			if(height >= width){
				height1 = 150;
				ratio = height * 1.0 / 150;
				width1 = (int) (width * 1.0 / ratio);
			}else
			{
				width1 = 150;
				ratio = width * 1.0 / 150;
				height1 = (int) (height * 1.0 / ratio);
			}
            
            BufferedImage bi = new BufferedImage(width1, height1, BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.getGraphics();
            g.drawImage(img, 0, 0, width1, height1, Color.LIGHT_GRAY, null);
            g.dispose();
            String p = imageFile.getPath();
            // 将图片保存在原目录并加上前缀
            ImageIO.write(bi, suffix, new File(thumbFilePath));
            
            System.out.println("###################################time userd: " + Long.toString(System.currentTimeMillis()-beginTime));
		} catch (IOException e) {
			return false;
		}		
		
		System.out.println("----------------------------------total time: " + Long.toString(System.currentTimeMillis()-beginTime));
		return true;
	}
}