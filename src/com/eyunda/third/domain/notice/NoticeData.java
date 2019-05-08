package com.eyunda.third.domain.notice;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.NoticeTopCode;
import com.eyunda.third.domain.enumeric.NtcColumnCode;
import com.eyunda.third.domain.enumeric.ReleaseStatusCode;

public class NoticeData extends BaseData {
	private static final long serialVersionUID = -1L;
	
	private Long id = 0L; // ID
	private NtcColumnCode ntcColumn = null; // 栏目
	private Long pointNum = 0L; // 点击数
	private String title = ""; // 标题
	private String content = ""; // 内容
	private String createTime = ""; // 建立时间
	private String publishTime = ""; // 发布时间
	private ReleaseStatusCode releaseStatus = null; // 发布状态
	private String source = ""; // 图片地址
	private NoticeTopCode top = NoticeTopCode.no; // 是否置顶
	
	
	public NoticeData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.pointNum = ((Double) params.get("pointNum")).longValue();
			this.title = (String)params.get("title");
			this.content = (String)params.get("content");
			this.createTime = (String)params.get("createTime");
			this.publishTime = (String)params.get("publishTime");
			this.source = (String)params.get("source");
			
			String ntcColumnStr = (String) params.get("ntcColumn");
			if ((ntcColumnStr != null) && (!ntcColumnStr.equals(""))) {
				this.ntcColumn = NtcColumnCode.valueOf(ntcColumnStr);
			}
			String releaseStatusStr = (String) params.get("releaseStatus");
			if ((releaseStatusStr != null) && (!releaseStatusStr.equals(""))) {
				this.releaseStatus = ReleaseStatusCode.valueOf(releaseStatusStr);
			}
			String topStr = (String) params.get("top");
			if ((topStr != null) && (!topStr.equals(""))) {
				this.top = NoticeTopCode.valueOf(topStr);
			}

		}
	}
	
	public NoticeData() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	


	public NtcColumnCode getNtcColumn() {
		return ntcColumn;
	}

	public void setNtcColumn(NtcColumnCode ntcColumn) {
		this.ntcColumn = ntcColumn;
	}

	public Long getPointNum() {
		return pointNum;
	}

	public void setPointNum(Long pointNum) {
		this.pointNum = pointNum;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public ReleaseStatusCode getReleaseStatus() {
		return releaseStatus;
	}

	public void setReleaseStatus(ReleaseStatusCode releaseStatus) {
		this.releaseStatus = releaseStatus;
	}

	public NoticeTopCode getTop() {
		return top;
	}

	public void setTop(NoticeTopCode top) {
		this.top = top;
	}

}
