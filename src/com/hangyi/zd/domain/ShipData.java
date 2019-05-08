package com.hangyi.zd.domain;

import java.io.Serializable;
import java.util.Map;

public class ShipData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ShipID = "";
	private String ShipName = "";
	
	public ShipData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.ShipID = (String) params.get("ShipID");
			this.ShipName = (String) params.get("ShipName");
		}
	}

	public String getShipID() {
		return ShipID;
	}

	public void setShipID(String shipID) {
		ShipID = shipID;
	}

	public String getShipName() {
		return ShipName;
	}

	public void setShipName(String shipName) {
		ShipName = shipName;
	}

}
