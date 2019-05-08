package com.eyunda.third.domain.enumeric;

public enum OrderStatusCode {
	edit("编辑"),
	startsign("承运人已签"),
	endsign("托运人已签"),
	payment("已支付预付款"),

	confirmpay("已结算付款"),

	refundapply("退款申请"),
	refund("已退款，交易关闭"),
	approval("已评价，交易完成");


	private String description;

	private OrderStatusCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
