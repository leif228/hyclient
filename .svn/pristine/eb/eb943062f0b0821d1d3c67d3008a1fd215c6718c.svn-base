package com.eyunda.third.domain.oil;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.PayStyleCode;
import com.eyunda.third.domain.enumeric.ReleaseStatusCode;

public class GasWaresData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L;
	private Long companyId = 0L; // 能源公司
	private GasCompanyData companyData = null;
	private String waresLogo = ""; // 商品Logo

	private String waresName = ""; // 商品名称
	private String subTitle = ""; // 商品描述
	private String description = ""; // 商品详情
	private Double stdPrice = 0.00D; // 市场价格
	private Double price = 0.00D; // 销售价格
	private String priceSignal = ""; // 价格标语
	private String createTime = ""; // 建立时间
	private ReleaseStatusCode status = ReleaseStatusCode.publish; // 商品状态
	private String sellTime = ""; // 上下架时间
	private Long adminId = 0L; // 发表者Id
	
	public GasWaresData(Map<String, Object> params){
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.adminId = ((Double) params.get("adminId")).longValue();
			this.companyData = new GasCompanyData((Map<String, Object>) params.get("companyData"));
			this.waresLogo = (String) params.get("waresLogo");
			this.waresName = (String) params.get("waresName");
			this.subTitle = (String) params.get("subTitle");
			this.description = (String) params.get("description");
			this.priceSignal = (String) params.get("priceSignal");
			this.createTime = (String) params.get("createTime");
			this.sellTime = (String) params.get("sellTime");

			this.stdPrice = (Double) params.get("stdPrice");
			this.price = (Double) params.get("price");
			
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

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public GasCompanyData getCompanyData() {
		return companyData;
	}

	public void setCompanyData(GasCompanyData companyData) {
		this.companyData = companyData;
	}

	public String getWaresLogo() {
		return waresLogo;
	}

	public void setWaresLogo(String waresLogo) {
		this.waresLogo = waresLogo;
	}

	

	public String getWaresName() {
		return waresName;
	}

	public void setWaresName(String waresName) {
		this.waresName = waresName;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getStdPrice() {
		return stdPrice;
	}

	public void setStdPrice(Double stdPrice) {
		this.stdPrice = stdPrice;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPriceSignal() {
		return priceSignal;
	}

	public void setPriceSignal(String priceSignal) {
		this.priceSignal = priceSignal;
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

	public String getSellTime() {
		return sellTime;
	}

	public void setSellTime(String sellTime) {
		this.sellTime = sellTime;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}


}
