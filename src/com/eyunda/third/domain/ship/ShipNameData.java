package com.eyunda.third.domain.ship;

import java.util.Map;

import com.eyunda.third.domain.BaseData;

public class ShipNameData extends BaseData {
	private static final long serialVersionUID = -1L;

	private String mmsi = ""; // MMSI编号
	private String shipName = ""; // 船舶名称
	private Long id = 0L; // ID
	private Long carrierId = 0L; // 承运人
	

	public ShipNameData() {
		super();
	}

	public ShipNameData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.carrierId = ((Double) params.get("carrierId")).longValue();
			this.mmsi = (String) params.get("mmsi");
			this.shipName = (String) params.get("shipName");
			
		}
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Long carrierId) {
		this.carrierId = carrierId;
	}
	@Override
	public String toString() {
		return shipName;
	}

}
