package com.eyunda.third.domain.ship;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.ComboCode;
import com.eyunda.third.domain.enumeric.YesNoCode;

public class MyShipDuesData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L;
	private Long userId = 0L; // 用户ID
	private Long shipId = 0L; // 船舶ID
	private String shipName = "";
	private ComboCode combo = null; // 套餐
	private String startMonth = ""; // 开始年月
	private String endMonth = ""; // 结束年月
	private Double money = 0.00D; // 金额
	private String createTime = ""; // 创建时间
	private YesNoCode status = null; // 状态
	private String remark = ""; // 备注
	private String refundAction = ""; // 退款按钮 "yes" or "no"

	public MyShipDuesData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.userId = ((Double) params.get("userId")).longValue();
			this.shipId = ((Double) params.get("shipId")).longValue();
			this.shipName = (String) params.get("shipName");
			this.startMonth = (String) params.get("startMonth");
			this.endMonth = (String) params.get("endMonth");
			this.money = (Double) params.get("money");
			this.createTime = (String) params.get("createTime");
			this.remark = (String) params.get("remark");
			this.refundAction = (String) params.get("refundAction");

			String combo = (String) params.get("combo");
			if ((combo != null) && (!combo.equals(""))) {
				this.combo = ComboCode.valueOf(combo);
			}
			String status = (String) params.get("status");
			if ((status != null) && (!status.equals(""))) {
				this.status = YesNoCode.valueOf(status);
			}
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 取得用户ID
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * 设置用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public String getRefundAction() {
		return refundAction;
	}

	public void setRefundAction(String refundAction) {
		this.refundAction = refundAction;
	}

	/**
	 * 取得船舶ID
	 */
	public Long getShipId() {
		return shipId;
	}

	/**
	 * 设置船舶ID
	 */
	public void setShipId(Long shipId) {
		this.shipId = shipId;
	}

	/**
	 * 取得套餐
	 */
	public ComboCode getCombo() {
		return combo;
	}

	/**
	 * 设置套餐
	 */
	public void setCombo(ComboCode combo) {
		this.combo = combo;
	}

	/**
	 * 取得开始年月
	 */
	public String getStartMonth() {
		return startMonth;
	}

	/**
	 * 设置开始年月
	 */
	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}

	/**
	 * 取得结束年月
	 */
	public String getEndMonth() {
		return endMonth;
	}

	/**
	 * 设置结束年月
	 */
	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}

	/**
	 * 取得金额
	 */
	public Double getMoney() {
		return money;
	}

	/**
	 * 设置金额
	 */
	public void setMoney(Double money) {
		this.money = money;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * 取得状态
	 */
	public YesNoCode getStatus() {
		return status;
	}

	/**
	 * 设置状态
	 */
	public void setStatus(YesNoCode status) {
		this.status = status;
	}

	/**
	 * 取得备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
