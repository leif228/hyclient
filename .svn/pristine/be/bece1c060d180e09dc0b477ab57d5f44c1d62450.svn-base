package com.eyunda.third.domain.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.account.OperatorData;
import com.eyunda.third.domain.enumeric.ReleaseStatusCode;
import com.eyunda.third.domain.ship.TypeData;

public class TemplateData extends BaseData {
	private static final long serialVersionUID = 1L;

	private Long id = 0L;
	private String typeCode = ""; // 船舶类别编码
	private TypeData typeData = null; // 船舶类别
	private String title = ""; // 模板标题
	private Long operatorId = 0L;// 代理人ID
	private OperatorData operatorData = null; // 代理人
	private String createTime = ""; // 建立时间
	private String releaseTime = ""; // 发布时间
	private ReleaseStatusCode releaseStatus = null; // 发布状态

	private List<TempItemData> tempItemDatas = null;

	@SuppressWarnings("unchecked")
	public TemplateData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.typeCode = (String) params.get("typeCode");
			this.typeData = new TypeData(
					(Map<String, Object>) params.get("typeData"));
			this.title = (String) params.get("title");
			this.operatorId = ((Double) params.get("operatorId")).longValue();
			this.createTime = (String) params.get("createTime");
			this.releaseTime = (String) params.get("releaseTime");
			this.releaseStatus = ReleaseStatusCode.valueOf((String) params
					.get("releaseStatus"));

			List<TempItemData> datas = new ArrayList<TempItemData>();
			List<Map<String, Object>> list = (List<Map<String, Object>>) params
					.get("tempItemDatas");
			if (list != null && !list.isEmpty())
				for (Map<String, Object> map : list) {
					TempItemData data = new TempItemData(map);
					datas.add(data);
				}
			this.tempItemDatas = datas;
		}
	}

	public TemplateData() {
		super();
	}

	public TemplateData(Long id, String title, String des) {
		super();
		this.id = id;
		this.title = title;
		this.typeCode = des;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TypeData getTypeData() {
		return typeData;
	}

	public void setTypeData(TypeData typeData) {
		this.typeData = typeData;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public ReleaseStatusCode getReleaseStatus() {
		return releaseStatus;
	}

	public void setReleaseStatus(ReleaseStatusCode releaseStatus) {
		this.releaseStatus = releaseStatus;
	}

	public String getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public List<TempItemData> getTempItemDatas() {
		return tempItemDatas;
	}

	public void setTempItemDatas(List<TempItemData> tempItemDatas) {
		this.tempItemDatas = tempItemDatas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return title;
	}

}
