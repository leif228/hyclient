package com.eyunda.part1.data;

import com.eyunda.main.data.Data_loader;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

public class PartData_loader extends Data_loader {

	/**
	 * 获取当前大类
	 * 
	 * @param handler
	 * @param cid
	 */
	public void getPlugin(AsyncHttpResponseHandler handler) {
		asyncHttpClient.get(serverUrl + "/pluginPackageService/all", handler);

	}

	/**
	 * 获取所有插件
	 * 
	 * @param handler
	 * @param cid
	 */
	public void pluginService(AsyncHttpResponseHandler handler) {
		asyncHttpClient.get(serverUrl + "/pluginService/all", handler);

	}

	/**
	 * 获取所有插件
	 * 
	 * @param handler
	 * @param cid
	 */
	public void pluginServicefavorCount(AsyncHttpResponseHandler handler,
			String pluginid) {
		asyncHttpClient.get(
				serverUrl + "/pluginService/favorCount/" + pluginid, handler);

	}

	/**
	 * 点赞
	 * 
	 * @param handler
	 * @param cid
	 */
	public void pluginServicefavor(AsyncHttpResponseHandler handler,
			String userId, String pluginid) {
		asyncHttpClient.get(serverUrl + "/pluginService/favor/" + userId + "/"
				+ pluginid, handler);

	}

	/**
	 * 取消点赞
	 * 
	 * @param handler
	 * @param cid
	 */
	public void pluginServiceunfavor(AsyncHttpResponseHandler handler,
			String userId, String pluginid) {
		asyncHttpClient.get(serverUrl + "/pluginService/unfavor/" + userId
				+ "/" + pluginid, handler);

	}

	/**
	 * 获取推荐插件插件
	 * 
	 * @param handler
	 * @param cid
	 */
	public void recommendedPlugins(AsyncHttpResponseHandler handler) {
		asyncHttpClient.get(serverUrl + "/pluginService/recommendedPlugins",
				handler);

	}

	/**
	 * 获取用户所有插件
	 * 
	 * @param handler
	 * @param cid
	 */
	public void myPlugin(AsyncHttpResponseHandler handler, String userid) {
		asyncHttpClient.get(serverUrl + "/pluginService/myPlugin/" + userid,
				handler);

	}

	/**
	 * 获取选择自定义插件
	 * 
	 * @param handler
	 * @param cid
	 */
	public void choosePlugin(AsyncHttpResponseHandler handler, String userid,
			String pluginid) {
		asyncHttpClient.post(serverUrl + "/pluginService/myPlugin/enable/"
				+ userid + "/" + pluginid, handler);

	}

	/**
	 * 取消获取选择自定义插件
	 * 
	 * @param handler
	 * @param cid
	 */
	public void unchoosePlugin(AsyncHttpResponseHandler handler, String userid,
			String pluginid) {
		asyncHttpClient.post(serverUrl + "/pluginService/myPlugin/disable/"
				+ userid + "/" + pluginid, handler);

	}

	/**
	 * 选择插件包
	 * 
	 * @param handler
	 * @param cid
	 */
	public void choosePluginPackage(AsyncHttpResponseHandler handler,
			String userid, String packageid) {
		asyncHttpClient.post(serverUrl
				+ "/pluginPackageService/myPluginPackage/" + userid + "/"
				+ packageid, handler);

	}

	/**
	 * 获取资讯
	 * 
	 * @param handler
	 * @param cid
	 *            第一个数字不用管 第二个数字表示栏目id：1是高考，3是自考，4是成考，5是研考，6是综合考试。 第三个数字表示当前第几页
	 *            第四个数字表示每页多少条。
	 */
	public void toTotalInfoList(AsyncHttpResponseHandler handler, int index,
			String type) {
		index += 1;
		asyncHttpClient.get(oldserverUrl
				+ "/inforClinetService/toTotalInfoList/0/" + type + "/" + index
				+ "/10", handler);

	}

