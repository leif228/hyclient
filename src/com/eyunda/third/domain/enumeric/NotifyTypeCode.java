package com.eyunda.third.domain.enumeric;

public enum NotifyTypeCode {
	edit("编辑"),
	startsign("承运人已签"),
	endsign("托运人已签"),
	prepay("已支付预付款"),
	preupcargo("已准备装货"),
	upcargo("已确认装货"),	
	endpay("已结算付款"),
	predowncargo("已准备卸货"),
	downcargo("交易完成"), // 已确认卸货
	refund("已退款"),
	approval("已评价"),
	close("交易关闭"),
	
	cargonotify("货物"),
	allfriends("会话群发"),
	allowner("货主群发"),
	allmaster("船东群发"),
	allchildren("子帐号群发"),
	friend("好友");

	private String description;

	private NotifyTypeCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
