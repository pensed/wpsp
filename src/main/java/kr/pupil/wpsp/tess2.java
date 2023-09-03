package kr.pupil.wpsp;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class tess2 {
	// TODO 이미지내 grayscale을 이용하여 이미지를 편집 후 추출하는 텍스트의 좌표가 있는 box파일을 생성, tess4j로 ocr진행
	public static void main(String[] args) throws TesseractException, IOException {

		String path   = "C:\\Users\\USER\\Desktop\\downloads\\";			//목표 이미지 저장소
		String tdpath = "C:\\Users\\USER\\Desktop\\downloads\\tessdata";	//lang.traindata 저장소
		String lang   = "kor";												//인식 언어
		String flnm   = lang+".굴림.exp0";									//파일명
		String rslt   = "";													//ocr 추출 결과값
		
		File imgFile = new File(path + flnm + ".jpg");
		BufferedImage img = ImageIO.read(imgFile);
		Tesseract inst = new Tesseract();
		
		//이미지 grayscale 작업
		for(int y = 0; y < img.getHeight(); y++) {
			   for(int x = 0; x < img.getWidth(); x++) {
			       Color clr = new Color(img.getRGB(x, y));
			       int rgb 	 = (int) (0.2126 * clr.getRed() 
			    		   			+ 0.7152 * clr.getGreen() 
			    		   			+ 0.0722 * clr.getBlue());
			       img.setRGB(x, y, new Color(rgb, rgb, rgb).getRGB());
			   }
			}
		
		//grayscale한 이미지 저장 경로설정 및 불러오기
		path 	+= "data\\";
		ImageIO.write(img, "tif", new File(path + flnm + ".tif"));
		imgFile  = new File(path + flnm + ".tif");
		img 	 = ImageIO.read(imgFile);
		
		//이미지 box 생성
		try {
		    String command = "tesseract --oem 1 --psm 6 " + path + flnm + ".tif " + path + flnm + " -l " + lang + " batch.nochop makebox";
		    Process p = Runtime.getRuntime().exec(command);
		    p.waitFor();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		//tesseract 설정및 ocr실행
		inst.setLanguage(lang);
		inst.setDatapath(tdpath);
		inst.setOcrEngineMode(1);
		inst.setPageSegMode(6);
		rslt = inst.doOCR(img);
		
		System.out.println("result: " + rslt);
	}

}
