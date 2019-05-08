package com.eyunda.third.domain.cargo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.eyunda.third.adapters.chat.widget.CalendarUtil;
import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.CargoStatusCode;
import com.eyunda.third.domain.enumeric.CargoTypeCode;
import com.eyunda.third.domain.enumeric.EnumConst.RecentPeriodCode;
import com.eyunda.third.domain.order.PortData;

public class CargoData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID
	private Long ownerId = 0L; // 货主ID
	private UserData owner = null; // 货主
	private Long agentId = 0L; // 货代ID
	private UserData agent = null; // 货代
	private CargoTypeCode cargoType = CargoTypeCode.container20e; // 货类
	private String cargoTypeName = null;// 货类名称
	private String cargoName = ""; // 货名
	private String cargoImage = ""; // 货物图片

	private List<CargoContainerData> containers;
	private String containerNames = ""; // 规格
	private String containerTeus = ""; // 箱量(个)
	private String containerPrices = ""; // 运价(元/个)

	private String startPortNo = ""; // 起运港
	private String endPortNo = ""; // 到达港
	private PortData startPortData = null;
	private String startFullName = null;// 起运港全称
	private PortData endPortData = null;
	private String endFullName = null;// 到达港全称

	private Integer tonTeu = 0; // 货量或箱量
	private Double price = 0.00D; // 单价（元/吨）
	private Double transFee = 0.00D; // 总价（元）
	private String tonTeuDes = ""; // 货量或箱量描述
	private String priceDes = ""; // 单价描述（元/吨）
	private String transFeeDes = ""; // 总价描述（元）

	private String description = ""; // 货物描述

	private String receiver = ""; // 收货人
	private String receMobile = ""; // 收货人电话
	private String receAddress = ""; // 收货地址

	private String periodCode = RecentPeriodCode.ONE_MONTH.getCode(); // 期限编码
	private String createTime = CalendarUtil.toYYYY_MM_DD_HH_MM_SS(Calendar.getInstance()); // 发布时间
	private String periodTime = "";// 截止时间

	private CargoStatusCode cargoStatus = CargoStatusCode.nopublish; // 状态
	private String remark = ""; // 备注

	public CargoData() {
		containers = new ArrayList<CargoContainerData>();
		for (CargoTypeCode ct : CargoTypeCode.getCargoTypeCodes(CargoBigTypeCode.container)) {
			CargoContainerData ccd = new CargoContainerData();
			ccd.setCargoName(ct.getShortDesc());
			containers.add(ccd);
		}
	}

	@SuppressWarnings("unchecked")
	public CargoData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();

			this.agentId = ((Double) params.get("agentId")).longValue();
			this.ownerId = ((Double) params.get("ownerId")).longValue();
			this.agent = new UserData((Map<String, Object>) params.get("agent"));
			this.owner = new UserData((Map<String, Object>) params.get("owner"));

			String cargo = (String) params.get("cargoType");
			if ((cargo != null) && (!cargo.equals(""))) {
				this.cargoType = CargoTypeCode.valueOf(cargo);
			}
			this.cargoTypeName = (String) params.get("cargoTypeName");
			this.cargoName = (String) params.get("cargoName");
			this.cargoImage = (String) params.get("cargoImage");

			if (true) {
				List<CargoContainerData> datas = new ArrayList<CargoContainerData>();
				List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("containers");
				if (list != null && !list.isEmpty())
					for (Map<String, Object> map : list) {
						CargoContainerData data = new CargoContainerData(map);
						datas.add(data);
					}
				this.containers = datas;
			}

			this.containerNames = (String) params.get("containerNames");
			this.containerTeus = (String) params.get("containerTeus");
			this.containerPrices = (String) params.get("containerPrices");

			this.startPortData = new PortData((Map<String, Object>) params.get("startPortData"));
			this.endPortData = new PortData((Map<String, Object>) params.get("endPortData"));
			this.startPortNo = (String) params.get("startPortNo");
			this.endPortNo = (String) params.get("endPortNo");
			this.startFullName = (String) params.get("startFullName");
			this.endFullName = (String) params.get("endFullName");

			this.tonTeu = ((Double) params.get("tonTeu")).intValue();
			this.price = (Double) params.get("price");
			this.transFee = (Double) params.get("transFee");
			this.tonTeuDes = (String) params.get("tonTeuDes");
			this.priceDes = (String) params.get("priceDes");
			this.transFeeDes = (String) params.get("transFeeDes");

			this.description = (String) params.get("description");

			this.receiver = (String) params.get("receiver");
			this.receMobile = (String) params.get("receMobile");
			this.receAddress = (String) params.get("receAddress");

			this.periodCode = (String) params.get("periodCode");
			this.periodTime = (String) params.get("periodTime");
			this.createTime = (String) params.get("createTime");

			String cargoStatus = (String) params.get("cargoStatus");
			if ((cargoStatus != null) && (!cargoStatus.equals(""))) {
				this.cargoStatus = CargoStatusCode.valueOf(cargoStatus);
			}

			this.remark = (String) params.get("remark");
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public UserData getOwner() {
		return owner;
	}

	public void setOwner(UserData owner) {
		this.owner = owner;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public UserData getAgent() {
		return agent;
	}

	public void setAgent(UserData agent) {
		this.agent = agent;
	}

	public CargoTypeCode getCargoType() {
		return cargoType;
	}

	public void setCargoType(CargoTypeCode cargoType) {
		this.cargoType = cargoType;
	}

	public String getCargoTypeName() {
		return cargoTypeName;
	}

	public void setCargoTypeName(String cargoTypeName) {
		this.cargoTypeName = cargoTypeName;
	}

	public String getCargoName() {
		return cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}

	public String getCargoImage() {
		return cargoImage;
	}

	public void setCargoImage(String cargoImage) {
		this.cargoImage = cargoImage;
	}

	public List<CargoContainerData> getContainers() {
		return containers;
	}

	public void setContainers(List<CargoContainerData> containers) {
		this.containers = containers;
	}

	public String getContainerNames() {
		return containerNames;
	}

	public void setContainerNames(String containerNames) {
		this.containerNames = containerNames;
	}

	public String getContainerTeus() {
		return containerTeus;
	}

	public void setContainerTeus(String containerTeus) {
		this.containerTeus = containerTeus;
	}

	public String getContainerPrices() {
		return containerPrices;
	}

	public void setContainerPrices(String containerPrices) {
		this.containerPrices = containerPrices;
	}

	public String getStartPortNo() {
		return startPortNo;
	}

	public void setStartPortNo(String startPortNo) {
		this.startPortNo = startPortNo;
	}

	public String getEndPortNo() {
		return endPortNo;
	}

	public void setEndPortNo(String endPortNo) {
		this.endPortNo = endPortNo;
	}

	public PortData getStartPortData() {
		return startPortData;
	}

	public void setStartPortData(PortData startPortData) {
		this.startPortData = startPortData;
	}

	public String getStartFullName() {
		return startFullName;
	}

	public void setStartFullName(String startFullName) {
		this.startFullName = startFullName;
	}

	public PortData getEndPortData() {
		return endPortData;
	}

	public void setEndPortData(PortData endPortData) {
		this.endPortData = endPortData;
	}

	public String getEndFullName() {
		return endFullName;
	}

	public void setEndFullName(String endFullName) {
		this.endFullName = endFullName;
	}

	public Integer getTonTeu() {
		return tonTeu;
	}

	public void setTonTeu(Integer tonTeu) {
		this.tonTeu = tonTeu;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTransFee() {
		return transFee;
	}

	public void setTransFee(Double transFee) {
		this.transFee = transFee;
	}

	public String getTonTeuDes() {
		return tonTeuDes;
	}

	public void setTonTeuDes(String tonTeuDes) {
		this.tonTeuDes = tonTeuDes;
	}

	public String getPriceDes() {
		return priceDes;
	}

	public void setPriceDes(String priceDes) {
		this.priceDes = priceDes;
	}

	public String getTransFeeDes() {
		return transFeeDes;
	}

	public void setTransFeeDes(String transFeeDes) {
		this.transFeeDes = transFeeDes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceMobile() {
		return receMobile;
	}

	public void setReceMobile(String receMobile) {
		this.receMobile = receMobile;
	}

	public String getReceAddress() {
		return receAddress;
	}

	public void setReceAddress(String receAddress) {
		this.receAddress = receAddress;
	}

	public String getPeriodCode() {
		return periodCode;
	}

	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPeriodTime() {
		return periodTime;
	}

	public void setPeriodTime(String periodTime) {
		this.periodTime = periodTime;
	}

	public CargoStatusCode getCargoStatus() {
		return cargoStatus;
	}

	public void setCargoStatus(CargoStatusCode cargoStatus) {
		this.cargoStatus = cargoStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isContainer(){
		if(this.getCargoType() == null)return true;
		return this.getCargoType().equals(CargoTypeCode.container20e);
	}
}