	/**
	 * 高考资讯
	 * 
	 * @param handler
	 * @param index
	 * @param type
	 *            101高考，102自考，103成考，104考研
	 */
	public void infoRecommendService1(AsyncHttpResponseHandler handler,
			int index, String type, int tab_index) {

		if (tab_index == 0)
			asyncHttpClient.get(serverUrl
					+ "/infoRecommendService/list?pageSize=10&curPage=" + index
					+ "&t=" + type, handler);
		else {
			// index += 1;
			if (tab_index == 1)
				asyncHttpClient.get(
				// oldserverUrl+"/clinetService/toSubjectList/" +
						serverUrl + "/infoSubjectService/list/"
								+ "12,13,19,20,272" +
								// "/"+ index + "/10", handler);
								"?pageSize=10&curPage=" + index, handler);
			else if (tab_index == 2)
				asyncHttpClient.get(
				// oldserverUrl+"/clinetService/toSubjectList/" +
						serverUrl + "/infoSubjectService/list/" + "257,258" +
						// "/"+ index + "/10", handler);
								"?pageSize=10&curPage=" + index, handler);
			else if (tab_index == 3)
				asyncHttpClient.get(
				// oldserverUrl+"/clinetService/toSubjectList/" +
						serverUrl + "/infoSubjectService/list/" + "260" +
						// "/"+ index + "/10", handler);
								"?pageSize=10&curPage=" + index, handler);
			else if (tab_index == 4)
				asyncHttpClient.get(
				// oldserverUrl+"/clinetService/toSubjectList/" +
						serverUrl + "/infoSubjectService/list/"
								+ "246,247,248,249" +
								// "/"+ index + "/10", handler);
								"?pageSize=10&curPage=" + index, handler);
			else if (tab_index == 5)
				asyncHttpClient.get(
				// oldserverUrl+"/clinetService/toSubjectList/" +
						serverUrl + "/infoSubjectService/list/" + "260,261,263"
								+
								// "/"+ index + "/10", handler);
								"?pageSize=10&curPage=" + index, handler);
			else if (tab_index == 6)
				asyncHttpClient.get(
				// oldserverUrl+"/clinetService/toSubjectList/" +
						serverUrl + "/infoSubjectService/list/"
								+ "265,266,269,270,271" +
								// "/"+ index + "/10", handler);
								"?pageSize=10&curPage=" + index, handler);
			else if (tab_index == 7)
				asyncHttpClient.get(
				// oldserverUrl+"/clinetService/toSubjectList/" +
						serverUrl + "/infoSubjectService/list/"
								+ "233,234,235,236,237,238" +
								// "/"+ index + "/10", handler);
								"?pageSize=10&curPage=" + index, handler);
		}
	}

	/**
	 * 成考资讯
	 * 
	 * @param handler
	 * @param index
	 * @param type
	 *            101高考，102自考，103成考，104考研
	 */
	public void infoRecommendService2(AsyncHttpResponseHandler handler,
			int index, String type, int tab_index) {

		if (tab_index == 0)
			asyncHttpClient.get(serverUrl
					+ "/infoRecommendService/list?pageSize=10&curPage=" + index
					+ "&t=" + type, handler);
		else {
			index += 1;
			if (tab_index == 1)
				asyncHttpClient.get(serverUrl
						+ "/clinetService/toSubjectList/50/" + index + "/10",
						handler);
			else if (tab_index == 2)
				asyncHttpClient.get(serverUrl
						+ "/clinetService/toSubjectList/51/" + index + "/10",
						handler);
			else if (tab_index == 3)
				asyncHttpClient.get(serverUrl
						+ "/clinetService/toSubjectList/53/" + index + "/10",
						handler);
		}
	}

	/**
	 * 自考资讯
	 * 
	 * @param handler
	 * @param index
	 * @param type
	 *            101高考，102自考，103成考，104考研
	 */
	public void infoRecommendService3(AsyncHttpResponseHandler handler,
			int index, String type, int tab_index) {

		if (tab_index == 0)
			asyncHttpClient.get(serverUrl
					+ "/infoRecommendService/list?pageSize=10&curPage=" + index
					+ "&t=" + type, handler);
		else {
			index += 1;
			if (tab_index == 1)
				asyncHttpClient.get(serverUrl
						+ "/clinetService/toSubjectList/38/" + index + "/10",
						handler);
			else if (tab_index == 2)
				asyncHttpClient.get(serverUrl
						+ "/clinetService/toSubjectList/39/" + index + "/10",
						handler);
			else if (tab_index == 3)
				asyncHttpClient.get(serverUrl
						+ "/clinetService/toSubjectList/44/" + index + "/10",
						handler);
		}
	}

