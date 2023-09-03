package kr.pupil.tesstrain;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class injectjson {
	// TODO json파일에서 id와 text데이터를 추출하여 txt파일을 작성후 저장
	public static void main(String[] args) {
		
		JSONParser parser = new JSONParser();
		
		String path = "C:\\Users\\USER\\Desktop\\downloads\\New_sample";
		File json = new File(path+"\\handwriting_data_info_clean.json");
		File img;
		
		Map<String, String> map = new HashMap<String, String>();
		int mxflnm = 104298;	//마지막 이미지 파일명.
		int cnt = 0;
		
		try {
			Object obj = parser.parse(new FileReader(json));
			JSONArray arr = (JSONArray)obj;
			
			for(int i=0;i<arr.size();i++) {
				JSONObject jsonObj = (JSONObject)arr.get(i);
				map.put((String) jsonObj.get("id"), (String) jsonObj.get("text"));
			}
			
			for(int i=19;i<mxflnm;i++) {
				String filenm = String.format("%08d", i);
				img = new File(path+"\\1_sentence\\"+filenm+".png");
				if(img.exists()) {
					BufferedWriter fw = new BufferedWriter(new FileWriter(path+"\\2_box\\"+filenm+".box"));
					BufferedImage bfimg = ImageIO.read(img);
					ImageIO.write(bfimg, "TIFF",new File(path+"\\3_tiff\\"+filenm+".tiff"));
					fw.write(map.get(filenm));
					fw.flush();
					fw.close();
					cnt++;
				}
			}
			System.out.println("Count: " + cnt + "개 성공");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
