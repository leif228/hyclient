package com.eyunda.third.domain.enumeric;

public enum SettleStyleCode {
	fill("充值", "将绑定银行卡的钱转入钱包"),
	fetch("提现", "将钱包的钱转入绑定的银行卡"),
	turn("转帐", "将钱包的钱转入别人的银行卡"),
	pay("交易", "一个会员支付交易金额给另一个会员,可设担保期,分担保、成交、退款三种状态");

	private String description;
	private String remark;

	private SettleStyleCode(String description, String remark) {
		this.description = description;
		this.remark = remark;
	}

	public String getDescription() {
		return description;
	}

	public String getRemark() {
		return remark;
	}
}
