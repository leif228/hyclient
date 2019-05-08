package com.eyunda.third.domain.wallet;

import java.util.HashMap;
import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.ApplyReplyCode;
import com.eyunda.third.domain.enumeric.FeeItemCode;
import com.eyunda.third.domain.enumeric.PayStatusCode;
import com.eyunda.third.domain.enumeric.SettleStyleCode;

public class WalletData extends BaseData {

	private static final long serialVersionUID = 1L;

	private Long id = 0L; // 钱包账务id

	private SettleStyleCode settleStyle = null; // 帐务类型
	private String paymentNo = ""; // 支付序列号：客户号10位＋日期8位＋序列号8位

	private FeeItemCode feeItem = null; // 费项类型
	private Long orderId = 0L; // 订单ID

	private Long buyerId = 0L;
	private UserData buyerData = null; // 买家
	private String sndAccountName = ""; // 付款帐户名
	private String sndCardNo = ""; // 付款帐号

	private Long brokerId = 0L; // 中间人ID
	private UserData brokerData = null; // 中间人
	private String brokerAccountName = ""; // 收款帐户名
	private String brokerCardNo = ""; // 收款帐号

	private Long sellerId = 0L;
	private UserData sellerData = null; // 卖家
	private String rcvAccountName = ""; // 收款帐户名
	private String rcvCardNo = ""; // 收款帐号

	private String subject = ""; // 交易标题
	private String body = ""; // 交易描述

	private Double totalFee = 0.00D; // 交易金额
	private Double middleFee = 0.00D; // 代理人佣金
	private Double serviceFee = 0.00D; // 平台服务费

	private PayStatusCode paymentStatus = null; // 支付状态
	private String gmtPayment = ""; // 支付时间

	private Integer suretyDays = 0; // 担保天数
	private String gmtSurety = ""; // 担保时间

	private ApplyReplyCode refundStatus = null; // 退款状态
	private String gmtRefund = ""; // 退款时间

	// 当前用户可进行的操作
	private Map<String, Boolean> ops = new HashMap<String, Boolean>();

	@SuppressWarnings("unchecked")
	public WalletData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			String settleStyle = (String) params.get("settleStyle");
			if ((settleStyle != null) && (!settleStyle.equals(""))) {
				this.settleStyle = SettleStyleCode.valueOf(settleStyle);
			}
			String feeItem = (String) params.get("feeItem");
			if ((feeItem != null) && (!feeItem.equals(""))) {
				this.feeItem = FeeItemCode.valueOf(feeItem);
			}
			this.paymentNo = (String) params.get("paymentNo");
			if (null != params.get("orderId")) {
				this.orderId = ((Double) params.get("orderId")).longValue();
			} else {
				this.orderId = 0L;
			}
			this.buyerId = ((Double) params.get("buyerId")).longValue();
			this.buyerData = new UserData((Map<String, Object>) params.get("buyerData"));
			this.sndAccountName = (String) params.get("sndAccountName");
			this.sndCardNo = (String) params.get("sndCardNo");

			this.sellerId = ((Double) params.get("sellerId")).longValue();
			this.sellerData = new UserData((Map<String, Object>) params.get("sellerData"));
			this.rcvAccountName = (String) params.get("rcvAccountName");
			this.rcvCardNo = (String) params.get("rcvCardNo");

			this.brokerId = ((Double) params.get("brokerId")).longValue();
			this.brokerData = new UserData((Map<String, Object>) params.get("brokerData"));
			this.brokerAccountName = (String) params.get("brokerAccountName");
			this.brokerCardNo = (String) params.get("brokerCardNo");

			this.subject = (String) params.get("subject");
			this.body = (String) params.get("body");
			this.totalFee = (Double) params.get("totalFee");
			this.middleFee = (Double) params.get("middleFee");
			this.serviceFee = (Double) params.get("serviceFee");

