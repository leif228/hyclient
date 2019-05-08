package com.eyunda.third.domain.enumeric;

public enum PayStatusCode {
	WAIT_PAYMENT("未支付", "未完成", "未完成"),

	WAIT_CONFIRM("资金托管", "", ""),

	TRADE_FINISHED("确认付款", "提现完成", "充值完成"),

	TRADE_CLOSED("退款到钱包", "退款到银行卡", "");
	
	private String description;
	private String remark;
	private String remark2;

	private PayStatusCode(String description, String remark, String remark2) {
		this.description = description;
		this.remark = remark;
		this.remark2 = remark2;
	}

	public String getDescription() {
		return description;
	}

	public String getRemark() {
		return remark;
	}

	public String getRemark2() {
		return remark2;
	}
}
