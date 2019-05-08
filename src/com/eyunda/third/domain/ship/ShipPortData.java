package com.eyunda.third.domain.ship;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;
import com.eyunda.third.domain.order.PortData;

public class ShipPortData extends BaseData {
	private static final long serialVersionUID = -1L;

	private PortCityCode portCity = null;
	private PortData cargoPortData = null;

	public ShipPortData() {
		super();
	}

	@SuppressWarnings("unchecked")
	public ShipPortData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.cargoPortData = new PortData((Map<String, Object>) params.get("cargoPortData"));

			String tmpPortCity = (String) params.get("portCity");
			if ((tmpPortCity != null) && (!tmpPortCity.equals(""))) {
				this.portCity = PortCityCode.valueOf(tmpPortCity);
			}
		}
	}

	public String getCityName() {
		if (portCity != null)
			return portCity.getDescription();
		else if (cargoPortData != null)
			return cargoPortData.getFullName();
		else
			return "";
	}

	public PortCityCode getPortCity() {
		return portCity;
	}

	public void setPortCity(PortCityCode portCity) {
		this.portCity = portCity;
	}

	public PortData getCargoPortData() {
		return cargoPortData;
	}

	public void setCargoPortData(PortData cargoPortData) {
		this.cargoPortData = cargoPortData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
