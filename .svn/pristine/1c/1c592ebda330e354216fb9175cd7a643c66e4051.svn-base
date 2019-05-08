package com.eyunda.third.adapters.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.ship.ShipNameData;

public class UserChildManager {
	private static UserChildManager instance = null;
	private Map<Long, UserData> childMaps = new HashMap<Long, UserData>();
	List<UserData> childList = new ArrayList<UserData>();
	private List<ShipNameData> shipList = new ArrayList<ShipNameData>();

	public static synchronized UserChildManager getInstance() {
		if (instance == null)
			instance = new UserChildManager();
		return instance;
	}


	public List<ShipNameData> getShipList() {
		return shipList;
	}


	public void setShipList(List<ShipNameData> shipList) {
		this.shipList = shipList;
	}


	public Map<Long, UserData> getChildMaps() {
		return childMaps;
	}


	public void setChildMaps(Map<Long, UserData> childMaps) {
		this.childMaps = childMaps;
	}


	public void addChild(Long childId, UserData userData) {
		childMaps.put(childId, userData);
	}
	public void addChild(ShipNameData shipData) {
		shipList.add(shipData);
	}

	public void delChild(Long childId) {
		Iterator<Entry<Long, UserData>> it = childMaps.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Long, UserData> entry = it.next();
			Long key = entry.getKey();
			if (key.equals(childId)) {
				it.remove();
			}
		}
	}


	public List<UserData> getChilds() {
		childList.clear();

		Iterator<Entry<Long, UserData>> it = childMaps.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Long, UserData> entry = it.next();
			UserData key = entry.getValue();
			childList.add(key);
		}
	
		return childList;
	}
	
	public Long getChildId(Long userId){
		Iterator<Entry<Long, UserData>> it = childMaps.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Long, UserData> entry = it.next();
			UserData key = entry.getValue();
			if(userId.equals(key.getId()))
				return entry.getKey();
		}
	
		return null;
	}
}
