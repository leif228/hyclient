package com.eyunda.third.domain.ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.ArvlftCode;
import com.eyunda.third.domain.order.PortData;

public class ShipStopData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID

	private String mmsi = ""; // MMSI编号
	private String createTime = ""; // 上报时间
	private String arvlftTime = ""; // 到离时间
	private ArvlftCode arvlft = null; // 到离状态

	private String portNo = ""; // 到离港口
	private PortData portData = null;
	private String goPortNo = ""; // 将去港口
	private PortData goPortData = null;

	private Double distance = 0D; // 到港Data中存放航行里程km
	private Double jobDistance = 0D;// 离港Data中存放港作里程km
	private String sailLineData = ""; // 航次航行坐标数据文件

	private String remark = ""; // 备注
	private String arvlftDesc = "未上报"; // 船舶动态

	private String startTime = "";// 肮行开始时间
	private String endTime = "";// 肮行结束时间
	private String jobStartTime = "";// 港作开始时间
	private String jobEndTime = "";// 港作结束时间

	private Integer hours = 0;
	private Integer minutes = 0;
	private Integer jobHours = 0;
	private Integer jobMinutes = 0;

	private List<ShipUpdownData> shipUpdownDatas = null; // 装卸货物列表

	public ShipStopData() {
		super();
	}

	@SuppressWarnings("unchecked")
	public ShipStopData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.mmsi = (String) params.get("mmsi");
			this.createTime = (String) params.get("createTime");
			this.arvlftTime = (String) params.get("arvlftTime");

			String tmpArvlft = (String) params.get("arvlft");
			if ((tmpArvlft != null) && (!tmpArvlft.equals(""))) {
				this.arvlft = ArvlftCode.valueOf(tmpArvlft);
			}

			this.portNo = (String) params.get("portNo");
			this.portData = new PortData((Map<String, Object>) params.get("portData"));
			this.goPortNo = (String) params.get("goPortNo");
			this.goPortData = new PortData((Map<String, Object>) params.get("goPortData"));

			this.distance = (Double) params.get("distance");
			this.jobDistance = (Double) params.get("jobDistance");
			this.sailLineData = (String) params.get("sailLineData");

			this.remark = (String) params.get("remark");
			this.arvlftDesc = (String) params.get("arvlftDesc");

			this.startTime = (String) params.get("startTime");
			this.endTime = (String) params.get("endTime");
			this.jobStartTime = (String) params.get("jobStartTime");
			this.jobEndTime = (String) params.get("jobEndTime");

			this.hours = ((Double) params.get("hours")).intValue();
			this.minutes = ((Double) params.get("minutes")).intValue();
			this.jobHours = ((Double) params.get("jobHours")).intValue();
			this.jobMinutes = ((Double) params.get("jobMinutes")).intValue();

			if (true) {
				List<ShipUpdownData> datas = new ArrayList<ShipUpdownData>();
				List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("shipUpdownDatas");
				if (list != null && !list.isEmpty())
					for (Map<String, Object> map : list) {
						ShipUpdownData data = new ShipUpdownData(map);
						datas.add(data);
					}
				this.shipUpdownDatas = datas;
			}

		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMmsi() {
		return mmsi;
	}

	public void setMmsi(String mmsi) {
		this.mmsi = mmsi;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getArvlftTime() {
		return arvlftTime;
	}

	public void setArvlftTime(String arvlftTime) {
		this.arvlftTime = arvlftTime;
	}

	public ArvlftCode getArvlft() {
		return arvlft;
	}

	public void setArvlft(ArvlftCode arvlft) {
		this.arvlft = arvlft;
	}

	public String getPortNo() {
		return portNo;
	}

	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}

	public PortData getPortData() {
		return portData;
	}

	public void setPortData(PortData portData) {
		this.portData = portData;
	}

	public String getGoPortNo() {
		return goPortNo;
	}

	public void setGoPortNo(String goPortNo) {
		this.goPortNo = goPortNo;
	}

	public PortData getGoPortData() {
		return goPortData;
	}

	public void setGoPortData(PortData goPortData) {
		this.goPortData = goPortData;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String getSailLineData() {
		return sailLineData;
	}

	public void setSailLineData(String sailLineData) {
		this.sailLineData = sailLineData;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getArvlftDesc() {
		return arvlftDesc;
	}

	public void setArvlftDesc(String arvlftDesc) {
		this.arvlftDesc = arvlftDesc;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getHours() {
		return hours;
	}

	public void setHours(Integer hours) {
		this.hours = hours;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	public List<ShipUpdownData> getShipUpdownDatas() {
		return shipUpdownDatas;
	}

	public void setShipUpdownDatas(List<ShipUpdownData> shipUpdownDatas) {
		this.shipUpdownDatas = shipUpdownDatas;
	}

	public Double getJobDistance() {
		return jobDistance;
	}

	public void setJobDistance(Double jobDistance) {
		this.jobDistance = jobDistance;
	}

	public String getJobStartTime() {
		return jobStartTime;
	}

	public void setJobStartTime(String jobStartTime) {
		this.jobStartTime = jobStartTime;
	}

	public String getJobEndTime() {
		return jobEndTime;
	}

	public void setJobEndTime(String jobEndTime) {
		this.jobEndTime = jobEndTime;
	}

	public Integer getJobHours() {
		return jobHours;
	}

	public void setJobHours(Integer jobHours) {
		this.jobHours = jobHours;
	}

	public Integer getJobMinutes() {
		return jobMinutes;
	}

	public void setJobMinutes(Integer jobMinutes) {
		this.jobMinutes = jobMinutes;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
