package com.eyunda.third.domain.enumeric;

public enum RefundStatusCode {
	WAIT_SELLER_AGREE("退款协议等待卖家确认中"),
	SELLER_REFUSE_BUYER("卖家不同意协议，等待买家修改"),
	WAIT_BUYER_RETURN_GOODS("退款协议达成，等待买家退货"),
	WAIT_SELLER_CONFIRM_GOODS("等待卖家收货"),
	REFUND_SUCCESS("退款成功"),
	REFUND_CLOSED("退款关闭");

	private String description;

	private RefundStatusCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
