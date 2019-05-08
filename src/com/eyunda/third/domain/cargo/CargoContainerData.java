package com.eyunda.third.domain.cargo;

import java.util.Map;

import com.eyunda.third.domain.BaseData;

public class CargoContainerData extends BaseData {

	private static final long serialVersionUID = 1L;

	private Long id = 0L;
	private Long cargoId = 0L; // 货物ID
	private String cargoName = ""; // 规格
	private Integer tonTeu = 0; // 箱量(个)
	private Double price = 0.00D; // 运价(元/个)

	public CargoContainerData() {
	}

	public CargoContainerData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.cargoId = ((Double) params.get("cargoId")).longValue();
			this.cargoName = (String) params.get("cargoName");
			this.tonTeu = ((Double) params.get("tonTeu")).intValue();
			this.price = (Double) params.get("price");
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCargoId() {
		return cargoId;
	}

	public void setCargoId(Long cargoId) {
		this.cargoId = cargoId;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
