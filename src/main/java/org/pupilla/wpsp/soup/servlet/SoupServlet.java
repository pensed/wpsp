package org.pupilla.wpsp.soup.servlet;

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
import org.pupilla.wpsp.soup.dao.SoupDAO;
import org.pupilla.wpsp.soup.vo.SoupVO;


public class SoupServlet extends HttpServlet {
	private static Logger log = LogManager.getLogger(SoupServlet.class);
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
		SoupVO vo = new SoupVO();
		vo.setCode(code);
		vo.setLang(lang);
		log.info("[code: " + code + "][lang: " + lang + "]");
		
		SoupDAO dao = new SoupDAO();
		
		try {
			switch(dao.allWork(vo)) {
				case 1:
					result = "작업 완료";
					break;
				case 2:
					result = "이미지 다운로드 실패";
					break;
				case 3:
					result = "이미지 다운로드 성공";
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
		out.println("<script>alert('" + result + "'); location.href='http://localhost:8080/view/soup.jsp';</script>");
        out.flush();
	}
}
