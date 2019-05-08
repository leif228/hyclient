package com.eyunda.third.domain.advert;

import java.util.Map;

public class AdvertData {
	public AdvertData(Map<String, Object> params){
		super();
		if (params != null && !params.isEmpty()) {
			this.sid = ((Double) params.get("sid")).longValue();
			this.title = (String) params.get("title");
			this.ext = (String) params.get("ext");
			this.logo = (String) params.get("logo");
			this.adtype = ((Double) params.get("adtype")).longValue();
			try{
				this.order = ((Double) params.get("order")).longValue();
			}catch(Exception e){
				this.order = 0L;
			}
		}
	}
	public Long getSid() {
		return sid;
	}
	public void setSid(Long sid) {
		this.sid = sid;
	}
	public Long getOrder() {
		return order;
	}
	public void setOrder(Long order) {
		this.order = order;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public Long getAdtype() {
		return adtype;
	}
	public void setAdtype(Long adtype) {
		this.adtype = adtype;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	private Long sid; //  资源Id
	private Long order; // 广告排序
	private String title; // 现实标题 
	private String ext; // 扩展标题
	private Long adtype; // 广告类型 1船舶，2货物，3油品
	private String logo; // 展示图片
	
}
