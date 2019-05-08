package com.eyunda.third.locatedb;


public class NeedCacheUtils {
	public enum NeedCacheUrls {
		
//		url1("/mobile/login/autoLogin"),
		url2("/mobile/chat/getAllCRS"),
		url3("/mobile/chat/chatMsgs"),
		url4("/mobile/message/myMessage"),
		url5("/mobile/comm/getPorts"),
		url6("/mobile/home/company/list"),
		url7("/mobile/home/sortList/show"),
		url8("/mobile/contact/myContact"),
		url9("/mobile/home/logoAdvert"),
		url10("/mobile/account/myAccount"),
		url11("/mobile/account/myChild"),
		url12("/mobile/ship/myAllShip"),
		url13("/mobile/cargo/myCargo"),
		url14("/mobile/state/shipApply/shipArvlft"),
		url15("/mobile/monitor/historyRoutes"),
		url16("/mobile/order/myOrder"),
		url17("/mobile/settle/mySettle"),
		url18("/mobile/home/logoAdvert"),
		url19("/mobile/home/index"),
		url20("/mobile/order/myOrder/edit"),
		url21("/mobile/home/operatorList"),
		url22("/mobile/home/sortCargoList"),
		url23("/mobile/home/shipSailLineList"),
		url24("/mobile/home/shipDynamic"),
		url25("/mobile/home/cargoList"),
		url26("/mobile/home/shipOperatorList"),
		url27("/mobile/state/shipApply/shipPrice"),
		url28("/mobile/cargo/AllCargos"),
		url29("/mobile/home/recentCargos"),
		url30("/mobile/home/operDetail"),
		url31("/mobile/home/cargoAgent"),
		url32("/mobile/home/cargoHome"),
		url34("/mobile/ship/myShip/getShipDelivery"),
		url35("/mobile/state/myShip"),
		url36("/mobile/login/getregister"),
		url37("/mobile/account/myAccount/getpca"),
		url38("/mobile/monitor/myAllShip"),
		url40("/mobile/ship/myShip/dues"),
		url41("/mobile/ship/myShip/gasOrder"),
		url42("/mobile/wallet/myWallet"),
		url43("/mobile/wallet/myWallet/bindCardInit");
		private String description;

		private NeedCacheUrls(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}
	}
	
	public static boolean isNeedCacheUrl(String url){
		NeedCacheUrls[] urls = NeedCacheUrls.values();
		for(NeedCacheUrls ncu:urls){
			if(url.equals(ncu.getDescription()))
				return true;
		}
		return false;
	}
}
