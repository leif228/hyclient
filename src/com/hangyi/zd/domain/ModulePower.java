package com.hangyi.zd.domain;

import com.eyunda.third.domain.BaseData;

/*这个是在获取权限时，json里面新添加功能模块授权的内容，分别是：
（1）历史回放（2）多组镜头（3）航次管理（4）航线-按码头（5）航线-按船公司
（6）航线-按客户（7）报警设置（8）报警信息（9）驳船统计*/
public class ModulePower extends BaseData {

	private static final long serialVersionUID = -1L;
	private boolean PlayBack = true;
	private boolean MultiCamera = true;
	private boolean VoyageManage = true;
	private boolean ByPort = true;
	private boolean ByShipOwner = true;
	private boolean ByCustomer = true;
	private boolean AlarmSetting = true;
	private boolean AlarmMessage = true;
	private boolean ShipCount = true;

	public ModulePower() {
		super();
	}

	public boolean isPlayBack() {
		return PlayBack;
	}

	public void setPlayBack(boolean playBack) {
		PlayBack = playBack;
	}

	public boolean isMultiCamera() {
		return MultiCamera;
	}

	public void setMultiCamera(boolean multiCamera) {
		MultiCamera = multiCamera;
	}

	public boolean isVoyageManage() {
		return VoyageManage;
	}

	public void setVoyageManage(boolean voyageManage) {
		VoyageManage = voyageManage;
	}

	public boolean isByPort() {
		return ByPort;
	}

	public void setByPort(boolean byPort) {
		ByPort = byPort;
	}

	public boolean isByShipOwner() {
		return ByShipOwner;
	}

	public void setByShipOwner(boolean byShipOwner) {
		ByShipOwner = byShipOwner;
	}

	public boolean isByCustomer() {
		return ByCustomer;
	}

	public void setByCustomer(boolean byCustomer) {
		ByCustomer = byCustomer;
	}

	public boolean isAlarmSetting() {
		return AlarmSetting;
	}

	public void setAlarmSetting(boolean alarmSetting) {
		AlarmSetting = alarmSetting;
	}

	public boolean isAlarmMessage() {
		return AlarmMessage;
	}

	public void setAlarmMessage(boolean alarmMessage) {
		AlarmMessage = alarmMessage;
	}

	public boolean isShipCount() {
		return ShipCount;
	}

	public void setShipCount(boolean shipCount) {
		ShipCount = shipCount;
	}

}
