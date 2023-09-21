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

public class CharacterEncodingFilter implements Filter {
	private String encoding;
	
	private boolean forceEncoding = false;
	
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	public void setForceEncoding(boolean forceEncoding) {
        this.forceEncoding = forceEncoding;
    }
	
	@Override
	public void destroy() {
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		this.encoding = config.getInitParameter("encoding");
		this.forceEncoding = Boolean.parseBoolean(config.getInitParameter("forceEncoding"));
	}
	
	public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		
		if(!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			throw new ServletException("CharacterEncodingFilter just supports HTTP requests");
		}
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
			
		doFilterInternal(httpRequest, httpResponse, filterChain);
	}
	
	protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		
		if(this.encoding != null && (this.forceEncoding || request.getCharacterEncoding() == null)) {
			request.setCharacterEncoding(this.encoding);
			if(this.forceEncoding) {
				response.setCharacterEncoding(this.encoding);
			}
		}
		filterChain.doFilter(request, response);
	}
}
