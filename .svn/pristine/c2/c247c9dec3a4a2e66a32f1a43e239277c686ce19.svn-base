package com.eyunda.main.data;

import java.io.File;
import java.io.FileNotFoundException;


import com.eyunda.main.reg.MD5Util;
import com.ta.annotation.TAInject;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

public class Data_loader {
//	protected static String serverUrl = "http://59.175.144.111:9001";//正式服务
	
	protected static String serverUrl = "http://115.28.227.82:9001"; //2.0服务 测试服务器
	protected static String serverUrlTemp = "http://api.eyunda.com"; //替换服务
	protected static String oldserverUrl="http://59.175.144.111:8070";
	@TAInject
	protected static AsyncHttpClient asyncHttpClient;

	public Data_loader() {
		if (asyncHttpClient == null)
			asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("SECURITYKEY", "111" );
	}

	/**
	 * 获取推荐船舶及航线
	 * 
	 * @param handler
	 */
	public void recommendBoat(AsyncHttpResponseHandler handler,
			String userid, String t) {
		if (userid.equals(""))
			userid = "0";
		asyncHttpClient.get(
				serverUrlTemp+"/appService/recommendBoat/"
						+ userid + "?pageSize=5&curPage=0&t=" + t, handler);
	}

	/**
	 * 获取推荐院校列表
	 * 
	 * @param handler
	 */
	public void recommendCollege(AsyncHttpResponseHandler handler,
			String userid, String t) {

		// asyncHttpClient
		// .post(oldserverUrl+"/collegeClinetService/listCommCollege/1/101/1/2",
		// handler);
		//[{"cid":2576,"schoolname":"毕节职业技术学院","popularity":1,"infotitle":""}]
		if (userid.equals(""))
			userid = "0";
//		asyncHttpClient.get(
//				serverUrl+"/collegeRecommendService/list/"
//						+ userid + "?pageSize=5&curPage=0&t=" + t, handler);
		asyncHttpClient.get(
				serverUrlTemp+"/appService/recommendBoat/"
						+ userid + "?pageSize=5&curPage=0&t=" + t, handler);
	}

	/**
	 * 获取关注院校列表
	 * 
	 * @param handler
	 */
	public void myfavors(AsyncHttpResponseHandler handler, String userid,
			String t) {
		if (userid.equals(""))
			return;
		asyncHttpClient.get(
				serverUrl+"/collegeFavorService/myfavors/"
						+ userid + "?curPage=0&pageSize=2&t=" + t, handler);
	}

	/**
	 * 获取关注院校分页列表
	 * 
	 * @param handler
	 */
	public void myfavors(AsyncHttpResponseHandler handler, int page,
			String userid) {

		if (userid.equals(""))
			return;
		asyncHttpClient
				.get(serverUrl+"/collegeFavorService/myfavors/"
						+ userid + "?curPage=" + page + "&pageSize=10", handler);
	}

	/**
	 * 获取关注院校分页列表
	 * 
	 * @param handler
	 */
	public void myfavorsInfo(AsyncHttpResponseHandler handler, int page,
			String userid) {

		if (userid.equals(""))
			return;
		asyncHttpClient.get(
				serverUrl+"/infoFavorService/myfavors/" + userid
						+ "?curPage=" + page + "&pageSize=10", handler);
	}

	/**
	 * 获取所有关注
	 * 
	 * @param handler
	 * @param userid
	 */
	public void getAllFavors(AsyncHttpResponseHandler handler, String userid) {
		if (userid.equals(""))
			return;
		asyncHttpClient.get(
				serverUrl+"/collegeFavorService/myfavors/"
						+ userid, handler);

	}

	/**
	 * 获取所有关注资讯
	 * 
	 * @param handler
	 * @param userid
	 */
	public void getAllFavorsInfo(AsyncHttpResponseHandler handler, String userid) {
		if (userid.equals(""))
			return;
		asyncHttpClient
				.get(serverUrl+"/infoFavorService/myfavors/"
						+ userid, handler);

	}