			this.gmtPayment = (String) params.get("gmtPayment");
			String paymentStatus = (String) params.get("paymentStatus");
			if ((paymentStatus != null) && (!paymentStatus.equals(""))) {
				this.setPaymentStatus(PayStatusCode.valueOf(paymentStatus));
			}

			if (params.get("suretyDays") != null)
				this.suretyDays = ((Double) params.get("suretyDays")).intValue();
			this.gmtSurety = (String) params.get("gmtSurety");

			String refundStatus = (String) params.get("refundStatus");
			if ((refundStatus != null) && (!refundStatus.equals(""))) {
				this.refundStatus = ApplyReplyCode.valueOf(refundStatus);
			}
			this.gmtRefund = (String) params.get("gmtRefund");

			this.ops = (Map<String, Boolean>) params.get("ops");
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SettleStyleCode getSettleStyle() {
		return settleStyle;
	}

	public void setSettleStyle(SettleStyleCode settleStyle) {
		this.settleStyle = settleStyle;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public UserData getSellerData() {
		return sellerData;
	}

	public void setSellerData(UserData sellerData) {
		this.sellerData = sellerData;
	}

	public String getRcvAccountName() {
		return rcvAccountName;
	}

	public void setRcvAccountName(String rcvAccountName) {
		this.rcvAccountName = rcvAccountName;
	}

	public String getRcvCardNo() {
		return rcvCardNo;
	}

	public void setRcvCardNo(String rcvCardNo) {
		this.rcvCardNo = rcvCardNo;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public UserData getBuyerData() {
		return buyerData;
	}

	public void setBuyerData(UserData buyerData) {
		this.buyerData = buyerData;
	}

	public String getSndAccountName() {
		return sndAccountName;
	}

	public void setSndAccountName(String sndAccountName) {
		this.sndAccountName = sndAccountName;
	}

	public String getSndCardNo() {
		return sndCardNo;
	}

	public void setSndCardNo(String sndCardNo) {
		this.sndCardNo = sndCardNo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public String getGmtPayment() {
		return gmtPayment;
	}

	public void setGmtPayment(String gmtPayment) {
		this.gmtPayment = gmtPayment;
	}

	public Integer getSuretyDays() {
		return suretyDays;
	}

	public void setSuretyDays(Integer suretyDays) {
		this.suretyDays = suretyDays;
	}

	public PayStatusCode getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PayStatusCode paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Long getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(Long brokerId) {
		this.brokerId = brokerId;
	}

	public UserData getBrokerData() {
		return brokerData;
	}

	public void setBrokerData(UserData brokerData) {
		this.brokerData = brokerData;
	}

	public String getBrokerAccountName() {
		return brokerAccountName;
	}

	public void setBrokerAccountName(String brokerAccountName) {
		this.brokerAccountName = brokerAccountName;
	}

	public String getBrokerCardNo() {
		return brokerCardNo;
	}

	public void setBrokerCardNo(String brokerCardNo) {
		this.brokerCardNo = brokerCardNo;
	}

	public Double getMiddleFee() {
		return middleFee;
	}

	public void setMiddleFee(Double middleFee) {
		this.middleFee = middleFee;
	}

	public Double getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(Double serviceFee) {
		this.serviceFee = serviceFee;
	}

	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	public FeeItemCode getFeeItem() {
		return feeItem;
	}

	public void setFeeItem(FeeItemCode feeItem) {
		this.feeItem = feeItem;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getGmtSurety() {
		return gmtSurety;
	}

	public void setGmtSurety(String gmtSurety) {
		this.gmtSurety = gmtSurety;
	}

	public ApplyReplyCode getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(ApplyReplyCode refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getGmtRefund() {
		return gmtRefund;
	}

	public void setGmtRefund(String gmtRefund) {
		this.gmtRefund = gmtRefund;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Map<String, Boolean> getOps() {
		return ops;
	}

	public void setOps(Map<String, Boolean> ops) {
		this.ops = ops;
	}

}
