package kr.pupil.tesstrain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class mkbox {
    public static void main(String[] args) throws IOException, InterruptedException {

    	String[] cmd = new String[1300];
    	//cmd[0] = "cmd.exe /c cd C:\\Users\\USER\\Desktop\\downloads\\New_sample\\4_train";
    	for(int i=0;i<1300;i++) {
    		cmd[i] = "tesseract --oem 1 --psm 7 kor.맑은고딕.exp"+(i+1)+".tiff kor.맑은고딕.exp"+(i+1)+" -l kor batch.nochop makebox";
    	}
    	 try {
             BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\USER\\Desktop\\downloads\\New_sample\\tscmd.txt"));

             for (String s : cmd) {
                 writer.write(s);
                 writer.newLine(); // 줄 바꿈
             }

             writer.close(); // writer 닫기
         } catch (IOException e) {
             e.printStackTrace();
         }
    	
    }
}