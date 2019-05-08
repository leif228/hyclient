package com.eyunda.third.domain.wallet;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.EnumConst.BankCode;
import com.eyunda.third.domain.enumeric.YesNoCode;

public class UserBankData extends BaseData {

	private static final long serialVersionUID = -1L;

	private Long id = 0L;
	private Long userId = 0L;
	private String accountName = "";
	private String cardNo = "";
	private BankCode bankCode = BankCode.EYUNDA;
	private String bindTime = ""; // 绑定时间
	private YesNoCode isRcvCard = YesNoCode.no;
	
	private String bindId = ""; // 平安银行绑定id
	private String bankName = "";
	private String telephone = ""; // 银行预留手机号

	public UserBankData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.userId = ((Double) params.get("userId")).longValue();
			this.accountName = (String) params.get("accountName");
			this.cardNo = (String) params.get("cardNo");
			this.bindId = (String) params.get("bindId");
			this.bankName = (String) params.get("bankName");
			this.telephone = (String) params.get("telephone");
			
			String bankCode = (String) params.get("bankCode");
			if ((bankCode != null) && (!bankCode.equals(""))) {
				this.bankCode = BankCode.valueOf(bankCode);
			}
			
			this.bindTime = (String) params.get("bindTime");
			
			String isRcvCard = (String) params.get("isRcvCard");
			if ((isRcvCard != null) && (!isRcvCard.equals(""))) {
				this.isRcvCard = YesNoCode.valueOf(isRcvCard);
			}
		}
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

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public BankCode getBankCode() {
		return bankCode;
	}

	public void setBankCode(BankCode bankCode) {
		this.bankCode = bankCode;
	}

	public String getBindTime() {
		return bindTime;
	}

	public void setBindTime(String bindTime) {
		this.bindTime = bindTime;
	}

	public YesNoCode getIsRcvCard() {
		return isRcvCard;
	}

	public void setIsRcvCard(YesNoCode isRcvCard) {
		this.isRcvCard = isRcvCard;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
}
