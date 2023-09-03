package kr.pupil.wpsp;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class mkpdf {
	// TODO 이미지들을 PDF로 합쳐주는 프로그램
	public static void main(String[] args) throws IOException {
		
		String imgPath = "C:\\Users\\USER\\Desktop\\downloads\\";
		String pdfPath = "C:\\Users\\USER\\Desktop\\downloads\\pdf\\";
		
		if(!pdfPath.endsWith(".pdf")) {
			System.out.println("확장자명이 .pdf여야 합니다.");
			System.exit(1);
		}
		
		PDDocument doc = new PDDocument();
		
		try {
			File imgDir = new File(imgPath);
			File[] imgFiles = imgDir.listFiles();
			
			for(int i=1;i<imgFiles.length;i++) {
				PDPage page = new PDPage();
				doc.addPage(page);
				
				PDImageXObject pdImg = PDImageXObject.createFromFile(imgPath+i+".jpg", doc);
				PDPageContentStream conts = new PDPageContentStream(doc, page);
				
				conts.drawImage(pdImg, 0, 0, 2100, 2970);
				
				conts.close();
				doc.save(pdfPath);
				System.out.println(i + "  ");
				if(i%50 == 0) System.out.println("");
			}
		} finally {
			doc.close();
			System.out.println("");
			System.out.println("끝");
		}
	}
}
