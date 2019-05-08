package com.eyunda.third.domain.enumeric;

public enum PlantBank {
	ICBCD("工商银行-借记卡", "ICBC-D"),
	ICBCC( "工商银行-贷记卡", "ICBC-C"),
	CEBD("光大银行-借记卡", "CEB-D"),	
	CEBC("光大银行-贷记卡", "CEB-C"),
	HXBD("华夏银行-借记卡", "HXB-D"),
	HXBC("华夏银行-贷记卡", "HXB-C"),
	CCBD("建设银行-借记卡", "CCB-D"),
	CCBC("建设银行-贷记卡", "CCB-C"),
	BOCOMD("交通银行-借记卡", "BOCOM-D"),
	CMBCD("民生银行-借记卡", "CMBC-D"),
	CMBCC("民生银行-贷记卡", "CMBC-C"),
	ABCD("农业银行-借记卡", "ABC-D"),
	ABCC("农业银行-贷记卡", "ABC-C"),
	PABD("平安银行-借记卡", "PAB-D"),
	PABC("平安银行-贷记卡", "PAB-C"),
	SPDBD("浦发银行-借记卡", "SPDB-D"),
	SPDBC("浦发银行-贷记卡", "SPDB-C"),
	BOSD("上海银行-借记卡", "BOS-D"),
	BOSC("上海银行-贷记卡", "BOS-C"),
	CIBD("兴业银行-借记卡", "CIB-D"),
	CIBC("兴业银行-贷记卡", "CIB-C"),
	BOCD("中国银行-借记卡", "BOC-D"),
	BOCC("中国银行-贷记卡", "BOC-C"),
	PSBCD("中国邮政储蓄银行-借记卡", "PSBC-D"),
	CNCBD("中信银行-借记卡", "CNCB-D"),
	CNCBC("中信银行-贷记卡", "CNCB-C"),
	GDRCUD("广东农村信用社-借记卡", "GDRCU-D"),
	HZCBD("杭州银行-借记卡", "HZCB-D"),
	HZCBC("杭州银行-贷记卡", "HZCB-C"),
	JSBKD("江苏银行-借记卡", "JSBK-D"),
	JSBKC("江苏银行-贷记卡", "JSBK-C"),
	SZRCUD("深圳农村商业银行-借记卡", "SZRCU-D"),
	CBHBD("渤海银行-借记卡", "CBHB-D"),
	CBHBC("渤海银行-贷记卡", "CBHB-C"),
	BCCBC("北京银行-贷记卡", "BCCB-C"),
	GDBC("广发银行-贷记卡", "GDB-C"),
	HSCBC("徽商银行-贷记卡", "HSCB-C"),
	SRCBC("上海农商行-贷记卡", "SRCB-C"),
	BRCBC("北京农商行-贷记卡", "BRCB-C"),
	CQCBANKC("重庆银行-贷记卡", "CQCBANK-C");
	
	private String code;
	private String description;
	private PlantBank(String description, String code){
		this.code = code;
		this.description = description;
	}
	
	public String getCode(){
		return code;
	}
	
	public String getDescription(){
		return description;
	}
}