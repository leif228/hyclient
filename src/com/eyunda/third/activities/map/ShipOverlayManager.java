package com.eyunda.third.activities.map;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;

public class ShipOverlayManager extends OverlayManager {
    private List<OverlayOptions> optionsList = new ArrayList<OverlayOptions>();

    public ShipOverlayManager(BaiduMap baiduMap) {
        super(baiduMap);
    }

    @Override
    public List<OverlayOptions> getOverlayOptions() {
        return optionsList;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }

    public void setData(List<OverlayOptions> optionsList) {
        this.optionsList = optionsList;
    }

	@Override
	public boolean onPolylineClick(Polyline arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
