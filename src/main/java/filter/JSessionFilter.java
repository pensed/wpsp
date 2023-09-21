package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class JSessionFilter implements Filter {
	
	//TODO 로그인하는게 없어서 사용 X
	
	public FilterConfig filterConfig;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
	
	@Override
	public void destroy() {
		this.filterConfig = null;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
		throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		
		String sQueryString = "";
		
		if(httpRequest.getQueryString() != null && httpRequest.getQueryString().length() > 0) {
			sQueryString = "?" + httpRequest.getQueryString();
		}
		
		if(session.isNew()) {
			httpResponse.sendRedirect(httpResponse.encodeRedirectURL(httpRequest.getRequestURI() + sQueryString));
			return;
		} else if (session.getAttribute("verified") == null) {
			session.setAttribute("verified", true);
			if(httpRequest.isRequestedSessionIdFromCookie()) {
				httpResponse.sendRedirect(httpRequest.getRequestURI().split(";")[0] + sQueryString);
				return;
			}
		}
		
		filterChain.doFilter(request, response);
	}
}
