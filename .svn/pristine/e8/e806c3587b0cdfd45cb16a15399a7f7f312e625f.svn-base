package com.eyunda.tools.pay;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.order.PortData;

public class PayEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String seller;//收款方
	private String payer;//付款方，不需要填
	private String royaltyParameters;//分润信息
	private String payStyleCode;//付款方式，默认支付宝
	private String price;//费用
	private String subject;//合同标题
	private String body;//合同内容
	private String orderNo;//合同号
	public PayEntity() {
	}
	public PayEntity(Map<String, Object> params){
		super();
		if(params != null && !params.isEmpty()){
			this.price = (Double) params.get("totalFee")+"";
			this.seller = (String) params.get("sellerEmail");
			this.orderNo = (String) params.get("orderNo");
			this.body = (String) params.get("orderDesc");
			this.payStyleCode = (String) params.get("payStyleCode");
			if(params.containsKey("royaltyParameters")){
				this.royaltyParameters = (String) params.get("royaltyParameters");
			}else{
				this.royaltyParameters = "";
			}
			if(params.containsKey("payer")){
				this.payer = (String) params.get("payer");
			}else{
				this.payer = "";
			}
			if(params.containsKey("subject")){
				this.subject = (String) params.get("subject");
			}else{
				this.subject = this.body;
			}
		}
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String fee) {
		this.price = fee;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String title) {
		this.subject = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getRoyaltyParameters() {
		return royaltyParameters;
	}
	public void setRoyaltyParameters(String royaltyParameters) {
		this.royaltyParameters = royaltyParameters;
	}
	public String getPayStyleCode() {
		return payStyleCode;
	}
	public void setPayStyleCode(String payStyleCode) {
		this.payStyleCode = payStyleCode;
	}

}
