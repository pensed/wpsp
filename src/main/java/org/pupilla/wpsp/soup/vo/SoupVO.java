package org.pupilla.wpsp.soup.vo;

public class SoupVO {
	private String code,
				   lang;
	
	public SoupVO() {
	}
	
	public SoupVO(String code, String lang) {
		this.code = code;
		this.lang = lang;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public String getLang() {
		return lang;
	}
	
	public void setLang(String lang) {
		this.lang = lang;
	}
	
}
