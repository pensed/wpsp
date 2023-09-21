package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 20150908
 */

//기능 사용 안함

public class MobileViewCheckFilter implements Filter {

    public String cookieName = "viewpcmode";
    public String redirectUrl = "모바일버전 url";

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request; 

        boolean pcviewCookie = false;
        if(cookieName != null && !"".equals(cookieName) && req.getCookies() != null && req.getCookies().length > 0){
            for(Cookie cook : req.getCookies()){
                if(cookieName.equals(cook.getName())){
                    if("true".equalsIgnoreCase(cook.getValue())){
                        pcviewCookie = true;
                    }
                    break;
                }
            }
        }
        if(pcviewCookie != true){
            UAgentInfo info = new UAgentInfo(req);
            if(info.detectIphone() || info.detectTierSmartphones() || info.detectTierOtherPhones() || info.detectAndroid()){
                System.out.println("------- Mobile rediect -------");
                HttpServletResponse res = (HttpServletResponse) response; 
                res.sendRedirect(this.redirectUrl);
                
                return;
            }else{
                System.out.println("------- PC browser -------");
                chain.doFilter(request, response);
            }
        }else{
            System.out.println("------- PC browser view Chk -------");
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.cookieName = filterConfig.getInitParameter("cookieName");
        this.redirectUrl = filterConfig.getInitParameter("redirectUrl");
    }
}
