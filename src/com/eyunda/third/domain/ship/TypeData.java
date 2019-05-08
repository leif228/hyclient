package com.eyunda.third.domain.ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eyunda.third.domain.BaseData;

public class TypeData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID

	private String typeCode = ""; // 类别编码

	private String typeName = ""; // 类别名称

	private TypeData parent = null; // 上级类别编码

	private List<TypeData> childrenDatas = new ArrayList<TypeData>(); // 下级类别名称

	@SuppressWarnings("unchecked")
	public TypeData(Map<String, Object> params) {
		super();
		if(params != null && !params.isEmpty()){
			this.id = ((Double) params.get("id")).longValue();
			this.typeCode = (String) params.get("typeCode");
			this.typeName = (String) params.get("typeName");
			this.parent = new TypeData((Map<String, Object>) params.get("parent"));
	
			List<TypeData> datas = new ArrayList<TypeData>();
			List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("childrenDatas");
			if (list != null && !list.isEmpty())
				for (Map<String, Object> map : list) {
					TypeData data = new TypeData(map);
					datas.add(data);
				}
			this.childrenDatas = datas;
		}
	}

	public TypeData() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public TypeData getParent() {
		return parent;
	}

	public void setParent(TypeData parent) {
		this.parent = parent;
	}

	public List<TypeData> getChildrenDatas() {
		return childrenDatas;
	}

	public void setChildrenDatas(List<TypeData> childrenDatas) {
		this.childrenDatas = childrenDatas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "TypeData [id=" + id + ", typeCode=" + typeCode + ", typeName="
				+ typeName + ", parent=" + parent + ", childrenDatas="
				+ childrenDatas + "]";
	}

}