	/**
	 * 获取推荐院校分页列表
	 * 
	 * @param handler
	 */
	public void recommendCollege(AsyncHttpResponseHandler handler, int page,
			String userid, String t) {

		// asyncHttpClient.post(
		// oldserverUrl+"/collegeClinetService/listCommCollege/1/101/"
		// + (page + 1) + "/10", handler);

		if (userid.equals(""))
			userid = "0";
		asyncHttpClient.get(
				serverUrl+"/collegeRecommendService/list/"
						+ userid + "?pageSize=10&curPage=" + page + "&t=" + t,
				handler);
	}

	/**
	 * 获取自考所有院校
	 * 
	 * @param handler
	 */
	public void loadCollege_zk(AsyncHttpResponseHandler handler, int index) {
		int form = 1 + index ;
		int to =  10 ;

		asyncHttpClient.post(
				oldserverUrl+"/collegeClinetService/listCollege/2/102/"
						+ form + "/" + to, handler);

	}

	/**
	 * 获取研考所有院校
	 * 
	 * @param handler
	 */
	public void loadCollege_yk(AsyncHttpResponseHandler handler, int index) {
		int form = 1 + index ;
		int to =  10 ;

		asyncHttpClient.post(
				oldserverUrl+"/collegeClinetService/listCollege/4/104/"
						+ form + "/" + to, handler);

	}

	/**
	 * 获取成考所有院校
	 * 
	 * @param handler
	 */
	public void loadCollege_ck(AsyncHttpResponseHandler handler, int index) {
		int form = 1 + index ;
		int to =  10 ;

		asyncHttpClient.post(
				oldserverUrl+"/collegeClinetService/listCollege/3/103/"
						+ form + "/" + to, handler);

	}

	/**
	 * 获取指定学校详情
	 * 
	 * @param handler
	 */
	public void loadCollegeInfo(AsyncHttpResponseHandler handler, String index) {
//		asyncHttpClient.post(
//				oldserverUrl+"/collegeClinetService/collegeDetail/"
//						+ index, handler);
		asyncHttpClient.post(serverUrlTemp+"/appService/schoolInfo/"+index,handler);
		
	}

	/**
	 * 获取常见问题列表
	 * 
	 * @param handler
	 */
	public void loadNormalQuestion(AsyncHttpResponseHandler handler, int index,
			String collegeId, String type) {
		// asyncHttpClient.post(
		// oldserverUrl+"/questionClientService.do?op=list&pageInfo.curPage="
		// + (index + 1) + "&pageInfo.pageSize=10", handler);
		asyncHttpClient.get(
				serverUrl+"/qaService/user/classicQuestions/"
						+ collegeId + "/" + type + "?pageSize=10&curPage="
						+ index, handler);
	}

	/**
	 * 获取所有我的问题
	 * 
	 * @param handler
	 * @param index
	 * @param userid
	 */
	public void loadMyQuestion(AsyncHttpResponseHandler handler, int index,
			String userid) {
		asyncHttpClient.get(
				serverUrl+"/qaService/user/questions/" + userid
						+ "?pageSize=10&curPage=" + index, handler);
	}
	
	
	
	/**
	 * 招生答疑：
	 * @param handler
	 * @param index
	 * @param userid
	 */
	public void recruitmentquestions(AsyncHttpResponseHandler handler, int index,
			String userid) {
		asyncHttpClient.get(
				serverUrl+"/qaService/user/recruitmentquestions/" + userid
						+ "?pageSize=10&curPage=" + index, handler);
	}
	
	/**
	 * 院校答疑
	 * @param handler
	 * @param index
	 * @param userid
	 */
	public void collegequestions(AsyncHttpResponseHandler handler, int index,
			String userid) {
		asyncHttpClient.get(
				serverUrl+"/qaService/user/collegequestions/" + userid
						+ "?pageSize=10&curPage=" + index, handler);
	}
	

	/**
	 * 获取所有我和院校相关的问题
	 * 
	 * @param handler
	 * @param index
	 * @param userid
	 */
	public void loadMyQuestion(AsyncHttpResponseHandler handler, int index,
			String userid, String collegeId, String type) {
		asyncHttpClient.get(
				serverUrl+"/qaService/user/questions/" + userid
				 + "/" + collegeId
						+ "?pageSize=10&curPage=" + index + "&t=" + type,
				handler);
	}

