package com.eyunda.third;

public enum NeedSessionIdCode {

	MyOrderActivity("合同列表", "com.eyunda.third.activities.order.MyOrderActivity", "com.eyunda.third.activities.order.MyOrderActivity"),

	AddOrderActivity("新增合同-查找托运人", "com.eyunda.third.activities.order.AddOrderActivity", "com.eyunda.third.activities.order.MyOrderActivity"),

	AddOrderCCActivity("新增合同-查找运输工具", "com.eyunda.third.activities.order.AddOrderCCActivity", "com.eyunda.third.activities.order.MyOrderActivity"),

	AddOrderGSHActivity("新增合同-新增干散货", "com.eyunda.third.activities.order.AddOrderGSHActivity", "com.eyunda.third.activities.order.MyOrderActivity"),

	AddOrderJSActivity("新增合同-结算约定", "com.eyunda.third.activities.order.AddOrderJSActivity", "com.eyunda.third.activities.order.MyOrderActivity"),

	AddOrderJZXActivity("新增合同-新增集装箱", "com.eyunda.third.activities.order.AddOrderJZXActivity", "com.eyunda.third.activities.order.MyOrderActivity"),

	AddOrderOneGSHActivity("新增合同-新增一条干散货", "com.eyunda.third.activities.order.AddOrderOneGSHActivity", "com.eyunda.third.activities.order.MyOrderActivity"),

	AddOrderOneJZXActivity("新增合同-新增一条集装箱", "com.eyunda.third.activities.order.AddOrderOneJZXActivity", "com.eyunda.third.activities.order.MyOrderActivity"),

	AddOrderOneTemplateActivity("新增合同-新增一条合同条款", "com.eyunda.third.activities.order.AddOrderOneTemplateActivity", "com.eyunda.third.activities.order.MyOrderActivity"),

	AddOrderTKActivity("编辑合同-合同条款", "com.eyunda.third.activities.order.AddOrderTKActivity", "com.eyunda.third.activities.order.MyOrderActivity"),

	AddOrderTYRActivity("新增合同-查找托运人", "com.eyunda.third.activities.order.AddOrderTYRActivity", "com.eyunda.third.activities.order.MyOrderActivity"),

	AddOrderZXActivity("新增合同-装卸约定", "com.eyunda.third.activities.order.AddOrderZXActivity", "com.eyunda.third.activities.order.MyOrderActivity");

	private String description;
	private String classPath;
	private String gotoPath;

	public String getDescription() {
		return description;
	}

	public String getClassPath() {
		return classPath;
	}

	public String getGotoPath() {
		return gotoPath;
	}

	public static NeedSessionIdCode getByClassPath(String classPath) {
		for (NeedSessionIdCode e : NeedSessionIdCode.values())
			if (e.getClassPath().equals(classPath))
				return e;

		return null;
	}

	private NeedSessionIdCode(String description, String classPath, String gotoPath) {
		this.description = description;
		this.classPath = classPath;
		this.gotoPath = gotoPath;
	}

}
