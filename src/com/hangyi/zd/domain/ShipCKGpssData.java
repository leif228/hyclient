package com.hangyi.zd.domain;

public class ShipCKGpssData {

	private String nID = ""; //
	private String Route_id = ""; //
	private String SN = ""; //
	private String Latitude = ""; //
	private String Longitude = ""; //

	public ShipCKGpssData() {
	}

	public String getnID() {
		return nID;
	}

	public void setnID(String nID) {
		this.nID = nID;
	}

	public String getRoute_id() {
		return Route_id;
	}

	public void setRoute_id(String route_id) {
		Route_id = route_id;
	}

	public String getSN() {
		return SN;
	}

	public void setSN(String sN) {
		SN = sN;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

}
