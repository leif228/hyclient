package com.eyunda.third.domain.enumeric;

public enum WalletOptCode {
//	startSurety("开始担保"),
//	endSurety("结束担保"),
	pay("支付"),
	fetch("提现"),
	delete("删除"),
	confirmPay("确认付款"),
	applyRefund("申请退款"),
	refund("退款处理");

	
	private String description;

	private WalletOptCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
