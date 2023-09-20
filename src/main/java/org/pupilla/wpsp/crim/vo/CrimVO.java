package org.pupilla.wpsp.crim.vo;

public class CrimVO {
	private String code,
				   lang;
	
	public CrimVO() {
	}
	
	public CrimVO(String code, String lang) {
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
