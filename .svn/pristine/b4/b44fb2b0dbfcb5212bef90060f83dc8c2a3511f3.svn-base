package com.eyunda.third.domain.oil;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.PayStyleCode;
import com.eyunda.third.domain.enumeric.SaleTypeCode;

public class GasCompanyData extends BaseData {

	private static final long serialVersionUID = 1L;

	private Long id = 0L;
	private SaleTypeCode saleType = null; // 卖家类型
	private String companyName = ""; // 公司名称
	private String address = ""; // 公司地址
	private String contact = ""; // 联系人
	private String mobile = ""; // 联系电话
	private String bigImage = ""; // 大图片
	private String smallImage = ""; // 小图片
	private String accounter = ""; // 开户人
	private PayStyleCode payStyle = PayStyleCode.alipay; // 支付方式
	private String accountNo = ""; // 收款账号
	private Long adminId = 0L; // 操作员Id

	public GasCompanyData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.adminId = ((Double) params.get("adminId")).longValue();

			this.companyName = (String) params.get("companyName");
			this.address = (String) params.get("address");
			this.contact = (String) params.get("contact");
			this.mobile = (String) params.get("mobile");
			this.bigImage = (String) params.get("bigImage");
			this.smallImage = (String) params.get("smallImage");
			this.accounter = (String) params.get("accounter");
			this.accountNo = (String) params.get("accountNo");

			String payStyle = (String) params.get("payStyle");
			if ((payStyle != null) && (!payStyle.equals(""))) {
				this.payStyle = PayStyleCode.valueOf(payStyle);
			}
			String saleType = (String) params.get("saleType");
			if ((saleType != null) && (!saleType.equals(""))) {
				this.saleType = SaleTypeCode.valueOf(saleType);
			}
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SaleTypeCode getSaleType() {
		return saleType;
	}

	public void setSaleType(SaleTypeCode saleType) {
		this.saleType = saleType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBigImage() {
		return bigImage;
	}

	public void setBigImage(String bigImage) {
		this.bigImage = bigImage;
	}

	public String getSmallImage() {
		return smallImage;
	}

	public void setSmallImage(String smallImage) {
		this.smallImage = smallImage;
	}

	public String getAccounter() {
		return accounter;
	}

	public void setAccounter(String accounter) {
		this.accounter = accounter;
	}

	public PayStyleCode getPayStyle() {
		return payStyle;
	}

	public void setPayStyle(PayStyleCode payStyle) {
		this.payStyle = payStyle;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

}
