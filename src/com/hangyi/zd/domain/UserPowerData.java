package com.hangyi.zd.domain;

import java.util.ArrayList;
import java.util.List;

import com.eyunda.third.domain.BaseData;

public class UserPowerData extends BaseData {
	private static final long serialVersionUID = -1L;

	private List<UserPowerShipData> userPowerShipDatas = new ArrayList<UserPowerShipData>(); // 

	public List<UserPowerShipData> getUserPowerShipDatas() {
		return userPowerShipDatas;
	}

	public void setUserPowerShipDatas(List<UserPowerShipData> userPowerShipDatas) {
		this.userPowerShipDatas = userPowerShipDatas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
