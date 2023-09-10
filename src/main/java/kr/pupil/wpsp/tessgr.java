package kr.pupil.wpsp;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class tessgr {
	// TODO 이미지를 흑백으로 그레이스케일한 후 tess4j를 사용
	public static void main(String[] args) throws TesseractException, IOException {
		final Logger log  = LogManager.getLogger(tessgr.class);
		File imgFile 	  = new File("C:\\Users\\USER\\Desktop\\downloads\\4.jpg");
		BufferedImage img = ImageIO.read(imgFile);
		Tesseract inst 	  = new Tesseract();
		String result 	  = "";
		
		for(int y=0;y<img.getHeight();y++) {
			   for(int x=0;x<img.getWidth();x++) {
			       Color clr = new Color(img.getRGB(x, y));
			       int Y 	 = (int) (0.2126 * clr.getRed() 
			    		   			+ 0.7152 * clr.getGreen() 
			    		   			+ 0.0722 * clr.getBlue());
			       img.setRGB(x, y, new Color(Y, Y, Y).getRGB());
			   }
			}
		ImageIO.write(img, "png", new File("C:\\Users\\USER\\Desktop\\downloads\\grayscale.png"));
		imgFile 				= new File("C:\\Users\\USER\\Desktop\\downloads\\grayscale.png");
		
		inst.setLanguage("eng");
		inst.setDatapath("C:\\Users\\USER\\Desktop\\downloads\\tessdata");
		result = inst.doOCR(imgFile);
		log.info("result: " + result);
	}

}