	/**
	 * 研考资讯
	 * 
	 * @param handler
	 * @param index
	 * @param type
	 *            101高考，102自考，103成考，104考研
	 */
	public void infoRecommendService4(AsyncHttpResponseHandler handler,
			int index, String type, int tab_index) {

		if (tab_index == 0)
			asyncHttpClient.get(serverUrl
					+ "/infoRecommendService/list?pageSize=10&curPage=" + index
					+ "&t=" + type, handler);
		else {
			index += 1;
			if (tab_index == 1)
				asyncHttpClient.get(serverUrl
						+ "/clinetService/toSubjectList/58/" + index + "/10",
						handler);
			else if (tab_index == 2)
				asyncHttpClient.get(serverUrl
						+ "/clinetService/toSubjectList/65/" + index + "/10",
						handler);

		}
	}

	/**
	 * 获取资讯首页
	 * 
	 * @param handler
	 * @param cid
	 *            第一个数字不用管 第二个数字表示栏目id：1是高考，3是自考，4是成考，5是研考，6是综合考试。 第三个数字表示当前第几页
	 *            第四个数字表示每页多少条。
	 */
	public void toTotalInfoList(AsyncHttpResponseHandler handler, String type) {

		// asyncHttpClient.get(
		// oldserverUrl+"/inforClinetService/toTopInfoList/"
		// + type + "/3", handler);
		asyncHttpClient.get(serverUrl
				+ "/infoRecommendService/list?pageSize=3&curPage=" + 0 + "&t="
				+ type, handler);

	}

	/**
	 * 获取资讯详情
	 * 
	 * @param handler
	 * @param id
	 */
	public void inforClinetService(AsyncHttpResponseHandler handler, String id) {

		asyncHttpClient.get(oldserverUrl + "/inforClinetService/toInfoDetail/"
				+ id + "/0/0/0", handler);

	}

	/**
	 * 获取广告
	 * 
	 * @param handler
	 * @param id
	 */
	public void mobileAdService(AsyncHttpResponseHandler handler, String id) {

		//asyncHttpClient.get(serverUrl + "/mobileAdService/list/" + id, handler);
		asyncHttpClient.get(serverUrlTemp + "/mobileAdService/listAd/" + id, handler);

	}

	/**
	 * 获取推送id
	 * 
	 * @param handler
	 * @param id
	 */
	public void pushCategory(AsyncHttpResponseHandler handler, String id) {

		asyncHttpClient.get(serverUrl + "/pushService/pushCategory/list/" + id,
				handler);

	}

	/**
	 * 设置推送
	 * 
	 * @param handler
	 * @param id
	 */
	public void pushService_enable(AsyncHttpResponseHandler handler,
			String userid, String id) {

		asyncHttpClient.post(serverUrl + "/pushService/mySubscribe/enable/"
				+ userid + "/" + id, handler);

	}

	/**
	 * 退订设置推送
	 * 
	 * @param handler
	 * @param id
	 */
	public void pushService_disable(AsyncHttpResponseHandler handler,
			String userid, String id) {
		if (userid.equals(""))
			return;
		asyncHttpClient.post(serverUrl + "/pushService/mySubscribe/disable/"
				+ userid + "/" + id, handler);

	}

	/**
	 * 插件详情
	 * 
	 * @param handler
	 * @param id
	 */
	public void pluginServiceDetail(AsyncHttpResponseHandler handler, String id) {
		asyncHttpClient.get(serverUrl + "/pluginService/plugin/" + id, handler);

	}

	/**
	 * 是否点赞
	 * 
	 * @param handler
	 * @param id
	 */
	public void pluginServiceisfavor(AsyncHttpResponseHandler handler,
			String userid, String pluginid) {
		asyncHttpClient.get(serverUrl + "/pluginService/isfavor/" + userid
				+ "/" + pluginid, handler);

	}

