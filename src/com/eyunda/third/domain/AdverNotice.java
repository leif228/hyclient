package com.eyunda.third.domain;

public class AdverNotice {
	private String title;
	private String tag;

	public AdverNotice(String title, String tag) {
		super();
		this.title = title;
		this.tag = tag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
