package com.eyunda.third.domain.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.CargoTypeCode;
import com.eyunda.third.domain.enumeric.MoneyStyleCode;
import com.eyunda.third.domain.enumeric.OrderStatusCode;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.domain.ship.TypeData;

public class OrderData extends BaseData {

	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID

	private String shipType = ""; // 船舶类别编码
	private TypeData typeData = null;

	private Long shipId = 0L; // 船舶ID
	private ShipData shipData = null;

	private Long ownerId = 0L; // 货主ID
	private UserData owner = null; // 货主
	private Long agentId = 0L; // 货代ID
	private UserData agent = null; // 货代
	private Long masterId = 0L; // 船东ID
	private UserData master = null;
	private Long brokerId = 0L; // 船代ID
	private UserData broker = null;

	private UserData handler = null;
	private Long handlerId = 0L; // 业务员
	
	private String startPortNo = ""; // 起运港
	private String endPortNo = ""; // 到达港
	private PortData startPort = null; // 起运港
	private PortData endPort = null; // 到达港

	private CargoTypeCode cargoType = CargoTypeCode.container20e; // 货类
	private String cargoName = ""; // 货名
	private Integer tonTeu = 0; // 货量或箱量
	private Double price = 0.00D; // 运价（元/吨）
	private Double transFee = 0.00D; // 运输费用（元）
	private Double demurrage = 0.00D; // 滞期费率（元/天）
	private Double bankFee = 0.00D; // 银行佣金
	private Double platFee = 0.00D; // 平台服务费
	private Double brokerFee = 0.00D; // 代理费

	private String upDate = ""; // 装船日期
	private String downDate = ""; // 卸船日期
	private Integer upTime = 0; // 装船时间（小时）
	private Integer downTime = 0; // 卸船时间（小时）
	private String receiver = ""; // 收货人
	private String receMobile = ""; // 收货人电话
	private String receAddress = ""; // 收货地址

	private Integer suretyDays = 0; // 担保期（天）
	private MoneyStyleCode moneyStyle = null; // 付款方式

	private OrderStatusCode status = null; // 状态
	private String createTime = ""; // 建立时间

	private String orderContent = ""; // 特约条款

	private String pdfFileName = ""; // 合同文件名

	private List<OrderContainerData> containers = null;
	private String containerNames = ""; // 规格
	private String containerTeus = ""; // 箱量(个)
	private String containerPrices = ""; // 运价(元/个)
	private String containerFees = ""; // 运费小计(元)

	// 当前用户对该合同可进行的操作
	private Map<String, Boolean> ops = new HashMap<String, Boolean>();

	public OrderData() {
		containers = new ArrayList<OrderContainerData>();
		for (CargoTypeCode ct : CargoTypeCode.getCargoTypeCodes(CargoBigTypeCode.container)) {
			OrderContainerData ocd = new OrderContainerData();
			ocd.setCargoName(ct.getShortDesc());
			containers.add(ocd);
		}
	}

	@SuppressWarnings("unchecked")
	public OrderData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();

			this.shipType = (String) params.get("shipType");
			this.typeData = new TypeData((Map<String, Object>) params.get("typeData"));

			this.shipId = ((Double) params.get("shipId")).longValue();
			this.shipData = new ShipData((Map<String, Object>) params.get("shipData"));

			this.ownerId = ((Double) params.get("ownerId")).longValue();
			this.owner = new UserData((Map<String, Object>) params.get("owner"));
			this.agentId = ((Double) params.get("agentId")).longValue();
			this.agent = new UserData((Map<String, Object>) params.get("agent"));
			this.masterId = ((Double) params.get("masterId")).longValue();
			this.master = new UserData((Map<String, Object>) params.get("master"));
			this.brokerId = ((Double) params.get("brokerId")).longValue();
			this.broker = new UserData((Map<String, Object>) params.get("broker"));
			this.handlerId = ((Double) params.get("handlerId")).longValue();
			this.handler = new UserData((Map<String, Object>) params.get("handler"));
			this.startPortNo = (String) params.get("startPortNo");
			this.endPortNo = (String) params.get("endPortNo");
			this.startPort = new PortData((Map<String, Object>) params.get("startPort"));
			this.endPort = new PortData((Map<String, Object>) params.get("endPort"));

			String cargo = (String) params.get("cargoType");
			if ((cargo != null) && (!cargo.equals(""))) {
				this.cargoType = CargoTypeCode.valueOf(cargo);
			}
			this.cargoName = (String) params.get("cargoName");
			this.tonTeu = ((Double) params.get("tonTeu")).intValue();
			this.price = (Double) params.get("price");
			this.transFee = (Double) params.get("transFee");
			this.demurrage = (Double) params.get("demurrage");
			this.bankFee = (Double) params.get("bankFee");
			this.platFee = (Double) params.get("platFee");
			this.brokerFee = (Double) params.get("brokerFee");

			this.upDate = (String) params.get("upDate");
			this.downDate = (String) params.get("downDate");
			this.upTime = ((Double) params.get("upTime")).intValue();
			this.downTime = ((Double) params.get("downTime")).intValue();
			this.receiver = (String) params.get("receiver");
			this.receMobile = (String) params.get("receMobile");
			this.receAddress = (String) params.get("receAddress");
			
			if(params.get("suretyDays")!=null)
				this.suretyDays = ((Double) params.get("suretyDays")).intValue();
			