	/**
	 * 获取用户订阅推送
	 * 
	 * @param handler
	 * @param cid
	 */
	public void mySubscribe(AsyncHttpResponseHandler handler, String userid) {
		asyncHttpClient.get(serverUrl + "/pushService/mySubscribe/" + userid,
				handler);
	}

	/**
	 * 获取焦点模块id
	 * 
	 * @param handler
	 *            {"userId":***,"pluginids":"*****"}
	 */
	public void myShortcutList(AsyncHttpResponseHandler handler, String userid) {
		asyncHttpClient.get(serverUrl + "/pluginService/myShortcutList/"
				+ userid, handler);
	}

	/**
	 * 设置焦点模块id
	 * 
	 * @param handler
	 */
	public void addtoshortcut(AsyncHttpResponseHandler handler, String userid,
			String pluginids) {
		asyncHttpClient.post(serverUrl + "/pluginService/addtoshortcut/"
				+ userid + "/" + pluginids + userid, handler);
	}

	/**
	 * 获取推送消息
	 * 
	 * @param handler
	 */
	public void myMsglist(AsyncHttpResponseHandler handler, String userid,
			int index) {
		// ，1为资讯，2为高考分数推送，3为录取状态查询
		asyncHttpClient.get(serverUrl + "/pushService/myMsglist/" + userid
				+ "?pageSize=10&curPage=" + index, handler);
	}

	/**
	 * 复习备考
	 * 
	 * @param handler
	 */
	public void reviewplugin(AsyncHttpResponseHandler handler, int index) {
		asyncHttpClient.get(serverUrl + "/reviewService/reviewplugin/list"
				+ "?pageSize=10&curPage=" + index, handler);
	}

	/**
	 * 复习备考
	 * 
	 * @param handler
	 */
	public void reviewcard(AsyncHttpResponseHandler handler, int index,
			String reviewpluginid) {
		asyncHttpClient.get(serverUrl + "/reviewService/reviewcard/list/"
				+ reviewpluginid + "?pageSize=10&curPage=" + index, handler);
	}

	/**
	 * 复习备考知识点总数
	 * 
	 * @param handler
	 */
	public void reviewServicecount(AsyncHttpResponseHandler handler,
			String reviewpluginid) {
		asyncHttpClient.get(serverUrl + "/reviewService/reviewcard/count/"
				+ reviewpluginid, handler);
	}

	/**
	 * 复习备考已学习知识点总数
	 * 
	 * @param handler
	 */
	public void reviewServicemyviewcount(AsyncHttpResponseHandler handler,
			String userid, String reviewpluginid) {
		asyncHttpClient.get(serverUrl
				+ "/reviewService/reviewcard/myviewcount/" + userid + "/"
				+ reviewpluginid, handler);
	}

	/**
	 * 获取复习备考知识点
	 * 
	 * @param handler
	 */
	public void allreviewcard(AsyncHttpResponseHandler handler, int index,
			String reviewpluginid) {
		asyncHttpClient.get(serverUrl + "/reviewService/reviewcard/list/"
				+ reviewpluginid + "?pageSize=10&curPage=" + index, handler);
	}

	/**
	 * 获取某个复习备考进度
	 * 
	 * @param handler
	 */
	public void reviewcardmyviewprogress(AsyncHttpResponseHandler handler,
			int index, String reviewpluginid, String userid) {
		asyncHttpClient.get(serverUrl
				+ "/reviewService/reviewcard/myviewprogress/" + userid + "/"
				+ reviewpluginid + "?pageSize=10&curPage=" + index, handler);
	}

	/**
	 * 添加进度
	 * 
	 * @param handler
	 */
	public void reviewcardDone(AsyncHttpResponseHandler handler,
			String reviewpluginid, String userid, String reviewcardid) {
		asyncHttpClient.post(serverUrl + "/reviewService/reviewcard/done/"
				+ userid + "/" + reviewpluginid + "/" + reviewcardid, handler);
	}

