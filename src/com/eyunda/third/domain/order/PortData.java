package com.eyunda.third.domain.order;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;

public class PortData extends BaseData {
	private static final long serialVersionUID = -1L;

	private PortCityCode portCity = null;
	private String portNo = ""; // 港口编码
	private String portName = ""; // 港口名称
	private String fullName = ""; // 港口全名

	public PortData(Map<String, Object> params) {
		super();
		if(params != null && !params.isEmpty()){
			this.portCity = PortCityCode.valueOf((String) params.get("portCity"));
			this.portNo = (String) params.get("portNo");
			this.portName = (String) params.get("portName");
			this.fullName = (String) params.get("fullName");
		}
	}

	public PortData() {
		super();
	}

	public String getFullName() {
		if (portCity == null)
			fullName = "";
		else if ("".equals(portName))
			fullName = "";
		else
			fullName = portCity.getDescription() + "." + portName;

		return fullName;
	}

	public PortCityCode getPortCity() {
		return portCity;
	}

	public void setPortCity(PortCityCode portCity) {
		this.portCity = portCity;
	}

	public String getPortNo() {
		return portNo;
	}

	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
