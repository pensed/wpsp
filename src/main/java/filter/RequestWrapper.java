package filter;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

//import org.apache.commons.text.StringEscapeUtils;

public class RequestWrapper  extends HttpServletRequestWrapper {
    
    public RequestWrapper(HttpServletRequest servletRequest) {         
        super(servletRequest);     
    }     
        
    public String[] getParameterValues(String parameter) {
        
        String[] values = super.getParameterValues(parameter);       
          
        if (values==null)  {                  
            return null;          
        }      
          
        int count = values.length;      
        String[] encodedValues = new String[count];      
          
        for (int i = 0; i < count; i++) {                
            encodedValues[i] = stripXSS(values[i]);        
        }       
        return encodedValues;    
    }     
         
    public String getParameter(String parameter) {           
    
        String value = super.getParameter(parameter);           
          
        if (value == null) {                  
        return null;                   
        }           
         
        return stripXSS(value); 
    }     
        
    public String getHeader(String name) {         
    
        String value = super.getHeader(name);         
          
        if (value == null){             
            return null;         
        }
          
        return stripXSS(value);
    }     

     public static String stripXSS(String value) {

          if (value != null) {
              // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
              // avoid encoded attacks.
              // value = ESAPI.encoder().canonicalize(value);
              // Avoid null characters
              value = value.replaceAll("", "");
              // Avoid anything between script tags
              Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
              value = scriptPattern.matcher(value).replaceAll("");
              // Avoid anything in a src='...' type of expression
              scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
              value = scriptPattern.matcher(value).replaceAll("");
   
              scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
              value = scriptPattern.matcher(value).replaceAll("");
              
              // Remove any lonesome <img ...> tag - 220104 보안취약점 점검 검출대상 조치
              scriptPattern = Pattern.compile("<img(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
              value = scriptPattern.matcher(value).replaceAll("");
              
              // Remove any lonesome </script> tag
              scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
              value = scriptPattern.matcher(value).replaceAll("");
   
              // Remove any lonesome <script ...> tag
              scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
              value = scriptPattern.matcher(value).replaceAll("");
   
              // Avoid eval(...) expressions
              scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
              value = scriptPattern.matcher(value).replaceAll("");
   
              // Avoid expression(...) expressions
              scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
              value = scriptPattern.matcher(value).replaceAll("");

              // Avoid javascript:... expressions
              scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
              value = scriptPattern.matcher(value).replaceAll("");
   
              // Avoid vbscript:... expressions
              scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
              value = scriptPattern.matcher(value).replaceAll("");
   
              // Avoid onload= expressions
              scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
              value = scriptPattern.matcher(value).replaceAll("");

              // HTML transformation characters
              //value = org.springframework.web.util.HtmlUtils.htmlEscape(value);
              //value = StringEscapeUtils.escapeHtml(value);    //이건 한글 깨짐..  
              value = com.google.common.html.HtmlEscapers.htmlEscaper().escape(value); //SPRING을 못 쓰니까 일단...
              // SQL injection characters
              //value = StringEscapeUtils.escapeSql(value);		//db사용 안함
          }
          return value;
     }
}
