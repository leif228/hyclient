package com.hangyi.zd.domain;

import com.eyunda.third.domain.BaseData;

public class HcNodeChildData extends BaseData {
	private static final long serialVersionUID = -1L;
	private String police = "";
	private BaiduWeatherData weather = null;

	public String getPolice() {
		return police;
	}

	public void setPolice(String police) {
		this.police = police;
	}

	public BaiduWeatherData getWeather() {
		return weather;
	}

	public void setWeather(BaiduWeatherData weather) {
		this.weather = weather;
	}

}
