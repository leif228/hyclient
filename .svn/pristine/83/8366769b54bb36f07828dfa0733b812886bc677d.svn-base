package com.eyunda.third.domain.ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eyunda.third.domain.BaseData;



public class ShipDynamicData extends BaseData {
	private static final long serialVersionUID = -1L;

	private ShipData shipData = null;
	private ShipApplyData myShipApplyData = null;

	@SuppressWarnings("unchecked")
	public ShipDynamicData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {


			this.shipData = new ShipData((Map<String, Object>) params.get("shipData"));
			this.myShipApplyData = new ShipApplyData((Map<String, Object>) params.get("myShipApplyData"));


		}
	}
			public ShipData getShipData() {
				return shipData;
			}



			public void setShipData(ShipData shipData) {
				this.shipData = shipData;
			}


			public ShipApplyData getMyShipApplyData() {
				return myShipApplyData;
			}
			
			public void setMyShipApplyData(ShipApplyData myShipApplyData) {
				this.myShipApplyData = myShipApplyData;
			}
			public static long getSerialversionuid() {
				return serialVersionUID;
			}

		}