	/**
	 * 获取所有我和院校相关的问题
	 * 
	 * @param handler
	 * @param index
	 * @param userid
	 */
	public void loadMyQuestionmMain(AsyncHttpResponseHandler handler,
			int index, String userid, String collegeId, String type) {
		asyncHttpClient.get(
				serverUrl+"/qaService/user/classicQuestions/"
						+ collegeId + "/" + type + "?pageSize=2&curPage="
						+ index, handler);
	}

	/**
	 * 登录
	 * 
	 * @param handler
	 */
	public void login(AsyncHttpResponseHandler handler, String mobileNo,
			String password) {
		RequestParams params = new RequestParams();
		params.put("mobileNo", mobileNo);
		params.put("password", password);
		params.put("verificationCode", "1234");
		asyncHttpClient.post(
				serverUrl+"/userService/mobileUser/login",
				params, handler);
//		asyncHttpClient.post(
//				serverUrlTemp+"/userService/mobileUser/login",
//				params, handler);
	}

	/**
	 * 获取验证码
	 * 
	 * @param handler
	 */
	public void getVer(AsyncHttpResponseHandler handler, String mobileNo) {

		asyncHttpClient.post(
				serverUrl+"/userService/mobileUser/sendRegisterVerificationCode/"
						+ mobileNo, handler);
	}
	
	/**
	 * 获取验证码
	 * 
	 * @param handler
	 */
	public void getVerForGetpwd(AsyncHttpResponseHandler handler, String mobileNo) {

		asyncHttpClient.post(
				serverUrl+"/userService/mobileUser/sendPasswdVerificationCode/"
						+ mobileNo, handler);
	}

	/**
	 * 验证注册验证码
	 * 
	 * @param handler
	 * @param mobileNo
	 * @param password
	 * @param verificationCode
	 */
	public void register(AsyncHttpResponseHandler handler, String mobileNo,
			String verificationCode) {

		RequestParams params = new RequestParams();
		params.put("mobileNo", mobileNo);
		params.put("verificationCode", verificationCode);
		asyncHttpClient
				.post(serverUrl+"/userService/mobileUser/registerMobileNo",
						params, handler);
	}

	/**
	 * 注册 　　　 “mobileNo”: “13148186557”, 　　　“password”: “123456”,
	 * 　　　“verificationCode” : “1234”, 　　　“phoneModel”:”HTC G18”, 　　　“IMEI”:
	 * “1794705015”, 　　　“MID”: “jlauqutal”
	 * 
	 * @param handler
	 */
	public void register(AsyncHttpResponseHandler handler, String mobileNo,
			String password, String verificationCode, String phoneModel,
			String IMEI, String MID, String passwordQuestion,
			String passwordAnswer) {
		RequestParams params = new RequestParams();
		params.put("mobileNo", mobileNo);
		params.put("password", password);
		params.put("verificationCode", verificationCode);
		params.put("phoneModel", phoneModel);
		params.put("IMEI", IMEI);
		params.put("MID", MID);

		params.put("passwordQuestion", passwordQuestion);
		params.put("passwordAnswer", passwordAnswer);
		asyncHttpClient
				.post(serverUrl+"/userService/mobileUser/registerDetail",
						params, handler);
	}

	/**
	 * 获取验证码
	 * 
	 * @param handler
	 * @param mobileNo
	 */
	public void getVerificationCode(AsyncHttpResponseHandler handler,
			String mobileNo) {
		RequestParams params = new RequestParams();
		params.put("mobileNo", mobileNo);
		asyncHttpClient
				.post(serverUrl+"/userService/mobileUser/getVerificationCode",
						params, handler);

	}

	/**
	 * 获取招生动态详情
	 */
	public void getCollegeRecruit(AsyncHttpResponseHandler handler,
			String collegeId) {

//		asyncHttpClient.post(
//				oldserverUrl+"/collegeClinetService/collegeNewsDetail/"
//						+ collegeId, handler);
		asyncHttpClient.post(serverUrlTemp+"/appService/newDetail/"+collegeId,handler);
	}

	/**
	 * 获取院校投档线
	 * 
	 * @param handler
	 * @param collegeId
	 * @param index
	 */
	public void getScoreList(AsyncHttpResponseHandler handler,
			String collegeId, int index) {
		int form = 1 + index * 10;
		int to = 10 + index * 10;
//		asyncHttpClient.post(
//				oldserverUrl+"/collegeClinetService/scoreList/"
//						+ collegeId + "/0/0/0/" + form + "/" + to + "/101",
//				handler);toudangLine
		asyncHttpClient.post(serverUrlTemp+"/appService/toudangLine",handler);
		
	}

