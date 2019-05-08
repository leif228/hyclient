package com.hangyi.zd.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eyunda.third.domain.ship.AttrNameData;

public class ShipVoyageData implements Serializable{
	private static final long serialVersionUID = 1L;
	private String nid = "";
	private String shipID = ""; // ����
	private String shipName = ""; // ����
	private String voyageNum = ""; // �V2016081601
	private String startPort = ""; // ģ������
	private String endPort = ""; // ģ��
	private String totalTime = ""; // ģ��
	private String totalDistance = ""; // ģ��
	private List<ShipVoyageNodeData> nodes = new ArrayList<ShipVoyageNodeData>();
	
	public ShipVoyageData() {
	}

	@SuppressWarnings("unchecked")
	public ShipVoyageData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.nid = (String) params.get("nid");
			this.shipID = (String) params.get("shipID");
			this.shipName = (String) params.get("shipName");
			this.voyageNum = (String) params.get("voyageNum");
			this.startPort = (String) params.get("startPort");
			this.endPort = (String) params.get("endPort");
			this.totalTime = (String) params.get("totalTime");
			this.totalDistance = (String) params.get("totalDistance");
			if (true) {
				List<ShipVoyageNodeData> datas = new ArrayList<ShipVoyageNodeData>();
				List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("nodes");
				if (list != null && !list.isEmpty())
					for (Map<String, Object> map : list) {
						ShipVoyageNodeData data = new ShipVoyageNodeData(map);
						datas.add(data);
					}
				this.nodes = datas;
			}
		}
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
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

	public String getVoyageNum() {
		return voyageNum;
	}

	public void setVoyageNum(String voyageNum) {
		this.voyageNum = voyageNum;
	}

	public String getStartPort() {
		return startPort;
	}

	public void setStartPort(String startPort) {
		this.startPort = startPort;
	}

	public String getEndPort() {
		return endPort;
	}

	public void setEndPort(String endPort) {
		this.endPort = endPort;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public String getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(String totalDistance) {
		this.totalDistance = totalDistance;
	}

	public List<ShipVoyageNodeData> getNodes() {
		return nodes;
	}

	public void setNodes(List<ShipVoyageNodeData> nodes) {
		this.nodes = nodes;
	}

}
