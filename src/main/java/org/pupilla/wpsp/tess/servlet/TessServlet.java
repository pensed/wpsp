package org.pupilla.wpsp.tess.servlet;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sourceforge.tess4j.Tesseract;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;

public class TessServlet extends HttpServlet {
	
	private static Logger log = LogManager.getLogger(TessServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doHandle(request,response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doHandle(request,response);
	}
	
	private void doHandle(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		Tesseract inst 	 = new Tesseract();
		File file, txt;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload;
		List<File> files = new ArrayList<>();
		String  webapp   = "D:\\works\\workspace\\workspace-eclipse-2023-06\\wpsp\\src\\main\\webapp",
				saveRoot = webapp + "\\ocr\\tess",
				lang 	 = "kor",
				rslt 	 = "";
		
		int page = 1,
			size = 1048576000;
		
		while(true) {
			file = new File(saveRoot + "\\" + page + "\\");
			if(file.exists()) {
				if(file.listFiles().length == 0) {
					break;
				}
				page++;
			} else {
				
				file.mkdir();
				break;
			}
		}
		log.info("Save Directory: " + file.getPath());
		
		factory.setRepository(file);
		factory.setSizeThreshold(size);
		upload = new ServletFileUpload(factory);
		int flnm = 1;
		try {
			List<FileItem> items = upload.parseRequest(request);
			for(FileItem fi:items) {
				if(fi.isFormField()) {
					log.info(fi.getFieldName() + " = " + fi.getString("utf-8"));
				} else {
					String og = fi.getName(),
						   ext = og.substring(og.lastIndexOf(".")),
						   name = flnm + ext;
					log.info("File Name: " + name);
					if(!file.exists()) {
						file.mkdirs();
					}
					fi.write(new File(file, flnm + ".png"));
					
					BufferedImage img = ImageIO.read(new File(file + "\\" + flnm + ".png"));
					
					for(int y=0;y<img.getHeight();y++) {
						   for(int x=0;x<img.getWidth();x++) {
						       Color clr = new Color(img.getRGB(x, y));
						       int Y 	 = (int) (0.2126 * clr.getRed() 
						    		   			+ 0.7152 * clr.getGreen() 
						    		   			+ 0.0722 * clr.getBlue());
						       img.setRGB(x, y, new Color(Y, Y, Y).getRGB());
						   }
						}
					ImageIO.write(img, "tif", new File(file + "\\" + flnm + ".tif"));
					
					Path path = Paths.get(file + "\\" + flnm + ".png");
					Files.delete(path);
					
					String command = "tesseract --oem 1 --psm 6 " + file + "\\" + flnm + ".tif " + file + "\\" + flnm + " -l " + lang + " batch.nochop makebox";
					Process p = Runtime.getRuntime().exec(command);
					p.waitFor();
					
					inst.setLanguage(lang);
					inst.setDatapath(webapp + "\\ocr\\tessdata");
					inst.setOcrEngineMode(1);
					inst.setPageSegMode(6);
					
					rslt = inst.doOCR(img);
					txt = new File(file + "\\" + flnm + ".txt");
					FileWriter fw = new FileWriter(txt, true);
					fw.write(rslt);
					fw.flush();
					fw.close();
					files.add(txt);
					flnm++;
				}
			}
			File zipFile = new File(file + "\\ocr.zip");
			byte[] buf = new byte[4096];
			try (ZipOutputStream output = new ZipOutputStream(new FileOutputStream(zipFile))) {
				for(File fl : files) {
					try (FileInputStream in = new FileInputStream(fl)) {
						ZipEntry ze = new ZipEntry(fl.getName());
						output.putNextEntry(ze);
						int len;
						while ((len = in.read(buf))>0) {
							output.write(buf,0,len);
						}
						output.closeEntry();
					}
				}
				ServletOutputStream out = response.getOutputStream();
				FileInputStream in = new FileInputStream(zipFile);
				byte[] buffer = new byte[1024*1024*1];
				while(true) {
					int count = in.read(buffer);
					if(count == -1)
						break;
					out.write(buffer, 0, count);
				}
				in.close();
				out.println("<button onclick=\"location.href='/ocr/tess/"+flnm+"/ocr.zip'\"download>download</button>"
						  + "<button onclick=\"location.href='/ocr/tess/"+flnm+"/ocr.zip'\"download>download</button>");
				out.flush();
				//out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
