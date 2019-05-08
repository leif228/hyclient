package com.eyunda.third.domain.order;

import java.util.Map;

import com.eyunda.third.domain.BaseData;

public class OrderContainerData extends BaseData {

	private static final long serialVersionUID = 1L;

	private Long id = 0L;
	private Long orderId = 0L; // 合同ID
	private String cargoName = ""; // 规格
	private Integer tonTeu = 0; // 箱量(个)
	private Double price = 0.00D; // 运价(元/个)

	public OrderContainerData() {
	}

	public OrderContainerData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.orderId = ((Double) params.get("orderId")).longValue();
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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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
