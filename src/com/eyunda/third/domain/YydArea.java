package com.eyunda.third.domain;

import java.util.Map;

public class YydArea {
	private static final long serialVersionUID = -1L;

	private String areaNo = ""; // 区县编码
	private String areaName = ""; // 区县名称
	private Double latitude = 0.00D; // 纬度
	private Double longitude = 0.00D; // 经度
	private Long id = 0L;
	
	public YydArea(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.areaNo = (String) params.get("areaNo");
			this.areaName = (String) params.get("areaName");
		}
	}

	public YydArea() {
		super();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 取得区县编码
	 */
	public String getAreaNo() {
		return areaNo;
	}

	/**
	 * 设置区县编码
	 */
	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	/**
	 * 取得区县名称
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * 设置区县名称
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
