package kr.pupil.wpsp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class tess {
	// TODO tess4j를 이용한 OCR프로그램
	public static void main(String[] args) throws TesseractException, IOException {
		File imgFile = new File("C:\\Users\\USER\\Desktop\\downloads\\New_sample\\4_train\\kor.맑은고딕.exp6.tiff");
		BufferedImage img = ImageIO.read(imgFile);
		Tesseract inst = new Tesseract();
		String result = "";
		
		inst.setLanguage("kor+eng");
		inst.setDatapath("C:\\Users\\USER\\Desktop\\downloads\\tessdata");
		inst.setOcrEngineMode(1);
		inst.setPageSegMode(6);
		//inst.setVariable(result, result);
		result = inst.doOCR(img);
		
		System.out.println("result: " + result);
	}

}
