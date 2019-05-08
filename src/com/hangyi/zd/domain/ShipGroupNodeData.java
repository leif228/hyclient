package com.hangyi.zd.domain;

import java.util.ArrayList;
import java.util.List;

import com.eyunda.third.domain.BaseData;

public class ShipGroupNodeData extends BaseData {
	private static final long serialVersionUID = 1L;
	private NodeCode status = NodeCode.arrivedLoadingDock; // 状态
	private String shipID = "";
	private String shipName = "";
	private String opTime = "";
	private String weather = "";
	
	private List<MapPortData> handlerDatas = new ArrayList<MapPortData>(); //报警信息

	public String getOpTime() {
		return opTime;
	}

	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
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

	public List<MapPortData> getHandlerDatas() {
		return handlerDatas;
	}

	public void setHandlerDatas(List<MapPortData> handlerDatas) {
		this.handlerDatas = handlerDatas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
