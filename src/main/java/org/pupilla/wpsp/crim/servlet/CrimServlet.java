package org.pupilla.wpsp.crim.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pupilla.wpsp.crim.dao.CrimDAO;
import org.pupilla.wpsp.crim.vo.CrimVO;


public class CrimServlet extends HttpServlet {
	private static Logger log = LogManager.getLogger(CrimServlet.class);
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		doHandle(request, response);
	}
	
	private void doHandle(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String 		code   = request.getParameter("code"),
					lang   = request.getParameter("lang"),
			   		result = "알 수 없는 오류";
		CrimVO vo = new CrimVO();
		vo.setCode(code);
		vo.setLang(lang);
		log.info("[code: " + code + "][lang: " + lang + "]");
		
		CrimDAO dao = new CrimDAO();
		
		try {
			switch(dao.allWork(vo)) {
				case 1:
					result = "작업 완료";
					break;
				case 2:
					result = "이미지 다운로드 실패";
					break;
				case 3:
					result = "이미지 텍스트 추출 실패";
					break;
				case 4:
					result = "텍스트 번역 실패";
					break;
				case 5:
					result = "텍스트 삽입 실패";
					break;
				case 6:
					result = "완성본 저장 실패";
					break;
				default:
					break;
			}
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
		out.println("<script>alert('" + result + "'); location.href='http://localhost:8080/view/crim.jsp';</script>");
        out.flush();
	}
}