	/**
	 * 获取招生资讯
	 * 
	 * @param handler
	 * @param collegeId
	 * @param index
	 */
	public void getCollegeNewsList1(AsyncHttpResponseHandler handler,
			String collegeId, int index) {
		int from = 1 + index * 100;
		int to = 100 + index * 100;
//		asyncHttpClient
//				.post(oldserverUrl+"/collegeClinetService/collegeNewsList/"
//						+ collegeId + "/1/" + from + "/" + to + "/101", handler);
		asyncHttpClient.post(serverUrlTemp+"/appService/zhaosheng/"+collegeId, handler);
	}

	/**
	 * 获取新闻
	 * 
	 * @param handler
	 * @param collegeId
	 * @param index
	 */
	public void getCollegeNewsList2(AsyncHttpResponseHandler handler,
			String collegeId, int index) {
		int from = 1 + index * 100;
		int to = 100 + index * 100;
		asyncHttpClient
				.post(oldserverUrl+"/collegeClinetService/collegeNewsList/"
						+ collegeId + "/2/" + from + "/" + to + "/101", handler);
	}

	/**
	 * 添加关注
	 * 
	 * @param handler
	 * @param userId
	 * @param collegeId
	 */
	public void addFavorite(AsyncHttpResponseHandler handler, String userId,
			String collegeId, String t) {
		if (userId.equals(""))
			return;
		
		// RequestParams params = new RequestParams();
		// params.put("userFavorite.userId", userId);
		// params.put("userFavorite.favorite", collegeId);
		// params.put("userFavorite.t", "2");
		// params.put("userFavorite.msg", "");
		// asyncHttpClient
		// .post(oldserverUrl+"/userFavoriteClinetService.do?op=addFavorite",
		// params, handler);
		asyncHttpClient.get(
				serverUrl+"/collegeFavorService/favor/" + userId
						+ "/" + collegeId + "?t=" + t, handler);

	}

	/**
	 * 添加关注资讯
	 * 
	 * @param handler
	 * @param userId
	 * @param collegeId
	 */
	public void infoFavorService(AsyncHttpResponseHandler handler,
			String userId, String collegeId) {
		if (userId.equals(""))
			return;
		
		asyncHttpClient.get(serverUrl+"/infoFavorService/favor/"
				+ userId + "/" + collegeId, handler);

	}

	/**
	 * 取消关注
	 * 
	 * @param handler
	 * @param userId
	 * @param collegeId
	 */
	public void delFavorite(AsyncHttpResponseHandler handler, String userId,
			String collegeId, String t) {
		if (userId.equals(""))
			return;
		
		// RequestParams params = new RequestParams();
		// params.put("userFavorite.userId", userId);
		// params.put("userFavorite.favorite", collegeId);
		// params.put("userFavorite.t", "2");
		// params.put("userFavorite.msg", "");
		// asyncHttpClient
		// .post(oldserverUrl+"/userFavoriteClinetService.do?op=delFavorite",
		// params, handler);

		asyncHttpClient.get(
				serverUrl+"/collegeFavorService/unfavor/"
						+ userId + "/" + collegeId + "?t=" + t, handler);

	}

	/**
	 * 取消关注资讯
	 * 
	 * @param handler
	 * @param userId
	 * @param collegeId
	 */
	public void delFavoriteInfo(AsyncHttpResponseHandler handler,
			String userId, String collegeId) {
		if (userId.equals(""))
			return;
		
		asyncHttpClient.get(
				serverUrl+"/infoFavorService/unfavor/" + userId
						+ "/" + collegeId, handler);

	}