	/**
	 * 添加进度
	 * 
	 * @param handler
	 */
	public void reviewcardUNDone(AsyncHttpResponseHandler handler,
			String reviewpluginid, String userid, String reviewcardid) {
		asyncHttpClient.post(serverUrl + "/reviewService/reviewcard/undone/"
				+ userid + "/" + reviewpluginid + "/" + reviewcardid, handler);
	}

	/**
	 * 备考详情
	 * 
	 * @param handler
	 */
	public void reviewcard(AsyncHttpResponseHandler handler,
			String reviewpluginid, String reviewcardid) {
		asyncHttpClient.get(serverUrl + "/reviewService/reviewcard/"
				+ reviewcardid, handler);
	}

	/**
	 * 获取报考流程tab type 考试类型
	 * 
	 * @param handler
	 */
	public void signflowService(AsyncHttpResponseHandler handler, String type) {
		asyncHttpClient.get(serverUrl + "/signflowService/list/" + type
				+ "?pageSize=1000", handler);
	}

	/**
	 * 某个报考流程 flowid 流程id
	 * 
	 * @param handler
	 */
	public void signflowServicesteps(AsyncHttpResponseHandler handler,
			String flowid) {
		asyncHttpClient.get(serverUrl + "/signflowService/steps/" + flowid,
				handler);
	}

	/**
	 * 某个报考流程政策 flowid 流程id
	 * 
	 * @param handler
	 */
	public void signflowServicepolicies(AsyncHttpResponseHandler handler,
			String stepid) {
		asyncHttpClient.get(serverUrl + "/signflowService/policies/" + stepid,
				handler);
	}

	/**
	 * 某个报考流程政策详情 flowid 流程id
	 * 
	 * @param handler
	 */
	public void signflowServicepolicy(AsyncHttpResponseHandler handler,
			String id) {
		asyncHttpClient.get(serverUrl + "/signflowService/policy/" + id,
				handler);
	}

	/**
	 * 添加自定义事件 || userId || number || 必选 || 用户id || || name || string || 必选 ||
	 * 事件名称 || || content || string || 必选 || 事件内容 || || timing || string || 必选
	 * || 事件时间，格式yyyyMMdd，如20140524 ||
	 * 
	 * @param handler
	 */
	public void mySelfDefinedEventsNew(AsyncHttpResponseHandler handler,
			String userId, String name, String content, String timing) {

		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("name", name);
		params.put("content", content);
		params.put("timing", timing);
		asyncHttpClient.post(serverUrl
				+ "/calendarService/mySelfDefinedEvents/new", params, handler);
	}

	/**
	 * 修改自定义事件
	 * 
	 * @param handler
	 * @param userId
	 * @param name
	 * @param content
	 * @param timing
	 * @param eventId
	 */
	public void mySelfDefinedEventsUpdate(AsyncHttpResponseHandler handler,
			String userId, String name, String content, String timing,
			String eventId) {

		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("name", name);
		params.put("content", content);
		params.put("timing", timing);
		asyncHttpClient.post(serverUrl
				+ "/calendarService/mySelfDefinedEvents/" + eventId, params,
				handler);
	}

	/**
	 * 删除自定义事件
	 * 
	 * @param handler
	 * @param eventid
	 */
	public void mySelfDefinedEventsDelete(AsyncHttpResponseHandler handler,
			String eventid) {
		asyncHttpClient.get(serverUrl
				+ "/calendarService/mySelfDefinedEvents/" + eventid
				+ "/delete", handler);
	}

	/**
	 * 获取用户自定义事件
	 * 
	 * @param handler
	 * @param userId
	 * 
	 *            [{"id":1,"userId":13148186557,"name":"自定义事件标题","content":
	 *            "自定义事件内容",
	 *            "timing":"2014-05-24 00:00:00","createtime":"2014-05-25 22:59:47"
	 *            }]
	 */
	public void mySelfDefinedEvents(AsyncHttpResponseHandler handler,
			String userId) {
		asyncHttpClient.get(serverUrl + "/calendarService/mySelfDefinedEvents/"
				+ userId, handler);
	}

