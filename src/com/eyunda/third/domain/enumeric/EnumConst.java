package com.eyunda.third.domain.enumeric;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnumConst {

	public enum BankCode {
		EYUNDA("EYUNDA", "平安银行见证宝", "/img/bank/eyunda.jpg"),

		ICBCB2C("102100099996", "中国工商银行", "/img/bank/zggsyh.jpg"),
		
		ABC("103100000026", "中国农业银行", "/img/bank/zgnyyh.jpg"),
		
		BOCB2C("104100000004", "中国银行", "/img/bank/zgyh.jpg"),

		CCB("105100000017", "中国建设银行", "/img/bank/zgjsyh.jpg"),

		COMM("301290000007", "交通银行", "/img/bank/jtyh.jpg"),

		CMB("308584000013", "招商银行", "/img/bank/zsyh.jpg"),

		CITIC("302100011000", "中信银行", "/img/bank/zxyh.jpg"),

		SPABANK("307584007998", "平安银行", "/img/bank/payh.jpg"),

		CIB("309391000011", "兴业银行", "/img/bank/xyyh.jpg"),

		SPDB("310290000013", "浦发银行", "/img/bank/pfyh.jpg"),

		CEBBANK("303100000006", "中国光大银行", "/img/bank/zggdyh.jpg"),

		CMBC("305100000013", "中国民生银行", "/img/bank/zgmsyh.jpg"),

		POSTGC("403100000004", "中国邮政储蓄银行", "/img/bank/zgyzcxyh.jpg"),

		BJBANK("313100000013", "北京银行", "/img/bank/bjyh.jpg"),

		SHBANK("325290000012", "上海银行", "/img/bank/shyh.jpg"),

		HXB("304100040000", "华夏银行", "/img/bank/hxyh.jpg"),

		GDB("306581000003", "广发银行", "/img/bank/gfyh.jpg");

		private String code;
		private String description;
		private String icon;

		public static BankCode getByCode(String code) {
			for (BankCode e : BankCode.values())
				if (e.getCode().equals(code))
					return e;

			return null;
		}

		public String getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}

		public String getIcon() {
			return icon;
		}

		private BankCode(String code, String description, String icon) {
			this.code = code;
			this.description = description;
			this.icon = icon;
		}
}

	public enum LoginSourceCode {
		ANDROID("0", "android"), IOS("1", "ios"), WEB("2", "web"), CLIENT("3", "client");

		private String code;
		private String description;

		public String getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}

		private LoginSourceCode(String code, String description) {
			this.code = code;
			this.description = description;
		}
	}

	public enum RecentPeriodCode {
		THREE_DAYS("0", "三日内", 3),

		ONE_WEEK("1", "一周内", 7),

		HALF_MONTH("2", "半月内", 15),

		ONE_MONTH("3", "一个月内", 30),

		TWO_MONTH("4", "二个月内", 60),

		THREE_MONTH("5", "三个月内", 90),

		HALF_YEAR("6", "半年内", 180),

		ONE_YEAR("7", "一年内", 360);

		private String code;
		private String description;
		private Integer totalDays;

		public static RecentPeriodCode getByCode(String code) {
			for (RecentPeriodCode e : RecentPeriodCode.values())
				if (e.getCode().equals(code))
					return e;

			return null;
		}

		public String getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}

		public Integer getTotalDays() {
			return totalDays;
		}

		private RecentPeriodCode(String code, String description, Integer totalDays) {
			this.code = code;
			this.description = description;
			this.totalDays = totalDays;
		}
	}

	public enum ShipSortCode {
		POINTER("1", "人气"),

		ORDER("2", "销量"),

		TIME("3", "最新"),

		PRICE("4", "报价"),

		TONS("5", "载货量");

		private String code;
		private String description;

		public static ShipSortCode getByCode(String code) {
			for (ShipSortCode e : ShipSortCode.values())
				if (e.getCode().equals(code))
					return e;

			return null;
		}

		public String getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}

		private ShipSortCode(String code, String description) {
			this.code = code;
			this.description = description;
		}
	}

	public enum BigAreaCode {

		ZHUSANJIAO("2", "珠三角", "泛珠三角流域、南海周边及港澳台地区"),

		CHANGSANJIAO("4", "长三角", "长江流域、淮河流域及京杭大运河流域"),

		CHINACOAST("6", "沿海", "中国沿海港口城市"),

		WORLDOCEAN("7", "远洋", "亚洲、美洲、欧洲、非洲及大洋洲著名港口");

		private String code;
		private String description;
		private List<PortCityCode> portCities;
		private String fullName;
		private String remark;

		public static BigAreaCode getByCode(String code) {
			for (BigAreaCode e : BigAreaCode.values())
				if (e.getCode().equals(code))
					return e;

			return null;
		}

		public String getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}

		public String getFullName() {
			return fullName;
		}

		public String getRemark() {
			return remark;
		}

		public List<PortCityCode> getPortCities() {
			return portCities;
		}

		public void setPortCities(List<PortCityCode> portCities) {
			this.portCities = portCities;
		}

		private BigAreaCode(String code, String description, String remark) {
			this.code = code;
			this.description = description;
			this.fullName = description;
			this.remark = remark;
		}
	}

	public enum PortCityCode {
		GUANGZHOU("11", "广州", BigAreaCode.ZHUSANJIAO),

		SHENZHEN("12", "深圳", BigAreaCode.ZHUSANJIAO),

		ZHUHAI("13", "珠海", BigAreaCode.ZHUSANJIAO),

		FOSHAN("14", "佛山", BigAreaCode.ZHUSANJIAO),

		DONGGUAN("15", "东莞", BigAreaCode.ZHUSANJIAO),

		ZHONGSHAN("16", "中山", BigAreaCode.ZHUSANJIAO),

		JIANGMEN("17", "江门", BigAreaCode.ZHUSANJIAO),

		ZHAOQING("18", "肇庆", BigAreaCode.ZHUSANJIAO),

		YUNFU("19", "云浮", BigAreaCode.ZHUSANJIAO),

		HUIZHOU("20", "惠州", BigAreaCode.ZHUSANJIAO),

		SANTOU("21", "汕头", BigAreaCode.ZHUSANJIAO),

		QINGUAN("22", "清远", BigAreaCode.ZHUSANJIAO),

		ZHANJIANG("23", "湛江", BigAreaCode.ZHUSANJIAO),

		FANFUJIAN("24", "福建", BigAreaCode.ZHUSANJIAO),

		FANGUANGXI("25", "广西", BigAreaCode.ZHUSANJIAO),

		FANHAINAN("26", "海南", BigAreaCode.ZHUSANJIAO),

		SHANGHAI("31", "上海", BigAreaCode.CHANGSANJIAO),

		JIANGSU("32", "江苏", BigAreaCode.CHANGSANJIAO),

		ANHUI("33", "安徽", BigAreaCode.CHANGSANJIAO),

		JIANGXI("34", "江西", BigAreaCode.CHANGSANJIAO),

		HUBEI("35", "湖北", BigAreaCode.CHANGSANJIAO),

		HUNAN("36", "湖南", BigAreaCode.CHANGSANJIAO),

		SICHUAN("37", "四川", BigAreaCode.CHANGSANJIAO),

		JINGHANG("41", "京杭运河", BigAreaCode.CHANGSANJIAO),

		HUAIHE("42", "淮河", BigAreaCode.CHANGSANJIAO),

		DONGBEI("61", "辽宁", BigAreaCode.CHINACOAST),

		HUABEI("62", "津冀", BigAreaCode.CHINACOAST),

		SHANDONG("63", "山东", BigAreaCode.CHINACOAST),

		SUZHEHU("64", "苏浙沪", BigAreaCode.CHINACOAST),

		FUJIAN("65", "福建", BigAreaCode.CHINACOAST),

		YUEGUI("66", "粤桂", BigAreaCode.CHINACOAST),

		HAINAN("67", "海南", BigAreaCode.CHINACOAST),

		TAIGANGAO("68", "香港", BigAreaCode.CHINACOAST),

		AOMEN("69", "澳门", BigAreaCode.CHINACOAST),

		ASIA("71", "亚洲", BigAreaCode.WORLDOCEAN),

		AMERICA("72", "美洲", BigAreaCode.WORLDOCEAN),

		EUROPE("73", "欧洲", BigAreaCode.WORLDOCEAN),

		AFRICA("74", "非洲", BigAreaCode.WORLDOCEAN),

		OCEANIA("75", "大洋洲", BigAreaCode.WORLDOCEAN);

		private String code;
		private String description;
		private String fullName;
		private BigAreaCode bigArea;

		public static PortCityCode getByCode(String code) {
			for (PortCityCode e : PortCityCode.values())
				if (e.getCode().equals(code))
					return e;

			return null;
		}

		public static List<PortCityCode> getPortCities(BigAreaCode bigArea) {
			List<PortCityCode> results = new ArrayList<PortCityCode>();
			for (PortCityCode e : PortCityCode.values()) {
				if (e.getBigArea() == bigArea) {
					results.add(e);
				}
			}
			return results;
		}

		public String getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}

		public String getFullName() {
			return fullName;
		}

		public BigAreaCode getBigArea() {
			return bigArea;
		}

		private PortCityCode(String code, String description, BigAreaCode bigArea) {
			this.code = code;
			this.description = description;
			this.bigArea = bigArea;
			this.fullName = bigArea.getDescription() + "." + description;
		}
	}

}