	/**
	 * 提问
	 * 
	 * @param handler
	 * @param userId
	 * @param collegeId
	 */
	public void newQuestion(AsyncHttpResponseHandler handler, String userId,
			String mobileNo, String title, String type, String collegeId,
			String t, String content) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("mobileNo", mobileNo);
		params.put("title", title);
		params.put("type", type);
		params.put("t", t);
		params.put("collegeId", collegeId);
		params.put("content", content);
		asyncHttpClient.post(
				serverUrl+"/qaService/user/newQuestion", params,
				handler);

	}

	/**
	 * 院校批次
	 * 
	 * @param handler
	 */
	public void listAllByP1(AsyncHttpResponseHandler handler) {
//		asyncHttpClient
//				.get(oldserverUrl+"/collegeClinetService/listAllByP/213",
//						handler);
		asyncHttpClient.get(serverUrlTemp+"/appService/pichi",handler);
		
	}

	/**
	 * 院校类别
	 * 
	 * @param handler
	 */
	public void listAllByP2(AsyncHttpResponseHandler handler) {
//		asyncHttpClient.get(
//				oldserverUrl+"/collegeClinetService/listAllByP/2",
//				handler);
		asyncHttpClient.get(serverUrlTemp+"/appService/listAllByP",handler);
	}

	/**
	 * 院校类别
	 * 
	 * @param handler
	 */
	public void listAllByP1943(AsyncHttpResponseHandler handler) {
		asyncHttpClient
				.get(oldserverUrl+"/collegeClinetService/listAllByP/1943",
						handler);
	}

	/**
	 * 招生类别
	 * 
	 * @param handler
	 */
	public void listAllByP3(AsyncHttpResponseHandler handler, String id) {
		if (id.equals("全部"))
			return;
		asyncHttpClient.get(
				oldserverUrl+"/collegeClinetService/listAllByP/"
						+ id, handler);
	}

	/**
	 * 专业层次
	 * 
	 * @param handler
	 */
	public void listAllByPTS(AsyncHttpResponseHandler handler, String id) {
		if (id.equals("全部"))
			return;
		asyncHttpClient.get(
				oldserverUrl+"/collegeClinetService/listAllByPTS/47/102/"
						+ id, handler);
	}

	/**
	 * 
	 * 
	 参数 数据 说明 湖北 省份 college.schoolName 武汉大学 学校名称 college.t1 101
	 * 表示学校是高考的(不可并用)（必填） college.t2 102 自考(不可并用)（必填） college.t3 103
	 * 成考(不可并用)（必填） college.t4 104 考研(不可并用)（必填） college.collegeExpand.pic 226
	 * 学校批次（数字，批次序号） college.collegeExpand.toudx 投档线类别
	 * college.collegeExpand.leib 院校类别 college.collegeExpand.putzy 专业的序号
	 * college.item.did 招生类别序号（查询特殊招生时必须有） college.item.exam
	 * 测试类别（1校组织专业测试2文化课考试3校组织college
	 * .province测试&文化课考试4冬令营5试训6面试7校考8湖北省美术统考9湖北省非美术联考10艺术校考11美术统考&校考12非美术联考&校考）
	 * college.item.recruitLimit 1或2 招生范围1全国2湖北 college.item.items 240
	 * 招生项目（特殊招生的子类） college.score.minScore 100 分数最低（查找分数范围）
	 * college.score.maxScore 200 分数最高（查找分数范围） college.score.year 2011
	 * 年份（查分数的话必选） pageInfo.curPage 2 当前第几页（第一页可以不加此参数） pageInfo.pageSize 20
	 * 每页显示的条数 college.type1 专业类 专业类、艺术类、体育类
	 */
	public void searchCollege(AsyncHttpResponseHandler handler, int page,
			String pc, String lb, String tszs1, String tszs2, String name,
			String low, String max, String address, String year,
			boolean ifscore, String type) {
		System.out.println(page);
		RequestParams params = new RequestParams();
		params.put("pageInfo.curPage", (page + 1) + "");
		params.put("pageInfo.pageSize", "10");
		params.put("college.t1", type);
		if (pc != null && !pc.equals("全部"))
			params.put("college.collegeExpand.pic", pc);
		if (ifscore) {
			if (low != null)
				params.put("college.score.minScore", low);
			if (max != null)
				params.put("college.score.maxScore", max);
			if (year != null)
				params.put("college.score.year", year);

		} else {

			if (lb != null && !lb.equals("全部"))
				params.put("college.collegeExpand.leib", lb);
			if (tszs1 != null && !tszs1.equals("全部"))
				params.put("college.item.did", tszs1); // 3297 3298
			if (tszs2 != null && !tszs2.equals("全部"))
				params.put("college.item.items", tszs2); // 3297 3298
			if (name != null && !name.equals("全部") && !name.equals(""))
				params.put("college.schoolName", name);
			if (address != null && !address.equals("全部"))
				params.put("college.province", address);

		}
		asyncHttpClient.post(serverUrlTemp+"/appService/getLists", params, handler);
		// asyncHttpClient
		// .post(oldserverUrl+"/collegeClinetService.do?op=searchCollege",
		// params, handler);
		// college.t1=101&college.collegeExpand.leib=8
		// &pageInfo.curPage=1&college.collegeExpand.pic=226
		// &college.schoolName=&pageInfo.pageSize=10
//		if (ifscore)
//			asyncHttpClient
//					.post(oldserverUrl+"/collegeClinetService.do?op=searchCollegeScore",
//							params, handler);
//		else
//			asyncHttpClient
//					.post(oldserverUrl+"/collegeClinetService.do?op=searchCollegeExpand",
//							params, handler);

	}

	/**
	 * 自考院校接口
	 * 
	 * @param handler
	 * @param page
	 * @param tszs1
	 * @param tszs2
	 * @param name
	 * @param address
	 * 
	 *            ，院校搜索，搜索条件包括：学校名称（模糊匹配）、地区（省份）、专业层次（两级关联的下拉列表）
	 */
	public void searchCollege_zk(AsyncHttpResponseHandler handler, int page,
			String tszs1, String tszs2, String name, String address) {
		RequestParams params = new RequestParams();
		params.put("pageInfo.curPage", (page + 1) + "");
		params.put("pageInfo.pageSize", "10");
		params.put("college.t2", "102");

		if (address != null && !address.equals("全部"))
			params.put("college.province", address);
		if (name != null && !name.equals("全部") && !name.equals(""))
			params.put("college.schoolName", name);
		if (tszs1 != null && !tszs1.equals("全部"))
			params.put("college.item.did", tszs1); // 3297 3298
		if (tszs2 != null && !tszs2.equals("全部"))
			params.put("college.item.items", tszs2); // 3297 3298

		asyncHttpClient
				.post(oldserverUrl+"/collegeClinetService.do?op=searchCollegeExpand",
						params, handler);
	}

	/**
	 * 研考院校接口
	 * 
	 * @param handler
	 * @param page
	 * @param tszs1
	 * @param tszs2
	 * @param name
	 * @param address
	 * 
	 *            ，院校搜索，搜索条件包括：学校名称（模糊匹配）、地区（省份）、专业层次（两级关联的下拉列表）
	 */
	public void searchCollege_yk(AsyncHttpResponseHandler handler, int page,
			String tszs1, String name, String address) {
		RequestParams params = new RequestParams();
		params.put("pageInfo.curPage", (page + 1) + "");
		params.put("pageInfo.pageSize", "10");
		params.put("college.t3", "104");

		if (address != null && !address.equals("全部"))
			params.put("college.province", address);
		if (name != null && !name.equals("全部") && !name.equals(""))
			params.put("college.schoolName", name);
		if (tszs1 != null && !tszs1.equals("全部"))
			params.put("college.collegeExpand.leib", tszs1);

		asyncHttpClient
				.post(oldserverUrl+"/collegeClinetService.do?op=searchCollegeExpand",
						params, handler);
	}

	/**
	 * 成考院校接口
	 * 
	 * @param handler
	 * @param page
	 * @param tszs1
	 * @param tszs2
	 * @param name
	 * @param address
	 * 
	 *            ，院校搜索，搜索条件包括：学校名称（模糊匹配）、地区（省份）、专业层次（两级关联的下拉列表）
	 */
	public void searchCollege_ck(AsyncHttpResponseHandler handler, int page,
			String tszs1, String name, String address) {
		RequestParams params = new RequestParams();
		params.put("pageInfo.curPage", (page + 1) + "");
		params.put("pageInfo.pageSize", "10");
		params.put("college.t4", "103");

		if (address != null && !address.equals("全部"))
			params.put("college.province", address);
		if (name != null && !name.equals("全部") && !name.equals(""))
			params.put("college.schoolName", name);
		if (tszs1 != null && !tszs1.equals("全部"))
			params.put("college.collegeExpand.leib", tszs1);

		asyncHttpClient
				.post(oldserverUrl+"/collegeClinetService.do?op=searchCollegeExpand",
						params, handler);
	}

	/**
	 * 获取我的关注院校数量
	 * 
	 * @param handler
	 */
	public void myfavorCount(AsyncHttpResponseHandler handler, String userid) {
		asyncHttpClient.get(
				serverUrl+"/collegeFavorService/myfavorCount/"
						+ userid, handler);
	}

	/**
	 * 获取我的关注资讯数量
	 * 
	 * @param handler
	 */
	public void newsFavorServicemyfavorCount(AsyncHttpResponseHandler handler,
			String userid) {
		asyncHttpClient.get(
				serverUrl+"/infoFavorService/myfavorCount/"
						+ userid, handler);
	}

	/**
	 * 修改密码 mobileNo/password/newPassword
	 * 
	 * @param handler
	 */
	public void updatePwd(AsyncHttpResponseHandler handler, String password,
			String mobileNo, String newPassword) {
		RequestParams params = new RequestParams();
		params.put("password", MD5Util.MD5(password));
		params.put("mobileNo", mobileNo);
		params.put("newPassword", MD5Util.MD5(newPassword));
		asyncHttpClient
				.post(serverUrl+"/userService/mobileUser/updatePasswd",
						params, handler);
	}

	/**
	 * 修改头像
	 * 
	 * @param handler
	 */
	public void uploadHead(AsyncHttpResponseHandler handler, String mobileNo,
			String path) {
		RequestParams params = new RequestParams();
		try {
			params.put("picture", new File(path));
		} catch (FileNotFoundException e) {
		}
		asyncHttpClient.post(
				serverUrl+"/userService/mobileUser/uploadheadPortrait/"
						+ mobileNo, params, handler);
	}

	/**
	 * 修改密码提示
	 * 
	 * @param handler
	 */
	public void updateQA(AsyncHttpResponseHandler handler, String password,
			String mobileNo, String passwordQuestion, String passwordAnswer) {
		RequestParams params = new RequestParams();
		params.put("password", MD5Util.MD5(password));
		params.put("passwordQuestion", passwordQuestion);
		params.put("passwordAnswer", passwordAnswer);
		asyncHttpClient.post(
				serverUrl+"/userService/mobileUser/updatePasswdQuestion/"
						+ mobileNo, params, handler);
	}

	/**
	 * 重置密码
	 * 
	 * @param handler
	 */
	public void resetPasswd(AsyncHttpResponseHandler handler, String password,
			String mobileNo, String verificationCode) {
		RequestParams params = new RequestParams();
		params.put("newPassword", MD5Util.MD5(password));
		params.put("verificationCode", verificationCode);
		// asyncHttpClient.post(serverUrl+"/userService/mobileUser/resetPasswd/"+mobileNo,
		// params, handler);

		// asyncHttpClient.get(
		// serverUrl+"/userService/mobileUser/forgetPasswd/"+mobileNo,
		// handler) ;

		asyncHttpClient.post(
				serverUrl+"/userService/mobileUser/resetPasswd/"
						+ mobileNo,params, handler);
	}

	/**
	 * 获取院校答疑老师数
	 * 
	 * @param handler
	 * @param cid
	 */
	public void getAnswerer(AsyncHttpResponseHandler handler, String cid) {

		asyncHttpClient.get(serverUrl+"/collegeService/" + cid
				+ "/answererCount", handler);

	}

	/**
	 * 获取更新版本
	 * 
	 * @param handler
	 * @param cid
	 */
	public void getVersiom(AsyncHttpResponseHandler handler) {

//		asyncHttpClient.get(
//				serverUrl+"/appService/latestVersion/android",
//				handler);
		asyncHttpClient.get(
				serverUrlTemp+"/appService/latestVersion/android",
				handler);
	}

	/**
	 * 
	 * 
	 * @param handler
	 * @param cid
	 */
	public void getSchoolName(AsyncHttpResponseHandler handler, String id) {

		asyncHttpClient.get(serverUrl+"/collegeService/" + id
				+ "/name", handler);

	}

	/**
	 * 
	 * 获取用户问题详情
	 * 
	 * @param handler
	 * @param cid
	 */
	public void userquestion(AsyncHttpResponseHandler handler, String questionId) {

		asyncHttpClient.get(
				serverUrl+"/qaService/user/question/"
						+ questionId, handler);

	}

}
