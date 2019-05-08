package com.eyunda.third.domain.ship;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.CargoTypeCode;
import com.eyunda.third.domain.enumeric.WrapStyleCode;

public class ShipUpdownData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID
	private Long arvlftId = 0L; // 到离ID

	private CargoTypeCode cargoType = null; // 货类
	private String cargoName = ""; // 货名
	private String cargoTypeDescription = "";
	
	private WrapStyleCode wrapStyle = null; // 包装
	private Integer wrapCount = 0; // 数量
	private Double unitWeight = 0.00D; // 单重
	private Double fullWeight = 0.00D; // 总重
	private Double ctlLength = 0.00D; // 长
	private Double ctlWidth = 0.00D; // 宽
	private Double ctlHeight = 0.00D; // 高
	private Double ctlVolume = 0.00D; // 体积
	private Double tonTeu = 0.00D; // 货量或箱量
	
	public ShipUpdownData() {
		super();
	}

	public ShipUpdownData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.arvlftId = ((Double) params.get("arvlftId")).longValue();

			String cargo = (String) params.get("cargoType");
			if ((cargo != null) && (!cargo.equals(""))) {
				this.cargoType = CargoTypeCode.valueOf(cargo);
			}
			this.cargoName = (String) params.get("cargoName");
			this.cargoTypeDescription = (String) params.get("cargoTypeDescription");

			String wrap = (String) params.get("wrapStyle");
			if ((wrap != null) && (!wrap.equals(""))) {
				this.wrapStyle = WrapStyleCode.valueOf(wrap);
			}
			if(params.get("wrapCount")!=null)
			this.wrapCount = ((Double) params.get("wrapCount")).intValue();
			this.unitWeight = (Double) params.get("unitWeight");
			this.fullWeight = (Double) params.get("fullWeight");
			this.ctlLength = (Double) params.get("ctlLength");
			this.ctlWidth = (Double) params.get("ctlWidth");
			this.ctlHeight = (Double) params.get("ctlHeight");
			this.ctlVolume = (Double) params.get("ctlVolume");
			this.tonTeu = (Double) params.get("tonTeu");
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getArvlftId() {
		return arvlftId;
	}

	public void setArvlftId(Long arvlftId) {
		this.arvlftId = arvlftId;
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

	public String getCargoTypeDescription() {
		return cargoTypeDescription;
	}

	public void setCargoTypeDescription(String cargoTypeDescription) {
		this.cargoTypeDescription = cargoTypeDescription;
	}

	public WrapStyleCode getWrapStyle() {
		return wrapStyle;
	}

	public void setWrapStyle(WrapStyleCode wrapStyle) {
		this.wrapStyle = wrapStyle;
	}

	public Integer getWrapCount() {
		return wrapCount;
	}

	public void setWrapCount(Integer wrapCount) {
		this.wrapCount = wrapCount;
	}

	public Double getUnitWeight() {
		return unitWeight;
	}

	public void setUnitWeight(Double unitWeight) {
		this.unitWeight = unitWeight;
	}

	public Double getFullWeight() {
		return fullWeight;
	}

	public void setFullWeight(Double fullWeight) {
		this.fullWeight = fullWeight;
	}

	public Double getCtlLength() {
		return ctlLength;
	}

	public void setCtlLength(Double ctlLength) {
		this.ctlLength = ctlLength;
	}

	public Double getCtlWidth() {
		return ctlWidth;
	}

	public void setCtlWidth(Double ctlWidth) {
		this.ctlWidth = ctlWidth;
	}

	public Double getCtlHeight() {
		return ctlHeight;
	}

	public void setCtlHeight(Double ctlHeight) {
		this.ctlHeight = ctlHeight;
	}

	public Double getCtlVolume() {
		return ctlVolume;
	}

	public void setCtlVolume(Double ctlVolume) {
		this.ctlVolume = ctlVolume;
	}

	public Double getTonTeu() {
		return tonTeu;
	}

	public void setTonTeu(Double tonTeu) {
		this.tonTeu = tonTeu;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return cargoTypeDescription+" : "+wrapCount+wrapStyle.getDescription()+fullWeight+"吨";   
	}

}
