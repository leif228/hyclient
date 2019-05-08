package com.eyunda.third.domain.wallet;

import java.util.Map;

import com.eyunda.third.domain.BaseData;

public class PubPayNode extends BaseData {
	private static final long serialVersionUID = -1L;
	
	private Long id = 0L;
	private String nodeCode = ""; // 省份编码
	private String nodeName = ""; // 省份名称

	public PubPayNode(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.nodeCode = (String) params.get("nodeCode");
			this.nodeName = (String) params.get("nodeName");
		}
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	@Override
	public String toString() {
		return nodeName;
	}


}
