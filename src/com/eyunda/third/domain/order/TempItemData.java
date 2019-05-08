package com.eyunda.third.domain.order;

import java.util.Map;

import com.eyunda.third.domain.BaseData;

public class TempItemData extends BaseData {
	private static final long serialVersionUID = 1L;

	private Long id = 0L;
	private Long tempId = 0L; // 模板ID
	private Integer no = 0; // 序号
	private String content = ""; // 内容

	public TempItemData(Map<String, Object> params) {
		super();
		if(params != null && !params.isEmpty()){
			this.id = ((Double) params.get("id")).longValue();
			this.tempId = ((Double) params.get("tempId")).longValue();
			this.no = ((Double) params.get("no")).intValue();
			this.content = (String) params.get("content");
		}
	}

	public TempItemData() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTempId() {
		return tempId;
	}

	public void setTempId(Long tempId) {
		this.tempId = tempId;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
