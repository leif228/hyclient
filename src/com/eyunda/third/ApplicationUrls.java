package com.eyunda.third;

/**
 * 保存Url常量
 *
 */
public class ApplicationUrls {
	
	public static final String login = "/CLIENTAPI/login.php";
	public static final String testGps = "/clientapi/?Function=2&Style=json&ShipName=粤安顺621&StartTime=2016-07-26%2009:06:00&EndTime=2016-07-26%2010:28:00";
	public static final String mapPort = "/supportapi/zd_server.php?Function=database&sqlstr=";
	public static final String userPower = "/clientapi/?Function=4&Style=json";
	public static final String policeList = "/clientapi/?Function=26&StartTime=";
	public static final String shipSCGet = "/CLIENTAPI/?Function=21&Style=json";
	public static final String historyGps = "/CLIENTAPI/?Function=2&Style=json&ShipID=";
	public static final String pageHomeGps = "/clientapi/?Function=18&Style=json&GPSTime=";
	public static final String shipInfo = "/clientapi/?Function=24&Style=json&ShipID=";
	public static final String shipInfoZZ = "/clientapi/?Function=29&Style=json&ShipID=";
	public static final String shipCKHX = "/CLIENTAPI/?Function=27&Style=json&StartPort=";
	public static final String shipCurrHC = "/CLIENTAPI/?Function=25&Style=json&ShipID=";
	public static final String policeReadFlag = "/CLIENTAPI/?Function=28&nID=";
	public static final String shipSCPost = "/CLIENTAPI/?Function=20";
	public static final String loadImg = "/clientapi/?Function=3&ShipID=";
	public static final String loadImg2 = "/clientapi/?Function=30&ShipID=";
	public static final String loadSTPosition = "/clientapi/?Function=1&Style=json&ShipID=";
	public static final String api_key = "/supportapi/?Function=get_key&str=android";
	public static final String portarea = "/supportapi/zd_server.php?Function=database&sqlstr=SELECT * FROM portposition";
	public static final String loadSSDistance = "/supportapi/?Function=GetVoyageInfo&ShipID=";
//	public static final String loadImg = "/clientapi/pic.php/?ShipID=";
	
	public static final String version = "/client/push/update";
	public static final String bugUpload = "/client/push/bugUpload";
	public static final String groups = "/client/group/getMobGroups";
	public static final String currHCByShipId = "/client/route/mobCurrRoutesByShipId/?PHPSESSID=";
	public static final String excuteHC = "/client/route/mobExcuteRoutes/?PHPSESSID=";
	public static final String historyHC = "/client/route/historyRoutes/?PHPSESSID=";
	public static final String saveMobGroups = "/client/group/saveMobGroups";
	public static final String saveChannelId = "/client/push/saveChannelId";
	
	public static final String javaLogin = "/client/login/ajaxLogin";
}
