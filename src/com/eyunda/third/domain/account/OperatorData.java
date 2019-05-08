package com.eyunda.third.domain.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.ApplyStatusCode;

public class OperatorData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID
	private Long userId = 0L; // 用户ID
	private UserData userData = null;
	private String legalPerson = ""; // 法人代表

	private String idCardFront = ""; // 身份证正面
	private String idCardBack = ""; // 身份证反面
	private String busiLicence = ""; // 营业执照
	private String taxLicence = ""; // 税务登记证

	private ApplyStatusCode status = ApplyStatusCode.apply; // 状态
	private String applyTimeSt = ""; // 申请时间

	private Long shipNumber = 0L; // 代理船舶数
	private Long orderNumber = 0L; // 代理合同数
	
	private List<UserData> handlerDatas = new ArrayList<UserData>();

	@SuppressWarnings("unchecked")
	public OperatorData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.userData = new UserData((Map<String, Object>) params.get("userData"));

			this.id = ((Double) params.get("id")).longValue();
			this.userId = ((Double) params.get("userId")).longValue();
			this.legalPerson = (String) params.get("legalPerson");

			this.idCardFront = (String) params.get("idCardFront");
			this.idCardBack = (String) params.get("idCardBack");
			this.busiLicence = (String) params.get("busiLicence");
			this.taxLicence = (String) params.get("taxLicence");

			this.status = ApplyStatusCode.valueOf((String) params.get("status"));
			this.applyTimeSt = (String) params.get("applyTimeSt");

			this.shipNumber = ((Double) params.get("shipNumber")).longValue();
			this.orderNumber = ((Double) params.get("orderNumber")).longValue();
			
			if (true) {
				List<UserData> datas = new ArrayList<UserData>();
				if (params.get("handlerDatas") != null) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("handlerDatas");
					if (list != null && !list.isEmpty())
						for (Map<String, Object> map : list) {
							UserData data = new UserData(map);
							datas.add(data);
						}
				}
				this.handlerDatas = datas;
			}
		}
	}

	public OperatorData() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getIdCardFront() {
		return idCardFront;
	}

	public void setIdCardFront(String idCardFront) {
		this.idCardFront = idCardFront;
	}

	public String getIdCardBack() {
		return idCardBack;
	}

	public void setIdCardBack(String idCardBack) {
		this.idCardBack = idCardBack;
	}

	public String getBusiLicence() {
		return busiLicence;
	}

	public void setBusiLicence(String busiLicence) {
		this.busiLicence = busiLicence;
	}

	public String getTaxLicence() {
		return taxLicence;
	}

	public void setTaxLicence(String taxLicence) {
		this.taxLicence = taxLicence;
	}

	public ApplyStatusCode getStatus() {
		return status;
	}

	public void setStatus(ApplyStatusCode status) {
		this.status = status;
	}

	public String getApplyTimeSt() {
		return applyTimeSt;
	}

	public void setApplyTimeSt(String applyTimeSt) {
		this.applyTimeSt = applyTimeSt;
	}

	public Long getShipNumber() {
		return shipNumber;
	}

	public void setShipNumber(Long shipNumber) {
		this.shipNumber = shipNumber;
	}

	public Long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public List<UserData> getHandlerDatas() {
		return handlerDatas;
	}

	public void setHandlerDatas(List<UserData> handlerDatas) {
		this.handlerDatas = handlerDatas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