	/**
	 * 启用系统事件
	 * 
	 * @param handler
	 * @param userId
	 * @param eventid
	 */
	public void myExameEventsenable(AsyncHttpResponseHandler handler,
			String userId, String eventid) {
		asyncHttpClient.post(serverUrl
				+ "/calendarService/myExameEvents/enable/" + userId + "/"
				+ eventid, handler);
	}

	/**
	 * 取消启用系统事件
	 * 
	 * @param handler
	 * @param userId
	 * @param eventid
	 */
	public void myExameEventsdisable(AsyncHttpResponseHandler handler,
			String userId, String eventid) {
		asyncHttpClient.post(serverUrl
				+ "/calendarService/myExameEvents/disable/" + userId + "/"
				+ eventid, handler);
	}

	/**
	 * 获取系统事件
	 * 
	 * @param handler
	 * @param userId
	 */
	public void examEvents(AsyncHttpResponseHandler handler, String type,
			int index) {
		asyncHttpClient.get(serverUrl + "/calendarService/examEvents/" + type
				+ "?pageSize=10&curPage=" + index, handler);
	}

	/**
	 * 获取倒计时
	 * 
	 * @param handler
	 * @param userId
	 */
	public void examTimers(AsyncHttpResponseHandler handler, String type,
			int index) {
		asyncHttpClient.get(serverUrl + "/calendarService/examTimers/" + type
				+ "?pageSize=10&curPage=" + index, handler);
	}

	/**
	 * 获取考试安排
	 * 
	 * @param handler
	 * @param userId
	 */
	public void examSchedules(AsyncHttpResponseHandler handler, String type,
			int index) {
		asyncHttpClient.get(serverUrl + "/calendarService/examSchedules/"
				+ type + "?pageSize=1&curPage=" + index, handler);
	}

	/**
	 * 获取某用户的系统考试倒计时列表
	 * 
	 * @param handler
	 * @param userId
	 */
	public void myExamTimers(AsyncHttpResponseHandler handler, String userid,
			int index) {
		asyncHttpClient.get(serverUrl + "/calendarService/myExamTimers/"
				+ userid
		// + "?pageSize=1&curPage=" + index
				, handler);
	}

	/**
	 * 获取某用户的系统考试倒计时列表
	 * 
	 * @param handler
	 * @param userId
	 */
	public void myExamTimersOne(AsyncHttpResponseHandler handler, String userid) {
		asyncHttpClient.get(serverUrl + "/calendarService/myExamTimers/"
				+ userid
		// + "?pageSize=1&curPage=0"
				, handler);
	}

	/**
	 * 获 取某用户的系统事件列表
	 * 
	 * @param handler
	 * @param userId
	 */
	public void myExamEvents(AsyncHttpResponseHandler handler, String userid,
			int index) {
		asyncHttpClient.get(serverUrl + "/calendarService/myExamEvents/"
				+ userid
		// + "?pageSize=1&curPage=" + index
				, handler);
	}

	/**
	 * 获 取某用户的系统事件列表
	 * 
	 * @param handler
	 * @param userId
	 */
	public void myExamEventsMain(AsyncHttpResponseHandler handler, String userid) {
		asyncHttpClient.get(serverUrl + "/calendarService/myExamEvents/"
				+ userid + "?pageSize=2&curPage=0", handler);
	}

	/**
	 * 获 取专题 内资讯列表
	 * 
	 * @param handler
	 * @param userId
	 */
	public void subjectServicelist(AsyncHttpResponseHandler handler,
			String subjectid, int index) {
		asyncHttpClient.get(serverUrl + "/subjectService/list/" + subjectid
				+ "?pageSize=10&curPage=" + index, handler);
	}

	/**
	 * 获取事件详情
	 * 
	 * @param handler
	 * @param id
	 */
	public void examEventResources(AsyncHttpResponseHandler handler, String id) {
		asyncHttpClient.get(serverUrl + "/calendarService/examEventResources/"
				+ id
		// + "?pageSize=1&curPage=" + index
				, handler);
	}

