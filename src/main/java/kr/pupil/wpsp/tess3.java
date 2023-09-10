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

public class tess3 {
	// TODO 이미지내 grayscale을 이용하여 이미지를 편집 후 추출하는 텍스트의 좌표가 있는 box파일을 생성, tess4j로 ocr진행
	// 개선사항:	   이미지를 배열로 가져와 파일명이 최근작업폴더명+1인 폴더를 생성(중간에 빈폴더가 있으면 거기서 작업)하여 
	//			   해당 폴더에 그레이스케일한 tif와 box파일을
	//			   방식으로 개선
	// 차후계획:	   box 영역을 blur filter을 사용하여

	public static void main(String[] args) throws TesseractException, IOException {
		
		final Logger log = LogManager.getLogger(tess3.class);
		
		String path   = "C:\\Users\\USER\\Desktop\\downloads\\",			//목표 이미지 저장소
			   tdpath = path + "tessdata",									//lang.traindata 저장소
			   lang   = "kor",												//인식 언어
			   flnm   = lang+".굴림.exp0",									//파일명
			   rslt   = "";													//ocr 추출 결과값
		
		File dir;
		File[] mkdir;
		int n = 1;
		
		while(true) {
			dir = new File(path + n + "\\");
			if(dir.exists()) {
				mkdir = dir.listFiles();
				if(mkdir.length == 0) {
					break;
				}
			} else {
				try {
					dir.mkdir();
					log.info("Make Directory \'" + dir.getName() + "\'folder");
				} catch(Exception e) {
					e.getStackTrace();
				}
				break;
			}
			n++;
		}
		
		log.info("Job Path => " + dir.getAbsolutePath());
		
		File imgFile 	  = new File(path + flnm + ".jpg");
		BufferedImage img = ImageIO.read(imgFile);					//log4j2로 이미지 못읽을 시 에러 검출 하도록 만들기
		Tesseract inst 	  = new Tesseract();
		
		//이미지 grayscale 작업
		for(int y=0;y<img.getHeight();y++) {
			for(int x=0;x<img.getWidth();x++) {
				Color clr = new Color(img.getRGB(x, y));
				int   rgb = (int) (0.2126 * clr.getRed() 
					      		 + 0.7152 * clr.getGreen() 
					      		 + 0.0722 * clr.getBlue());
				img.setRGB(x, y, new Color(rgb, rgb, rgb).getRGB());
			}
		}
		
		//grayscale한 이미지 저장 경로설정 및 불러오기
		path 	 = dir.getAbsolutePath() + "\\";
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
		
		log.info("result: " + rslt);
		
	}
	
}
