package com.eyunda.third.domain.order;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.GiroTypeCode;
import com.eyunda.third.domain.enumeric.UserRoleCode;

public class BalanceBabyData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // 余额宝id
	private Long userId = 0L; // 用户ID
	private Long orderId = 0L; // 合同id
	private Long settleId = 0L; // 帐务id
	private UserRoleCode roleCode = null; // 角色
	private String createTime = ""; // 创建时间
	private GiroTypeCode giroType = null; // 转帐类型
	private Double money = 0.00D; // 金额
	private Double balance = 0.00D; // 余额
	private String remark = ""; // 备注

	public BalanceBabyData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.userId = ((Double) params.get("userId")).longValue();
			this.orderId = ((Double) params.get("orderId")).longValue();
			this.settleId = ((Double) params.get("settleId")).longValue();
			this.money = (Double) params.get("money");
			this.balance = (Double) params.get("balance");
			this.roleCode = UserRoleCode.valueOf((String) params.get("roleCode"));
			this.giroType = GiroTypeCode.valueOf((String) params
					.get("giroType"));
			this.createTime = (String) params.get("createTime");
			this.remark = (String) params.get("remark");
		}
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getSettleId() {
		return settleId;
	}

	public void setSettleId(Long settleId) {
		this.settleId = settleId;
	}

	/**
	 * 取得角色
	 */
	public UserRoleCode getRoleCode() {
		return roleCode;
	}

	/**
	 * 设置角色
	 */
	public void setRoleCode(UserRoleCode roleCode) {
		this.roleCode = roleCode;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * 取得转帐类型
	 */
	public GiroTypeCode getGiroType() {
		return giroType;
	}

	/**
	 * 设置转帐类型
	 */
	public void setGiroType(GiroTypeCode giroType) {
		this.giroType = giroType;
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

	/**
	 * 取得余额
	 */
	public Double getBalance() {
		return balance;
	}

	/**
	 * 设置余额
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
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
