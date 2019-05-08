package com.eyunda.third.domain;

import java.util.Map;



public class YydCity {
	private static final long serialVersionUID = -1L;

	private String cityNo = ""; // 地市编码
	private String cityName = ""; // 地市名称
	private Double latitude = 0.00D; // 纬度
	private Double longitude = 0.00D; // 经度
	private Long id = 0L;
	
	public YydCity(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.cityNo = (String) params.get("cityNo");
			this.cityName = (String) params.get("cityName");
		}
	}

	public YydCity() {
		super();
	}
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 取得地市编码
	 */
	public String getCityNo() {
		return cityNo;
	}

	/**
	 * 设置地市编码
	 */
	public void setCityNo(String cityNo) {
		this.cityNo = cityNo;
	}

	/**
	 * 取得地市名称
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * 设置地市名称
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
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
