package com.eyunda.third.domain.enumeric;

public enum ApplyReplyCode {
	noapply("未申请", "不退款"),
	apply("申请", "申请退款"),
	reply("同意", "已退款"),
	noreply("拒绝", "退款失败");

	private String description;
	private String remark;

	private ApplyReplyCode(String description, String remark) {
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
