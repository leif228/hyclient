package com.hangyi.zd.domain;

import java.util.ArrayList;
import java.util.List;

public class StaEndTimeImgData {

	private String ShipID = ""; //
	private String StartTime = ""; //
	private String EndTime = ""; //
	private String Channel = ""; //
	private List<TimeSrcData> Content = new ArrayList<TimeSrcData>(); // ��ǰ���Ȩ�޾�ͷ
	private List<String> Picture = new ArrayList<String>(); // ��ǰ���Ȩ�޾�ͷ

	public String getShipID() {
		return ShipID;
	}

	public void setShipID(String shipID) {
		ShipID = shipID;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public String getChannel() {
		return Channel;
	}

	public void setChannel(String channel) {
		Channel = channel;
	}

	public List<TimeSrcData> getContent() {
		return Content;
	}

	public void setContent(List<TimeSrcData> content) {
		Content = content;
	}

	public List<String> getPicture() {
		return Picture;
	}

	public void setPicture(List<String> picture) {
		Picture = picture;
	}

}
