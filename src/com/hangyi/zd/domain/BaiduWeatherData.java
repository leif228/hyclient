package com.hangyi.zd.domain;

import java.io.Serializable;
import java.util.Map;


public class BaiduWeatherData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String aPI = ""; // "night/yin.png"
	private String city = ""; // �"广州市"
	private String weather = ""; // ���"小雨转阴"
	private String wind = ""; // ����"北风3-4级"
	private String temperature = ""; // ʱ��"16 ~ 13℃"
	
	public BaiduWeatherData() {
	}

	public BaiduWeatherData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.aPI = (String) params.get("aPI");
			this.city = (String) params.get("city");
			this.weather = (String) params.get("weather");
			this.wind = (String) params.get("wind");
			this.temperature = (String) params.get("temperature");
		}
	}

	public String getaPI() {
		return aPI;
	}

	public void setaPI(String aPI) {
		this.aPI = aPI;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

}
