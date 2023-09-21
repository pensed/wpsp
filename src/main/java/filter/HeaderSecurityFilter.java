package filter;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.net.HttpHeaders;

public class HeaderSecurityFilter implements Filter{
   
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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {        
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Protection against Type 1 Reflected XSS attacks
        res.addHeader("X-XSS-Protection", "1; mode=block");
        
        // Disabling browsers to perform risky mime sniffing
        res.addHeader("X-Content-Type-Options", "nosniff");        
        
        res.addHeader("Content-Security-Policy", "default-src 'self' www.google-analytics.com; script-src 'self' 'unsafe-inline' 'unsafe-eval' www.googletagmanager.com www.google-analytics.com ssl.daumcdn.net t1.daumcdn.net stats.g.doubleclick.net; style-src 'self' 'unsafe-inline' 'unsafe-eval' t1.daumcdn.net; frame-ancestors 'self'; img-src * ");

        res.addHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
        
        chain.doFilter(req, res);
        
        addSameSite(res , "Strict");
    }     
    
    private void addSameSite(HttpServletResponse response, String sameSite) {
    	Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
    	boolean firstHeader = true;
    	for (String header : headers) {
            if (firstHeader) {
                response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; Secure; %s", header, "SameSite=" + sameSite));
                firstHeader = false;
                continue;
            }
            response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s; Secure; %s", header, "SameSite=" + sameSite));
    	}
    }
     
    
}

