package com.hangyi.zd.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupData implements Serializable{
	private static final long serialVersionUID = 1L;
	private String GroupID = "";
	private String GroupName = "";
	private String GroupType = "";//0表示客户，1表示船公司
	private String tempx = "";
	private String tempy = "";
	private boolean Flag = false;
	private boolean isPort = false;

	private List<ShipData> shipDatas = new ArrayList<ShipData>();
	public GroupData(){}
	
	@SuppressWarnings("unchecked")
	public GroupData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.GroupID = (String) params.get("GroupID");
			this.GroupName = (String) params.get("GroupName");
			this.GroupType = (String) params.get("GroupType");
			this.Flag = (Boolean) params.get("Flag");
			if (true) {
				List<ShipData> datas = new ArrayList<ShipData>();
				List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("shipDatas");
				if (list != null && !list.isEmpty())
					for (Map<String, Object> map : list) {
						ShipData data = new ShipData(map);
						datas.add(data);
					}
				this.shipDatas = datas;
			}
		}
	}

	public boolean isPort() {
		return isPort;
	}

	public void setPort(boolean isPort) {
		this.isPort = isPort;
	}

	public String getGroupType() {
		return GroupType;
	}

	public void setGroupType(String groupType) {
		GroupType = groupType;
	}

	public String getTempx() {
		return tempx;
	}

	public void setTempx(String tempx) {
		this.tempx = tempx;
	}

	public String getTempy() {
		return tempy;
	}

	public void setTempy(String tempy) {
		this.tempy = tempy;
	}

	public String getGroupID() {
		return GroupID;
	}

	public void setGroupID(String groupID) {
		GroupID = groupID;
	}

	public String getGroupName() {
		return GroupName;
	}

	public void setGroupName(String groupName) {
		GroupName = groupName;
	}

	public boolean getFlag() {
		return Flag;
	}

	public void setFlag(boolean flag) {
		Flag = flag;
	}

	public List<ShipData> getShipDatas() {
		return shipDatas;
	}

	public void setShipDatas(List<ShipData> shipDatas) {
		this.shipDatas = shipDatas;
	}
	
}
