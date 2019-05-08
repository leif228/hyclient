package com.eyunda.third.domain.ship;

import java.util.Map;

import com.eyunda.third.domain.BaseData;


public class ArvlftShipData extends BaseData {
	private static final long serialVersionUID = -1L;

	private ShipData shipData = null;
	private ShipArvlftData arriveData = null;
	private ShipArvlftData leftData = null;

	public ShipData getShipData() {
		return shipData;
	}
	
	@SuppressWarnings("unchecked")
	public ArvlftShipData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
            this.shipData = new ShipData((Map<String, Object>) params.get("shipData"));
            this.arriveData = new ShipArvlftData((Map<String, Object>) params.get("arriveData"));
            this.leftData = new ShipArvlftData((Map<String, Object>) params.get("leftData"));
		}
	}
	public void setShipData(ShipData shipData) {
		this.shipData = shipData;
	}

	public ShipArvlftData getArriveData() {
		return arriveData;
	}

	public void setArriveData(ShipArvlftData arriveData) {
		this.arriveData = arriveData;
	}

	public ShipArvlftData getLeftData() {
		return leftData;
	}

	public void setLeftData(ShipArvlftData leftData) {
		this.leftData = leftData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
