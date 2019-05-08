package com.eyunda.third.domain.account;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.PayStyleCode;
import com.eyunda.third.domain.enumeric.YesNoCode;

public class AccountData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID
	private Long userId = 0L; // 用户ID
	private String accounter = ""; // 开户人
	private PayStyleCode payStyle = PayStyleCode.alipay; // 支付方式
	private String accountNo = ""; // 账号
	private YesNoCode payPassWord = YesNoCode.no; // 设置支付密码
	private String idCode = ""; // 身份证号码
	private String mobile = ""; // 平安绑定用手机

	public AccountData(Map<String, Object> params) {
		super();
		if(params != null && !params.isEmpty()){
			this.id = ((Double) params.get("id")).longValue();
			this.userId = ((Double) params.get("userId")).longValue();
			this.accounter = (String) params.get("accounter");
			this.payStyle = PayStyleCode.valueOf((String) params.get("payStyle"));
			this.accountNo = (String) params.get("accountNo");
			this.payPassWord = YesNoCode.valueOf((String) params.get("payPassWord"));
			this.idCode = (String) params.get("idCode");
			this.mobile = (String) params.get("mobile");
		}
	}

	public AccountData() {
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

	public String getAccounter() {
		return accounter;
	}

	public void setAccounter(String accounter) {
		this.accounter = accounter;
	}

	public PayStyleCode getPayStyle() {
		return payStyle;
	}

	public void setPayStyle(PayStyleCode payStyle) {
		this.payStyle = payStyle;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public YesNoCode getPayPassWord() {
		return payPassWord;
	}

	public void setPayPassWord(YesNoCode payPassWord) {
		this.payPassWord = payPassWord;
	}

	public String getIdCode() {
		return idCode;
	}

	public void setIdCode(String idCode) {
		this.idCode = idCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
