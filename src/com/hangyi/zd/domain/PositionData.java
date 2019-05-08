package com.hangyi.zd.domain;

public class PositionData {

	private String PortName = "";
	private Double Longitude = 0.0D; //
	private Double Latitude = 0.0D; //

	private Double palng1 = 0.0D; // 港区经度1
	private Double palat1 = 0.0D; // 港区纬度1

	private Double palng2 = 0.0D; // 港区经度2
	private Double palat2 = 0.0D; // 港区纬度2

	public String getPortName() {
		return PortName;
	}

	public void setPortName(String portName) {
		PortName = portName;
	}

	public Double getLongitude() {
		return Longitude;
	}

	public void setLongitude(Double longitude) {
		Longitude = longitude;
	}

	public Double getLatitude() {
		return Latitude;
	}

	public void setLatitude(Double latitude) {
		Latitude = latitude;
	}

	public Double getPalng1() {
		return palng1;
	}

	public void setPalng1(Double palng1) {
		this.palng1 = palng1;
	}

	public Double getPalat1() {
		return palat1;
	}

	public void setPalat1(Double palat1) {
		this.palat1 = palat1;
	}

	public Double getPalng2() {
		return palng2;
	}

	public void setPalng2(Double palng2) {
		this.palng2 = palng2;
	}

	public Double getPalat2() {
		return palat2;
	}

	public void setPalat2(Double palat2) {
		this.palat2 = palat2;
	}

}
