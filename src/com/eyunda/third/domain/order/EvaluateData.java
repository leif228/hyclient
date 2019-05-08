package com.eyunda.third.domain.order;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.EvalTypeCode;

public class EvaluateData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID

	private Long shipId = 0L; // 船舶ID

	private Long userId = 0L; // 评价用户ID

	private UserData userData = null;

	private EvalTypeCode evalType = null; // 评价类型

	private String evalContent = ""; // 评价内容

	private String createTime = ""; // 评价时间

	@SuppressWarnings("unchecked")
	public EvaluateData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.shipId = ((Double) params.get("shipId")).longValue();
			this.userId = ((Double) params.get("userId")).longValue();
			this.userData = new UserData(
					(Map<String, Object>) params.get("userData"));
			this.evalType = EvalTypeCode.valueOf((String) params
					.get("evalType"));
			this.evalContent = (String) params.get("evalContent");
			this.createTime = (String) params.get("createTime");
		}
	}

	public EvaluateData() {
		super();
	}

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShipId() {
		return shipId;
	}

	public void setShipId(Long shipId) {
		this.shipId = shipId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public EvalTypeCode getEvalType() {
		return evalType;
	}

	public void setEvalType(EvalTypeCode evalType) {
		this.evalType = evalType;
	}

	public String getEvalContent() {
		return evalContent;
	}

	public void setEvalContent(String evalContent) {
		this.evalContent = evalContent;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
