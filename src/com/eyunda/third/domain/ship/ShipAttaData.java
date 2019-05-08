package com.eyunda.third.domain.ship;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.ImgTypeCode;
import com.eyunda.tools.StringUtil;

public class ShipAttaData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = null; // ID
	private Long shipId = 0L; // 船舶ID
	private String title = ""; // 图片标题
	private String titleDes = ""; // 图片标题
	private Integer no = 0; // 顺序号
	private ImgTypeCode imgType = null; // 图片类型
	private String url = ""; // 路径
	private String createTime = ""; // 上传时间
	private Long size = 0L; // 文件大小

	public ShipAttaData(Map<String, Object> params) {
		super();
		if(params != null && !params.isEmpty()){
			this.id = ((Double) params.get("id")).longValue();
			this.shipId = ((Double) params.get("shipId")).longValue();
			this.title = (String) params.get("title");
			this.titleDes = (String) params.get("titleDes");
			this.no = ((Double) params.get("no")).intValue();
			this.url = (String) params.get("url");
			this.createTime = (String) params.get("createTime");
			this.size = ((Double) params.get("size")).longValue();
			if(params.get("imgType")!="")  
			return;
			this.imgType = ImgTypeCode.valueOf((String) params.get("imgType"));
		}
	}

	public ShipAttaData() {
		super();
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public ImgTypeCode getImgType() {
		return imgType;
	}

	public void setImgType(ImgTypeCode imgType) {
		this.imgType = imgType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShipId() {
		return shipId;
	}

	public void setShipId(Long shipId) {
		this.shipId = shipId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		this.titleDes = StringUtil.formatHTML(title);
	}

	public String getTitleDes() {
		return titleDes;
	}

	public void setTitleDes(String titleDes) {
		this.titleDes = titleDes;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ShipAttaData [id=" + id + ", shipId=" + shipId + ", title="
				+ title + ", titleDes=" + titleDes + ", no=" + no
				+ ", imgType=" + imgType + ", url=" + url + ", createTime="
				+ createTime + ", size=" + size + "]";
	}

}
