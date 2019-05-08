package com.eyunda.third.domain.ship;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.ReleaseStatusCode;
import com.eyunda.third.domain.order.PortData;


public class ShipCabinData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID
	private ShipData shipData = null; // 船舶
	private UserData shiper = null; // 船员,发布者
	private Long shiperId = 0L;
	private String arrivePortTime = ""; // 预计到港时间
	private String portNo = ""; // 到达港港口号
	private PortData portData = null; // 到达港
	private String remark = ""; // 接货信息
	private String createTime = ""; // 发布时间
	private ReleaseStatusCode status = ReleaseStatusCode.publish; // 状态
	
	@SuppressWarnings("unchecked")
	public ShipCabinData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.shipData = new ShipData((Map<String, Object>) params.get("shipData"));
			this.shiper = new UserData((Map<String, Object>) params.get("shiper"));
			this.portData = new PortData((Map<String, Object>) params.get("portData"));
			this.shiperId = ((Double) params.get("shiperId")).longValue();
			this.remark = (String) params.get("remark");
			this.portNo = (String) params.get("portNo");
			this.createTime = (String) params.get("createTime");
			this.arrivePortTime = (String) params.get("arrivePortTime");
			String status = (String) params.get("status");
			if ((status != null) && (!status.equals(""))) {
				this.status = ReleaseStatusCode.valueOf(status);
			}

		}
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ShipData getShipData() {
		return shipData;
	}
	public void setShipData(ShipData shipData) {
		this.shipData = shipData;
	}
	public UserData getShiper() {
		return shiper;
	}
	public void setShiper(UserData shiper) {
		this.shiper = shiper;
	}
	public String getArrivePortTime() {
		return arrivePortTime;
	}
	public void setArrivePortTime(String arrivePortTime) {
		this.arrivePortTime = arrivePortTime;
	}
	public String getPortNo() {
		return portNo;
	}
	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public ReleaseStatusCode getStatus() {
		return status;
	}
	public void setStatus(ReleaseStatusCode status) {
		this.status = status;
	}
	public Long getShiperId() {
		return shiperId;
	}
	public void setShiperId(Long shiperId) {
		this.shiperId = shiperId;
	}
	public PortData getPortData() {
		return portData;
	}
	public void setPortData(PortData portData) {
		this.portData = portData;
	}

}