package com.eyunda.third.domain;

import java.util.Map;


public class UpdateInfoData extends BaseData {
	private static final long serialVersionUID = -1L;

	private String version=""; // 当前版本
	private String url=""; // 升级地址
	private String  note = ""; // 升级说明

	public UpdateInfoData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.version = (String) params.get("version");
			this.url = (String) params.get("url");
			this.note = (String) params.get("note");
		}
	}


	public String getVersion() {
		return version;
	}



	public void setVersion(String version) {
		this.version = version;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public String getNote() {
		return note;
	}



	public void setNote(String note) {
		this.note = note;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
