package com.eyunda.third.domain.ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.AttrTypeCode;

public class AttrNameData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID
	private String attrNameCode = ""; // 属性名编码
	private String attrName = ""; // 属性名描述
	private AttrTypeCode attrType = null; // 属性类型

	private String attrValues = "";

	private TypeData typeData = null; // 类别
	private List<AttrValueData> attrValueDatas = new ArrayList<AttrValueData>();
	private ShipAttrData currAttrValue = null;

	@SuppressWarnings("unchecked")
	public AttrNameData(Map<String, Object> params) {
		super();
		if(params != null && !params.isEmpty()){
			this.id = ((Double) params.get("id")).longValue();
			this.attrNameCode = (String) params.get("attrNameCode");
			this.attrName = (String) params.get("attrName");
			this.attrType = AttrTypeCode.valueOf((String) params.get("attrType"));
			this.attrValues = (String) params.get("attrValues");
			this.typeData = new TypeData((Map<String, Object>) params.get("typeData"));
	
			List<AttrValueData> datas = new ArrayList<AttrValueData>();
			List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("attrValueDatas");
			if (list != null && !list.isEmpty())
				for (Map<String, Object> map : list) {
					AttrValueData data = new AttrValueData(map);
					datas.add(data);
				}
			this.attrValueDatas = datas;
	
			this.currAttrValue = new ShipAttrData(((Map<String, Object>) params.get("currAttrValue")));
		
		}
	}

	public AttrNameData() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAttrNameCode() {
		return attrNameCode;
	}

	public void setAttrNameCode(String attrNameCode) {
		this.attrNameCode = attrNameCode;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public TypeData getTypeData() {
		return typeData;
	}

	public void setTypeData(TypeData typeData) {
		this.typeData = typeData;
	}

	public List<AttrValueData> getAttrValueDatas() {
		return attrValueDatas;
	}

	public void setAttrValueDatas(List<AttrValueData> attrValueDatas) {
		this.attrValueDatas = attrValueDatas;
	}

	public AttrTypeCode getAttrType() {
		return attrType;
	}

	public void setAttrType(AttrTypeCode attrType) {
		this.attrType = attrType;
	}

	public String getAttrValues() {
		return attrValues;
	}

	public void setAttrValues(String attrValues) {
		this.attrValues = attrValues;
	}

	public ShipAttrData getCurrAttrValue() {
		return currAttrValue;
	}

	public void setCurrAttrValue(ShipAttrData currAttrValue) {
		this.currAttrValue = currAttrValue;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "AttrNameData [id=" + id + ", attrNameCode=" + attrNameCode
				+ ", attrName=" + attrName + ", attrType=" + attrType
				+ ", attrValues=" + attrValues + ", typeData=" + typeData
				+ ", attrValueDatas=" + attrValueDatas + ", currAttrValue="
				+ currAttrValue + "]";
	}

}
