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

public class tessbl {
	// TODO 이미지를 흑백으로 이진화한 후 tess4j를 사용
	public static void main(String[] args) throws TesseractException, IOException {
		
		final Logger log  = LogManager.getLogger(tessbl.class);
		File imgFile 	  = new File("C:\\Users\\USER\\Desktop\\downloads\\2.jpg");
		BufferedImage img = ImageIO.read(imgFile);
		Tesseract inst 	  = new Tesseract();
		String result 	  = "";
		
		for(int y = 0; y < img.getHeight(); y++) {
			   for(int x = 0; x < img.getWidth(); x++) {
                   int r = (0x00ff0000 & img.getRGB(x, y)) >> 16,
                   	   g = (0x0000ff00 & img.getRGB(x, y)) >> 8,
                   	   b = (0x000000ff & img.getRGB(x, y)), 
                   	   m = (r+g+b);
			       if(m>=383) {
			    	   img.setRGB(x,y,Color.WHITE.getRGB());
			       } else {
			    	   img.setRGB(x,y,0);
			       }
			   }
			}
		ImageIO.write(img, "png", new File("C:\\Users\\USER\\Desktop\\downloads\\binary.png"));
		imgFile = new File("C:\\Users\\USER\\Desktop\\downloads\\binary.png");
		
		inst.setLanguage("kor");
		inst.setDatapath("C:\\Users\\USER\\Desktop\\downloads\\tessdata");
		result = inst.doOCR(imgFile);
		log.info("result: " + result);
	}

}