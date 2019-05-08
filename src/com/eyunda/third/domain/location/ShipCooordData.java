package com.eyunda.third.domain.location;

import java.util.Map;

import com.eyunda.third.domain.BaseData;



public class ShipCooordData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID

	private String shipName = ""; // MMSI编号
	private String mmsi = ""; // MMSI编号
	private String posTime = ""; // 时间
	private Double latitude = 0.00D; // 百度经度
	private Double longitude = 0.00D; // 百度纬度
	private Double speed = 0.00D; // 速度
	private Double course = 0.00D; // 航向
	private Double lng = 0.00D; // 经度
	private Double lat = 0.00D; // 纬度
	
	private String detailState = ""; // 
	private String shipID = ""; // 
	private String gpsTime = ""; // 
	private String gpsLongitude = ""; // 
	private String gpsLatitude = ""; //
	private String gpsSpeed = ""; // 
	private String gpsCourse = ""; // 
	private String mainVoltage = ""; // 
	private String backUpVoltage = ""; // 
	private String temperature = ""; // 
	private String powerManagementState = ""; // 
	private String bdLongitude = "";
	private String bdLatitude = "";
	
	
	
	public ShipCooordData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.mmsi = (String)params.get("mmsi");
			this.shipName = (String)params.get("shipName");
			this.longitude = ((Double)params.get("longitude"));
			this.latitude = ((Double)params.get("latitude"));
			this.posTime = (String)params.get("posTime");
			this.speed = (Double)params.get("speed");
			this.course = (Double)params.get("course");
			this.lng =  (Double)params.get("lng");
			this.lat =  (Double)params.get("lat");
		}
	}

	public String getBdLongitude() {
		return bdLongitude;
	}

	public void setBdLongitude(String bdLongitude) {
		this.bdLongitude = bdLongitude;
	}

	public String getBdLatitude() {
		return bdLatitude;
	}

	public void setBdLatitude(String bdLatitude) {
		this.bdLatitude = bdLatitude;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public ShipCooordData() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMmsi() {
		return mmsi;
	}

	public void setMmsi(String mmsi) {
		this.mmsi = mmsi;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public String getPosTime() {
		return posTime;
	}

	public void setPosTime(String posTime) {
		this.posTime = posTime;
	}


	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Double getCourse() {
		return course;
	}

	public void setCourse(Double course) {
		this.course = course;
	}

	public String getDetailState() {
		return detailState;
	}

	public void setDetailState(String detailState) {
		this.detailState = detailState;
	}

	public String getShipID() {
		return shipID;
	}

	public void setShipID(String shipID) {
		this.shipID = shipID;
	}

	

	public String getGpsTime() {
		return gpsTime;
	}

	public void setGpsTime(String gpsTime) {
		this.gpsTime = gpsTime;
	}

	public String getGpsLongitude() {
		return gpsLongitude;
	}

	public void setGpsLongitude(String gpsLongitude) {
		this.gpsLongitude = gpsLongitude;
	}

	public String getGpsLatitude() {
		return gpsLatitude;
	}

	public void setGpsLatitude(String gpsLatitude) {
		this.gpsLatitude = gpsLatitude;
	}

	public String getGpsSpeed() {
		return gpsSpeed;
	}

	public void setGpsSpeed(String gpsSpeed) {
		this.gpsSpeed = gpsSpeed;
	}

	public String getGpsCourse() {
		return gpsCourse;
	}

	public void setGpsCourse(String gpsCourse) {
		this.gpsCourse = gpsCourse;
	}

	public String getMainVoltage() {
		return mainVoltage;
	}

	public void setMainVoltage(String mainVoltage) {
		this.mainVoltage = mainVoltage;
	}

	public String getBackUpVoltage() {
		return backUpVoltage;
	}

	public void setBackUpVoltage(String backUpVoltage) {
		this.backUpVoltage = backUpVoltage;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getPowerManagementState() {
		return powerManagementState;
	}

	public void setPowerManagementState(String powerManagementState) {
		this.powerManagementState = powerManagementState;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
