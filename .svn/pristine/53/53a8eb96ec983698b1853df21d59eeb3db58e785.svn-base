package com.eyunda.third.domain.ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.CargoTypeCode;
import com.eyunda.third.domain.enumeric.ShipStatusCode;
import com.eyunda.third.domain.enumeric.WarrantTypeCode;
import com.eyunda.third.domain.order.TemplateData;


public class ShipData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID
	private String shipType = ""; // ID
	private String mmsi = ""; // MMSI编号
	private String shipCode = ""; // 船舶编码
	private String shipName = ""; // 船舶名称
	private String englishName = ""; // 船舶英文名称
	private String imo = ""; // IMO编号
	private String callsign = ""; // 呼号
	private Double length = 0.00D; // 船长
	private Double breadth = 0.00D; // 船宽
	private Double mouldedDepth = 0.00D; // 型深
	private Double draught = 0.00D; // 吃水深度
	private Integer sumTons = 0; // 总吨
	private Integer cleanTons = 0; // 净吨
	private Integer aTons = 0; // 载重A级(吨)
	private Integer bTons = 0; // 载重B级(吨)
	private Integer fullContainer = 0; // 重箱(TEU)
	private Integer halfContainer = 0; // 半重箱(TEU)
	private Integer spaceContainer = 0; // 吉箱(TEU)

	private Integer pointCount = 0; // 点击数
	private Integer orderCount = 0; // 成交合同数
	private String keyWords = ""; // 航线
	private String shipTitle = ""; // 摘要描述
	private String shipLogo = ""; // 船舶Logo
	private WarrantTypeCode warrantType = WarrantTypeCode.personWarrant; // 委托类型
	private String shipMaster = ""; // 船东姓名
	private String idCardFront = ""; // 船东身份证正面
	private String idCardBack = ""; // 船东身份证反面
	private String warrant = ""; // 船东委托书
	private String certificate = ""; // 船舶运营证
	private ShipStatusCode shipStatus = ShipStatusCode.edit; // 编辑状态
	private String statusTime = "";
	private String releaseTime = "";
	
	private Long masterId = 0L; // 船东ID
	private UserData master = null;
	private Long brokerId = 0L; // 船代ID
	private UserData broker = null;

	private TypeData typeData = null;



	private List<AttrNameData> attrNameDatas;
	private List<ShipAttaData> myShipAttaDatas;
	private List<ShipStopData> shipArvlftDatas;

	private TemplateData templateData = null;// 合同样板

	private List<CargoTypeCode> shipCargoTypes;// 接货类别集合
	private List<ShipPortData> shipPortDatas;// 接货城市或港口编码集合

	private String arvlftDesc = "未上报"; // 船舶动态

	public ShipData() {
		super();
	}

	@SuppressWarnings("unchecked")
	public ShipData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.shipType = (String) params.get("shipType");
			this.mmsi = (String) params.get("mmsi");
			this.shipCode = (String) params.get("shipCode");
			this.shipName = (String) params.get("shipName");
			this.englishName = (String) params.get("englishName");

			this.imo = (String) params.get("imo");
			this.callsign = (String) params.get("callsign");
			this.length = (Double) params.get("length");
			this.breadth = (Double) params.get("breadth");
			this.draught = (Double) params.get("draught");
			this.mouldedDepth = (Double) params.get("mouldedDepth");

			this.sumTons = ((Double) params.get("sumTons")).intValue();
			this.cleanTons = ((Double) params.get("cleanTons")).intValue();
			this.aTons = ((Double) params.get("aTons")).intValue();
			this.bTons = ((Double) params.get("bTons")).intValue();
			this.fullContainer = ((Double) params.get("fullContainer")).intValue();
			this.halfContainer = ((Double) params.get("halfContainer")).intValue();
			this.spaceContainer = ((Double) params.get("spaceContainer")).intValue();

			this.pointCount = ((Double) params.get("pointCount")).intValue();
			this.orderCount = ((Double) params.get("orderCount")).intValue();
			this.keyWords = (String) params.get("keyWords");
			this.shipTitle = (String) params.get("shipTitle");
			this.arvlftDesc = (String) params.get("arvlftDesc");

			this.shipLogo = (String) params.get("shipLogo");
			this.idCardFront = (String) params.get("idCardFront");
			this.idCardBack = (String) params.get("idCardBack");
			this.warrant = (String) params.get("warrant");
			this.certificate = (String) params.get("certificate");
			this.shipMaster = (String) params.get("shipMaster");

			String tmpWarrantType = (String) params.get("warrantType");
			if ((tmpWarrantType != null) && (!tmpWarrantType.equals(""))) {
				this.warrantType = WarrantTypeCode.valueOf(tmpWarrantType);
			}
			
			String tmpReleaseStatus = (String) params.get("shipStatus");
			if ((tmpReleaseStatus != null) && (!tmpReleaseStatus.equals(""))) {
				this.shipStatus = ShipStatusCode.valueOf(tmpReleaseStatus);
			}

			this.releaseTime = (String) params.get("releaseTime");
			this.brokerId = ((Double) params.get("brokerId")).longValue();
			this.masterId = ((Double) params.get("masterId")).longValue();
			this.typeData = new TypeData((Map<String, Object>) params.get("typeData"));
			this.master = new UserData((Map<String, Object>) params.get("master"));
			this.broker = new UserData((Map<String, Object>) params.get("broker"));
			this.templateData = new TemplateData((Map<String, Object>) params.get("templateData"));
			if (true) {
				List<AttrNameData> datas = new ArrayList<AttrNameData>();
				List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("attrNameDatas");
				if (list != null && !list.isEmpty())
					for (Map<String, Object> map : list) {
						AttrNameData data = new AttrNameData(map);
						datas.add(data);
					}
				this.attrNameDatas = datas;
			}
			if (true) {
				List<ShipAttaData> datas = new ArrayList<ShipAttaData>();
				List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("myShipAttaDatas");
				if (list != null && !list.isEmpty())
					for (Map<String, Object> map : list) {
						ShipAttaData data = new ShipAttaData(map);
						datas.add(data);
					}
				this.myShipAttaDatas = datas;
			}
			if (true) {
				List<ShipStopData> datas = new ArrayList<ShipStopData>();
				List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("shipArvlftDatas");
				if (list != null && !list.isEmpty())
					for (Map<String, Object> map : list) {
						ShipStopData data = new ShipStopData(map);
						datas.add(data);
					}
				this.shipArvlftDatas = datas;
			}

			if (true) {
				List<CargoTypeCode> cargoTypes = new ArrayList<CargoTypeCode>();
				List<String> list = (List<String>) params.get("shipCargoTypes");
				if (list != null && !list.isEmpty())
					for (String strCargoType : list)
						cargoTypes.add(CargoTypeCode.valueOf(strCargoType));
				this.shipCargoTypes = cargoTypes;
			}

			if (true) {
				List<ShipPortData> datas = new ArrayList<ShipPortData>();
				List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("shipPortDatas");
				if (list != null && !list.isEmpty())
					for (Map<String, Object> map : list) {
						ShipPortData data = new ShipPortData(map);
						datas.add(data);
					}
				this.shipPortDatas = datas;
			}

		}
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

	public List<AttrNameData> getAttrNameDatas() {
		return attrNameDatas;
	}

	public void setAttrNameDatas(List<AttrNameData> attrNameDatas) {
		this.attrNameDatas = attrNameDatas;
	}

	public List<ShipAttaData> getMyShipAttaDatas() {
		return myShipAttaDatas;
	}

	public void setMyShipAttaDatas(List<ShipAttaData> myShipAttaDatas) {
		this.myShipAttaDatas = myShipAttaDatas;
	}

	public List<ShipStopData> getShipArvlftDatas() {
		return shipArvlftDatas;
	}

	public void setShipArvlftDatas(List<ShipStopData> shipArvlftDatas) {
		this.shipArvlftDatas = shipArvlftDatas;
	}



	public TemplateData getTemplateData() {
		return templateData;
	}

	public void setTemplateData(TemplateData templateData) {
		this.templateData = templateData;
	}

	public List<CargoTypeCode> getShipCargoTypes() {
		return shipCargoTypes;
	}

	public void setShipCargoTypes(List<CargoTypeCode> shipCargoTypes) {
		this.shipCargoTypes = shipCargoTypes;
	}

	public List<ShipPortData> getShipPortDatas() {
		return shipPortDatas;
	}

	public void setShipPortDatas(List<ShipPortData> shipPortDatas) {
		this.shipPortDatas = shipPortDatas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TypeData getTypeData() {
		return typeData;
	}

	public void setTypeData(TypeData typeData) {
		this.typeData = typeData;
	}

	public String getMmsi() {
		return mmsi;
	}

	public void setMmsi(String mmsi) {
		this.mmsi = mmsi;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getShipCode() {
		return shipCode;
	}

	public void setShipCode(String shipCode) {
		this.shipCode = shipCode;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public Integer getPointCount() {
		return pointCount;
	}

	public void setPointCount(Integer pointCount) {
		this.pointCount = pointCount;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getShipTitle() {
		return shipTitle;
	}

	public void setShipTitle(String shipTitle) {
		this.shipTitle = shipTitle;
	}

	public String getArvlftDesc() {
		return arvlftDesc;
	}

	public void setArvlftDesc(String arvlftDesc) {
		this.arvlftDesc = arvlftDesc;
	}

	public String getShipLogo() {
		return shipLogo;
	}

	public void setShipLogo(String shipLogo) {
		this.shipLogo = shipLogo;
	}


	public String getStatusTime() {
		return statusTime;
	}

	public void setStatusTime(String statusTime) {
		this.statusTime = statusTime;
	}

	public String getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}


	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getImo() {
		return imo;
	}

	public void setImo(String imo) {
		this.imo = imo;
	}

	public String getCallsign() {
		return callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getBreadth() {
		return breadth;
	}

	public void setBreadth(Double breadth) {
		this.breadth = breadth;
	}

	public Double getDraught() {
		return draught;
	}

	public void setDraught(Double draught) {
		this.draught = draught;
	}

	public Integer getSumTons() {
		return sumTons;
	}

	public void setSumTons(Integer sumTons) {
		this.sumTons = sumTons;
	}

	public Integer getCleanTons() {
		return cleanTons;
	}

	public void setCleanTons(Integer cleanTons) {
		this.cleanTons = cleanTons;
	}

	public Integer getaTons() {
		return aTons;
	}

	public void setaTons(Integer aTons) {
		this.aTons = aTons;
	}

	public Integer getbTons() {
		return bTons;
	}

	public void setbTons(Integer bTons) {
		this.bTons = bTons;
	}

	public Integer getFullContainer() {
		return fullContainer;
	}

	public void setFullContainer(Integer fullContainer) {
		this.fullContainer = fullContainer;
	}

	public Integer getHalfContainer() {
		return halfContainer;
	}

	public void setHalfContainer(Integer halfContainer) {
		this.halfContainer = halfContainer;
	}

	public Integer getSpaceContainer() {
		return spaceContainer;
	}

	public void setSpaceContainer(Integer spaceContainer) {
		this.spaceContainer = spaceContainer;
	}

	public Double getMouldedDepth() {
		return mouldedDepth;
	}

	public void setMouldedDepth(Double mouldedDepth) {
		this.mouldedDepth = mouldedDepth;
	}

	public WarrantTypeCode getWarrantType() {
		return warrantType;
	}

	public void setWarrantType(WarrantTypeCode warrantType) {
		this.warrantType = warrantType;
	}

	public String getShipMaster() {
		return shipMaster;
	}

	public void setShipMaster(String shipMaster) {
		this.shipMaster = shipMaster;
	}

	public String getIdCardFront() {
		return idCardFront;
	}

	public void setIdCardFront(String idCardFront) {
		this.idCardFront = idCardFront;
	}

	public String getIdCardBack() {
		return idCardBack;
	}

	public void setIdCardBack(String idCardBack) {
		this.idCardBack = idCardBack;
	}

	public String getWarrant() {
		return warrant;
	}

	public void setWarrant(String warrant) {
		this.warrant = warrant;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public ShipStatusCode getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(ShipStatusCode shipStatus) {
		this.shipStatus = shipStatus;
	}

}
