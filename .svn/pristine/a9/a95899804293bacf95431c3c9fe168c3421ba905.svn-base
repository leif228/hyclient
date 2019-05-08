package com.hangyi.zd.domain;

import java.util.ArrayList;
import java.util.List;

import com.eyunda.third.domain.BaseData;

public class HcNodeData extends BaseData {
	private static final long serialVersionUID = -1L;
	private NodeCode status = NodeCode.arrivedLoadingDock; // 状态
	private String shipID = "";
	private String shipName = "";
	private String opTime = "";
	private BaiduWeatherData weather = null;
	
	private List<HcNodeChildData> handlerDatas = new ArrayList<HcNodeChildData>(); //报警信息

	public String getOpTime() {
		return opTime;
	}

	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}

	public BaiduWeatherData getWeather() {
		return weather;
	}

	public void setWeather(BaiduWeatherData weather) {
		this.weather = weather;
	}

	public String getShipID() {
		return shipID;
	}

	public void setShipID(String shipID) {
		this.shipID = shipID;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public NodeCode getStatus() {
		return status;
	}

	public void setStatus(NodeCode status) {
		this.status = status;
	}

	public List<HcNodeChildData> getHandlerDatas() {
		return handlerDatas;
	}

	public void setHandlerDatas(List<HcNodeChildData> handlerDatas) {
		this.handlerDatas = handlerDatas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
