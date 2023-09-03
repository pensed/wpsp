package kr.pupil.wpsp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class crim {
	// TODO JSOUP을 이용한 이미지 크롤러
    public static void main( String[] args ) throws IOException
    {
    	int i, j, page = 0;
      //int code = 0;
      //String link = "https://주소.la/galleries/" + code + ".html";
    	String link = "https://www.itfind.or.kr/data/media/iitpYouTube/ictGood/list.do?pageSize=12&pageIndex=";
        URL url;
        InputStream is;
        File file;
        FileOutputStream fos;
        Document doc;
        Elements elems;
        Map<String, String> map = new HashMap<String, String>();
        
        for (i=-1;i<page;i++) {
        	System.out.println(page);
        	url = new URL(link+String.valueOf(page));
        	is = url.openStream();
        	file = new File("C:\\Users\\USER\\Desktop\\downloads\\text"+page+".html");
        	fos = new FileOutputStream(file);
        	while ((j = is.read()) != -1) {
                fos.write(j);
            }
            fos.close();
        	doc = Jsoup.parse(file, "utf-8");
        	elems = doc.getElementsByClass("picture");
        	if(!elems.isEmpty()) {
        	for(int k=0;k<elems.size();k++) {
        		Element el = elems.get(k);
        		String src= el.select("img").get(0).attr("src");
        		System.out.println(src);
        		map.put("src", src);
        		File folder = new File("C:\\Users\\USER\\Desktop\\downloads\\"+(page+1));
        		url = new URL(src);
        		is = url.openStream();
        		fos = new FileOutputStream("C:\\Users\\USER\\Desktop\\downloads\\"+(page+1)+"\\"+k+"/.jpg");
        		if(!folder.exists()) {
        			folder.mkdirs();
        		}
        	}
        	page++;
        	} else
        		break;
        }
    }
}
