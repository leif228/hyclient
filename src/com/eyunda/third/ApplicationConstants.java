package com.eyunda.third;

/**
 * 保存全局常量
 *
 */
public class ApplicationConstants {

	public static final String Logined = "logined";
	public static final String Logined_Success = "LoginSuccess";
	public static final String SESSIONID = "sessionId";
	public static final String CONTENTMD5 = "contentMD5";
	public static final String CONTENTMD5CHANGED = "contentMD5Changed";
	public static final String SHARED_PREFERENCE_NAME = "client_preferences";
	public static final String NOTIFICATION_ICON = "NOTIFICATION_ICON";
	public static final String HTTP_PRE = "http://";

	public static final String HTTP_DOMAINNAME = ".eyd98.com";
	
//	public static final String HTTP_SERVERNAME = "www.eyd98.com";
//	public static final int HTTP_SERVERPORT = 80;
//	public static final String APPNAME = "";
	 public static final String HTTP_SERVERNAME = "192.168.1.132";
	 public static final int HTTP_SERVERPORT = 8080;
	 public static final String APPNAME = "/eyunda";

	public static final String MINA_SERVERNAME = HTTP_SERVERNAME;
	public static final int MINA_SERVERPORT = 10168;
	public static final String LOGIN_NAME = "login_name";
	public static final String TRUE_NAME = "true_name";
	public static final String NICK_NAME = "nick_name";
	public static final String IFLOGIN = "ifLogin";
	// API服务器地址
	public static final String SERVER_URL = HTTP_PRE + HTTP_SERVERNAME + ":"
			+ HTTP_SERVERPORT + APPNAME; // 服务器

	public static final String SERVER_URL_SHORT = HTTP_DOMAINNAME + ":"
			+ HTTP_SERVERPORT + APPNAME; // 服务器短域名
	// 图片地址
	public static final String IMAGE_URL = SERVER_URL
			+ "/download/imageDownload?url=";
	// 文件下载地址
	public static final String FILE_URL = SERVER_URL
			+ "/mobile/download/fileDownload?url=";
	// 语音文件下载地址
	public static final String VOICE_URL = SERVER_URL
			+ "/mobile/download/voiceDownload?url=";
	// 支付宝
	public static final String ALIPAY_PARTNER = "2088811200378589";
	public static final String ALIPAY_SELLER = "18927552166@163.com";
	public static final String ALIPAY_SELLER_KEY = "ls3oeb4lzyr1ams3cz5d521i6fx3xelp";
	// 企业私钥 ,TODO:改成服务端解密，私钥要保存在服务端，不可写在客户端
	public static final String ALIPAY_RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALgdvEVsilj2+voi61HKxoYl5MqM5YExWRZGxFqzHBPiQjKylQEZJJZJ2JcxerfL6E+vXrsR+HaHboPkqqJ/c77iIXv28WXXA4b89Bt2erDmYst+1hTsSzFddhk99H7mfAd9u6nlw+iUgsjDK3zwGy4zgNfhQ8JowyXY9A4zVcTVAgMBAAECgYBnSdc7Iz8h1bzK4xgc1KM20r9dmlmOdr7BroWtVFnmFcKJaoIJEa/i2B64AzwTLENxoLKGQ/idljC7sWbArruhYNfqzLJfvA31GVCbO1bx/qllyBArEq/vW8MBVFLDy5NA1Ad6X5PhYXRWWy5QdH3vqR+sFdjRs03ir3XNmWvYaQJBAN4ACDqgKRMEsfY1xa3T6gl38OVZnurjJ2Opg8h/H3QQNAUamWjivCYFdR5SVQYrZ/jmqPEJMOksHqWWPixARMMCQQDUUGKIaESPpmwkNnhZKM7C/diDsUYmbaoq6c6EmGVLIP/UibxXEagq/h1n52B2GSIazNZfegEHt/aFn5dayFaHAkAxkxpDVT6244aGL83cGoWZ5bHi1ZeaZm2GYPVv0YuJlJC5Tw5REMjpSLC1u7cG8KyANaomYWINv+98FMf/g76XAkBu8YpriUC6Z9Z+ndAJlx9kGU24kzv6/Lc9MWvvMctMlPPaVhcK0Hk17g/jqa5oR/3gT0V+IrzDEchKTvjCQYmlAkAh8fxB0lW168tfkOhm/2I6F1BOKhQZ1xooN8bVNaZoa778kbOUWYqzWBIq0dj9A+/8C4CIa/xMDLguRjIPr6Ve";
	// 支付宝公钥
	public static final String ALIPAY_RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	public static final String ALIPAY_NOTIFY_URL = SERVER_URL
			+ "/payment/directPayNotify";
	public static final String NOTIFY_URL_BABYIN = SERVER_URL
			+ "/payment/babyInDirectPayNotify";
	public static final String NOTIFY_URL_SHIPDUES = SERVER_URL
			+ "/payment/shipDuesDirectPayNotify";
	public static final String NOTIFY_URL_GASORDERPAY = SERVER_URL
			+ "/payment/gasOrderPayDirectPayNotify";
	//平安银行接口地址
	public static final String PINAN_BANK = "https://testebank.sdb.com.cn";
	public static final String PINAN_KP_PAPAY = PINAN_BANK + "/corporbank/khPayment_papay_extend.do";
	public static final String PINAN_KP_BIND = PINAN_BANK + "/khpayment/khPayment_bind.do";
	
