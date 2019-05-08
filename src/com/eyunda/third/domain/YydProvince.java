package com.eyunda.third.domain;

import java.util.Map;

public class YydProvince  {
	private static final long serialVersionUID = -1L;

	private String provinceNo = ""; // 省份编码
	private String provinceName = ""; // 省份名称
	private Double latitude = 0.00D; // 纬度
	private Double longitude = 0.00D; // 经度
	private Long id = 0L;
	
	public YydProvince(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.provinceNo = (String) params.get("provinceNo");
			this.provinceName = (String) params.get("provinceName");
		}
	}

	public YydProvince() {
		super();
	}
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * 取得省份编码
	 */
	public String getProvinceNo() {
		return provinceNo;
	}

	/**
	 * 设置省份编码
	 */
	public void setProvinceNo(String provinceNo) {
		this.provinceNo = provinceNo;
	}

	/**
	 * 取得省份名称
	 */
	public String getProvinceName() {
		return provinceName;
	}

	/**
	 * 设置省份名称
	 */
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
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
