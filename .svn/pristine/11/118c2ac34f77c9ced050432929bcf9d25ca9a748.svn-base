package com.hangyi.zd.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShipVoyageNodeData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nid = "";
	private String voyageNum = ""; // �V2016081601
	private String shipID = ""; // ����id
	private String shipName = ""; // ����
	private String stage = ""; // ʱ��
	private String value = ""; // ģ在节点3和节点7分别表示装载吨和卸载吨，其他节点无意义������
	private String opTime = ""; // ģ"2016-08-16 19:34:53"���

	private BaiduWeatherData weather = null; // "﻿广州市,雷阵雨,微风,31 ~ 25℃", ģ������
	private NodeCode nodeCode = null;

	public ShipVoyageNodeData() {
	}

	@SuppressWarnings("unchecked")
	public ShipVoyageNodeData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.nid = (String) params.get("nid");
			this.voyageNum = (String) params.get("voyageNum");
			this.shipID = (String) params.get("shipID");
			this.shipName = (String) params.get("shipName");
			this.stage = (String) params.get("stage");
			this.value = (String) params.get("value");
			this.opTime = (String) params.get("opTime");
			if (true) {
				Map<String, Object> map = (Map<String, Object>) params.get("weather");
				if (map != null && !map.isEmpty()){
					BaiduWeatherData data = new BaiduWeatherData(map);
					this.weather = data;
				}
			}
		}
	}

	public NodeCode getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(NodeCode nodeCode) {
		this.nodeCode = nodeCode;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getVoyageNum() {
		return voyageNum;
	}

	public void setVoyageNum(String voyageNum) {
		this.voyageNum = voyageNum;
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

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public BaiduWeatherData getWeather() {
		return weather;
	}

	public void setWeather(BaiduWeatherData weather) {
		this.weather = weather;
	}

	public String getOpTime() {
		return opTime;
	}

	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}

}
