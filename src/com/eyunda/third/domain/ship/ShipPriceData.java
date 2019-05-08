package com.eyunda.third.domain.ship;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.CargoTypeCode;
import com.eyunda.third.domain.order.PortData;
import com.eyunda.tools.NumberFormatUtil;

public class ShipPriceData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID
	private Long shipId = 0L; // 船舶ID
	private String startPortNo = ""; // 起运港
	private String endPortNo = ""; // 到达港
	private PortData startPortData = null;
	private PortData endPortData = null;
	private Double goodWeight = 0.00D; // 载货量
	private Double transFee = 0.00D; // 运输费用（元）
	private Integer intTransFee = 0; // 运输费用（元）
	private String periodCode = ""; // 期限编码
	private String periodTime = "";// 截止时间
	private String createTime = "";// 报价时间
	private Integer tonTeu = 0; // 载货量或载箱量
	private String cargoName = ""; // 货名
	private CargoTypeCode cargoType; //货类
	private String tonTeuDes = ""; // 载货量或载箱量描述
	private String priceDes = ""; // 单价描述
	private String intTransFeeDes = ""; // 总价描述
	
	@SuppressWarnings("unchecked")
	public ShipPriceData(Map<String, Object> params) {
		super();
		if(params != null && !params.isEmpty()){
			this.id = ((Double) params.get("id")).longValue();
			this.shipId = ((Double) params.get("shipId")).longValue();
			this.startPortNo = (String) params.get("startPortNo");
			this.endPortNo = (String) params.get("endPortNo");
			this.startPortData = new PortData((Map<String, Object>) params.get("startPortData"));
			this.endPortData = new PortData((Map<String, Object>) params.get("endPortData"));
			this.goodWeight = (Double) params.get("goodWeight");
			this.transFee = (Double) params.get("transFee");
			this.intTransFee = ((Double) params.get("intTransFee")).intValue();
			this.periodCode = (String) params.get("periodCode");
			this.periodTime = (String) params.get("periodTime");
			this.createTime = (String) params.get("createTime");
			this.tonTeu = ((Double) params.get("tonTeu")).intValue();
			this.cargoName=(String) params.get("cargoName");
			String cargo = (String) params.get("cargoType");
			if((cargo!=null) &&( !cargo.equals(""))){
				this.cargoType = CargoTypeCode.valueOf(cargo);
			}
			this.tonTeuDes = (String) params.get("tonTeuDes");
			this.priceDes = (String) params.get("priceDes");
			this.intTransFeeDes = (String) params.get("intTransFeeDes");
		}
	}


	public String getCargoName() {
		return cargoName;
	}


	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}


	public CargoTypeCode getCargoType() {
		return cargoType;
	}


	public void setCargoType(CargoTypeCode cargoType) {
		this.cargoType = cargoType;
	}


	

	public Integer getTonTeu() {
		return tonTeu;
	}


	public void setTonTeu(Integer tonTeu) {
		this.tonTeu = tonTeu;
	}


	public ShipPriceData() {
		super();
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShipId() {
		return shipId;
	}

	public void setShipId(Long shipId) {
		this.shipId = shipId;
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

	public PortData getEndPortData() {
		return endPortData;
	}

	public void setEndPortData(PortData endPortData) {
		this.endPortData = endPortData;
	}

	public Double getGoodWeight() {
		return goodWeight;
	}

	public void setGoodWeight(Double goodWeight) {
		this.goodWeight = goodWeight;
	}

	public Double getTransFee() {
		return transFee;
	}

	public void setTransFee(Double transFee) {
		this.transFee = transFee;
		this.intTransFee = NumberFormatUtil.toInt(transFee);
	}

	public Integer getIntTransFee() {
		return intTransFee;
	}

	public void setIntTransFee(Integer intTransFee) {
		this.intTransFee = intTransFee;
	}

	public String getPeriodCode() {
		return periodCode;
	}

	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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


	public String getIntTransFeeDes() {
		return intTransFeeDes;
	}


	public void setIntTransFeeDes(String intTransFeeDes) {
		this.intTransFeeDes = intTransFeeDes;
	}


	@Override
	public String toString() {
		return "ShipPriceData [id=" + id + ", shipId=" + shipId
				+ ", startPortNo=" + startPortNo + ", endPortNo=" + endPortNo
				+ ", startPortData=" + startPortData + ", endPortData="
				+ endPortData + ", goodWeight=" + goodWeight + ", transFee="
				+ transFee + ", intTransFee=" + intTransFee + ", periodCode="
				+ periodCode + ", periodTime=" + periodTime + ", createTime="
				+ createTime + "]";
	}
	

}
