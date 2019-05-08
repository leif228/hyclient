package com.eyunda.main.localdata;

import java.io.Serializable;

import com.ta.util.db.annotation.TAPrimaryKey;

public class UserInfo implements Serializable{
	@TAPrimaryKey(autoIncrement = true)
	int auto;
	String name;
	String pwd;
	String userHead;
	String pquestion;
	String panswer;
	String email;
	String phone;
	String userId;
	String  plugIds;
	String simCardNo; // 手机SIM卡号
	String sessionId;
	String bindingCode; // 手机绑定编码
	Boolean ifLogined;
	
	public Boolean getIfLogined() {
		return ifLogined;
	}
	public void setIfLogined(Boolean ifLogined) {
		this.ifLogined = ifLogined;
	}
	public String getSimCardNo() {
		return simCardNo;
	}
	public void setSimCardNo(String simCardNo) {
		this.simCardNo = simCardNo;
	}
	public String getBindingCode() {
		return bindingCode;
	}
	public void setBindingCode(String bindingCode) {
		this.bindingCode = bindingCode;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getPlugIds() {
		return plugIds;
	}
	public void setPlugIds(String plugIds) {
		this.plugIds = plugIds;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getAuto() {
		return auto;
	}
	public void setAuto(int auto) {
		this.auto = auto;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getUserHead() {
		return userHead;
	}
	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}
	public String getPquestion() {
		return pquestion;
	}
	public void setPquestion(String pquestion) {
		this.pquestion = pquestion;
	}
	public String getPanswer() {
		return panswer;
	}
	public void setPanswer(String panswer) {
		this.panswer = panswer;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
