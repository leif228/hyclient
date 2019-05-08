package com.hangyi.zd.domain;

import java.util.ArrayList;
import java.util.List;

import com.eyunda.third.domain.BaseData;

public class UserPowerShipData extends BaseData {
	private static final long serialVersionUID = -1L;
	private String shipID = "";
	private String shipName = ""; 
	private List<ShipModelData> shipModels = new ArrayList<ShipModelData>(); //授权的船舶模块
	
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

	public List<ShipModelData> getShipModels() {
		return shipModels;
	}

	public void setShipModels(List<ShipModelData> shipModels) {
		this.shipModels = shipModels;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