	/**
	 * 用户启用考试倒计时
	 * 
	 * @param handler
	 * @param userId
	 */
	public void myExameTimersenable(AsyncHttpResponseHandler handler,
			String userId, String timerid) {
		asyncHttpClient.post(serverUrl
				+ "/calendarService/myExameTimers/enable/" + userId + "/"
				+ timerid, handler);
	}

	/**
	 * 用户取消考试倒计时
	 * 
	 * @param handler
	 * @param userId
	 */
	public void myExameTimersdisable(AsyncHttpResponseHandler handler,
			String userId, String timerid) {
		asyncHttpClient.post(serverUrl
				+ "/calendarService/myExameTimers/disable/" + userId + "/"
				+ timerid, handler);
	}

	/**
	 * 获取插件排序
	 * 
	 * @param handler
	 * @param userId
	 */
	public void pluginServicemyOrder(AsyncHttpResponseHandler handler,
			String userId) {
		asyncHttpClient.get(serverUrl + "/pluginService/myOrder/" + userId,
				handler);
	}

	/**
	 * 添加排序
	 * 
	 * @param handler
	 * @param userId
	 */
	public void pluginServicesaveOrder(AsyncHttpResponseHandler handler,
			String userId, String content) {
		asyncHttpClient.post(serverUrl + "/pluginService/saveOrder/" + userId
				+ "/" + content, handler);
	}

	/**
	 * 获取用户绑定考生
	 * 
	 * @param handler
	 * @param userId
	 */
	public void userExamineeBindingService(AsyncHttpResponseHandler handler,
			String userId) {
		asyncHttpClient.get(serverUrl + "/userExamineeBindingService/bindlist/"
				+ userId, handler);
	}

	/**
	 * 查看是否有通过的提问
	 * 
	 * @param handler
	 * @param userId
	 */
	public void mobileUsergetNotify(AsyncHttpResponseHandler handler,
			String userId) {
		asyncHttpClient.get(serverUrl + "/userService/mobileUser/getNotify/"
				+ userId, handler);
	}

	/**
	 * 修改绑定
	 * 
	 * @param handler
	 * @param userId
	 */
	public void userExamineeBindingServicerebind(
			AsyncHttpResponseHandler handler, String userId, String name,
			String idcard, String admissioncardid, String id, String registerno) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("name", name);
		params.put("idcard", idcard);
		params.put("admissioncardid", admissioncardid);
		params.put("registerno", registerno);
		asyncHttpClient.post(serverUrl + "/userExamineeBindingService/rebind/"
				+ id, params, handler);
	}

	/**
	 * 用户绑定考生
	 * 
	 * @param handler
	 * @param userId
	 */
	public void userExamineeBindingServicebind(
			AsyncHttpResponseHandler handler, String userId, String name,
			String idcard, String admissioncardid, String registerno) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("name", name);
		params.put("idcard", idcard);
		params.put("admissioncardid", admissioncardid);
		params.put("registerno", registerno);
		asyncHttpClient.post(serverUrl + "/userExamineeBindingService/bind",
				params, handler);
	}

	/**
	 * 帮顶
	 * 
	 * @param handler
	 * @param userId
	 * @param user_id
	 * @param appid
	 * @param channel_id
	 */
	public void pushServiceandroidBind(AsyncHttpResponseHandler handler,
			String userId, String user_id, String appid, String channel_id) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("user_id", user_id);
		params.put("appid", appid);
		params.put("channel_id", channel_id);

		asyncHttpClient.post(serverUrl + "/pushService/androidBind", params,
				handler);
	}

	/**
	 * 获取插件帮顶图片
	 * 
	 * @param handler
	 * @param userId
	 */
	public void pluginServicepictures(AsyncHttpResponseHandler handler,
			String plugid) {
		asyncHttpClient.get(serverUrl + "/pluginService/pictures/list/"
				+ plugid, handler);
	}

	/**
	 * 插件反馈
	 * 
	 * @param handler
	 * @param userId
	 * @param pluginId
	 * @param content
	 */
	public void userFeedBackService(AsyncHttpResponseHandler handler,
			String userId, String pluginId, String content) {

		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("pluginId", pluginId);
		params.put("content", content);
		asyncHttpClient.post(serverUrl + "/userFeedBackService/new", params,
				handler);
	}

}