			String moneyStyle = (String) params.get("moneyStyle");
			if ((moneyStyle != null) && (!moneyStyle.equals(""))) {
				this.moneyStyle = MoneyStyleCode.valueOf(moneyStyle);
			}
			
			String oStatus = (String) params.get("status");
			if ((oStatus != null) && (!oStatus.equals(""))) {
				this.status = OrderStatusCode.valueOf(oStatus);
			}
			this.createTime = (String) params.get("createTime");

			this.orderContent = (String) params.get("orderContent");

			this.pdfFileName = (String) params.get("pdfFileName");

			if (true) {
				List<OrderContainerData> datas = new ArrayList<OrderContainerData>();
				List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("containers");
				if (list != null && !list.isEmpty())
					for (Map<String, Object> map : list) {
						OrderContainerData data = new OrderContainerData(map);
						datas.add(data);
					}
				this.containers = datas;
			}
			this.containerNames = (String) params.get("containerNames");
			this.containerTeus = (String) params.get("containerTeus");
			this.containerPrices = (String) params.get("containerPrices");
			this.containerFees = (String) params.get("containerFees");

			this.ops = (Map<String, Boolean>) params.get("ops");
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}

	public TypeData getTypeData() {
		return typeData;
	}

	public void setTypeData(TypeData typeData) {
		this.typeData = typeData;
	}

	public Long getShipId() {
		return shipId;
	}

	public void setShipId(Long shipId) {
		this.shipId = shipId;
	}

	public ShipData getShipData() {
		return shipData;
	}

	public void setShipData(ShipData shipData) {
		this.shipData = shipData;
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

	public Long getMasterId() {
		return masterId;
	}

	public void setMasterId(Long masterId) {
		this.masterId = masterId;
	}

	public UserData getMaster() {
		return master;
	}

	public void setMaster(UserData master) {
		this.master = master;
	}

	public Long getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(Long brokerId) {
		this.brokerId = brokerId;
	}

	public UserData getBroker() {
		return broker;
	}

	public void setBroker(UserData broker) {
		this.broker = broker;
	}

	public UserData getHandler() {
		return handler;
	}

	public void setHandler(UserData handler) {
		this.handler = handler;
	}

	public Long getHandlerId() {
		return handlerId;
	}

	public void setHandlerId(Long handlerId) {
		this.handlerId = handlerId;
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

	public PortData getStartPort() {
		return startPort;
	}

	public void setStartPort(PortData startPort) {
		this.startPort = startPort;
	}

	public PortData getEndPort() {
		return endPort;
	}

	public void setEndPort(PortData endPort) {
		this.endPort = endPort;
	}

	public CargoTypeCode getCargoType() {
		return cargoType;
	}

	public void setCargoType(CargoTypeCode cargoType) {
		this.cargoType = cargoType;
	}

	public String getCargoName() {
		return cargoName;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
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

	public Double getDemurrage() {
		return demurrage;
	}

	public void setDemurrage(Double demurrage) {
		this.demurrage = demurrage;
	}

	public Double getBankFee() {
		return bankFee;
	}

	public void setBankFee(Double bankFee) {
		this.bankFee = bankFee;
	}

	public Double getPlatFee() {
		return platFee;
	}

	public void setPlatFee(Double platFee) {
		this.platFee = platFee;
	}

	public Double getBrokerFee() {
		return brokerFee;
	}

	public void setBrokerFee(Double brokerFee) {
		this.brokerFee = brokerFee;
	}

	public String getUpDate() {
		return upDate;
	}

	public void setUpDate(String upDate) {
		this.upDate = upDate;
	}

	public String getDownDate() {
		return downDate;
	}

	public void setDownDate(String downDate) {
		this.downDate = downDate;
	}

	public Integer getUpTime() {
		return upTime;
	}

	public void setUpTime(Integer upTime) {
		this.upTime = upTime;
	}

	public Integer getDownTime() {
		return downTime;
	}

	public void setDownTime(Integer downTime) {
		this.downTime = downTime;
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

	public Integer getSuretyDays() {
		return suretyDays;
	}

	public void setSuretyDays(Integer suretyDays) {
		this.suretyDays = suretyDays;
	}

	public MoneyStyleCode getMoneyStyle() {
		return moneyStyle;
	}

	public void setMoneyStyle(MoneyStyleCode moneyStyle) {
		this.moneyStyle = moneyStyle;
	}

	public OrderStatusCode getStatus() {
		return status;
	}

	public void setStatus(OrderStatusCode status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOrderContent() {
		return orderContent;
	}

	public String getShowOrderContent() {
		return orderContent.replace("\n", "<br />");
	}

	public void setOrderContent(String orderContent) {
		this.orderContent = orderContent;
	}

	public String getPdfFileName() {
		return pdfFileName;
	}

	public void setPdfFileName(String pdfFileName) {
		this.pdfFileName = pdfFileName;
	}

	public List<OrderContainerData> getContainers() {
		return containers;
	}

	public void setContainers(List<OrderContainerData> containers) {
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

	public String getContainerFees() {
		return containerFees;
	}

	public void setContainerFees(String containerFees) {
		this.containerFees = containerFees;
	}

	public Map<String, Boolean> getOps() {
		return ops;
	}

	public void setOps(Map<String, Boolean> ops) {
		this.ops = ops;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