	// 本地临时文件列表
	public static final String LF_AREA_NAME = "area";// 滚轮用的临时文件名称
	public static final String LF_SEARCH_CATEGORY_DLR = "searchCateDlr";// 首页分类搜索时临时文件名称-代理人
	public static final String LF_SEARCH_SHIP_LIST = "shipSort";// 首页分类搜索时临时文件名称-船列表
	
	//平安SDK
	public static final String APP_KEY = "88f7190cb48541198f5b5e3d7ff97126";
	
	
	public static final String ZDJAVA_PRE_URL = "http://120.76.194.65:80/zdmanage";
//	public static final String ZDJAVA_PRE_URL = "http://120.197.66.44:8890/zdmanage";
	
//	public static final String ZDJAVA_PRE_URL = "http://192.168.1.157:8080/zdmanage";
	
//	public static final String ZDPHP_PRE_URL = "http://120.236.139.134:8888";
	public static final String ZDPHP_PRE_URL = "http://120.76.194.65:8888";

//	public static final String HY_PRE_URL = "http://120.197.66.44:8890/hangyi";
//	public static final String HYPHP_PRE_URL = "http://192.168.1.98:8888";
//	public static final String HY_PRE_URL = "http://192.168.1.101:8080/hangyi";
	
	// 生产用ing
	public static final String HY_PRE_URL = "https://www.hx-oil.com:8444/hy-oil";
//	public static final String HY_PRE_URL = "http://192.168.1.103:8080/monitor-client-openlayers";
	// 测试用ing
//	public static final String HY_PRE_URL = "http://www.hx-oil.com:8081/hy-test";
	
	public static final String HYPHP_PRE_URL = "http://192.168.1.98:8888";
	
	public static final String PRE_URL = "http://www.hx-oil.com:8888";
	public static final String clienttype = "Android";
	
	public static final String imgCachePath = "/zd/img";
	public static final String pageHomeImgCachePath = "/zd/phimg";
	
	public static final String api_key = "api_key";
	public static final String com_baidu_lbsapi_API_KEY = "com.baidu.lbsapi.API_KEY";
	
	public static final String historyLineType = "historyLineType";
	public static final String historyLineNormal = "historyLineNormal";
	public static final String historyLinePolice = "historyLinePolice";
	
	public static final String UserPowerData_SharedPreferences = "UserPowerData";
	public static final String MapPortData_SharedPreferences = "MapPortData";
	public static final String UserInfoConfig_SharedPreferences = "UserInfoConfig";
	public static final String SearchShipData_SharedPreferences = "SearchShipData";
	public static final String ModulePowerData_SharedPreferences = "ModulePowerData";
	public static final String noUpdate_SharedPreferences = "noUpdate";
	public static final String ShipVoyageData_SharedPreferences = "ShipVoyageData";
	
	public static final String HY_noUpdate_SharedPreferences = "hynoUpdate";
	public static final String HY_SharedPreferences = "HyData";
}
