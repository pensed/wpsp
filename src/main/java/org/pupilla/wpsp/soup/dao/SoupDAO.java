package org.pupilla.wpsp.soup.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.pupilla.wpsp.soup.vo.SoupVO;

public class SoupDAO {
	private static Logger log = LogManager.getLogger(SoupDAO.class);
	
	public int allWork (SoupVO soupVo) throws IOException, KeyManagementException, NoSuchAlgorithmException {
		int result 	  = 2,
			i		  = 0,
			j		  = 0,
			idx		  = 1,
			page	  = 1;
		String code   = soupVo.getCode(),
			   lang   = soupVo.getLang(),
			   webapp = "D:\\works\\workspace\\workspace-eclipse-2023-06\\wpsp\\src\\main\\webapp",
			   path   = webapp + "\\ocr\\soup",
			   link   = "https://www.itfind.or.kr/data/media/iitpYouTube/ictGood/list.do?pageSize=12&pageIndex=";
		URL url;
		InputStream is;
		File file,
			 folder;
		FileOutputStream fos;
		Document doc;
		Elements elmts;
		Map<String, String> map = new HashMap<String, String>();
		
		SoupDAO.setSSL();
		
		while(true) {
			file = new File(path + "\\" + idx + "\\");
			if(file.exists()) {
				if(file.listFiles().length == 0) {
					break;
				}
				idx++;
			} else {
				file.mkdir();
				break;
			}
		}
		for(i=-1;i<page;i++) {

			url  = new URL(link + String.valueOf(i + 1));
			is   = url.openStream();
			file = new File(path + "\\" + idx + "\\" + page + ".html");
			fos  = new FileOutputStream(file);

			while((j = is.read()) != -1) {
				fos.write(j);
			}
			fos.close();
			log.info("Load... " + file.getPath());
			doc = Jsoup.parse(file, "utf-8");
			elmts = doc.getElementsByClass("picture");
			if(!elmts.isEmpty()) {
				for(int k=0;k<elmts.size();k++) {
					folder = new File(path + "\\" + idx + "\\" + page);
					if(!folder.exists()) {
						folder.mkdir();
						log.info("Make Directory \'" + page + "\'folder");
					}
					Element el = elmts.get(k);
					String src = el.select("img").get(0).attr("src");
					log.info(src);
					map.put("src", src);
					url = new URL(src);
					is = url.openStream();
					fos = new FileOutputStream(folder + "\\" + (k + 1) + ".jpg");
					while((j = is.read()) != -1) {
						fos.write(j);
					}
					fos.close();
					if(!folder.exists()) {
						folder.mkdirs();
					}
				}
				page++;
			} else {
				try {
					Path filePath = Paths.get(file.getPath());
					Files.deleteIfExists(filePath);
					log.info("Delete " + page + ".html");
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		result = 3;
		return result;
	}
	
	public static void setSSL() throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// TODO Auto-generated method stub
			}
			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// TODO Auto-generated method stub
			}
		} };
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}
}
