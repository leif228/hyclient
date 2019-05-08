$(document).ready(function(){
	// 初始化船舶图片缓冲
	// var initImage = new GetImage(_points).initImage();
	var targetW, targetH;
	var _noPicture = pic.noPicture; // 无画面
	var _buffering = pic.buffering; // 缓冲画面
	var _marker = null; // 当前选中船舶marker
	var cache = cacheMsg.split(":");
	var cacheShipId = cache[0];
	var mapLayer = cache[1];
	
	var map = new BMap.Map("mapContent", { enableMapClick: false }); //
	map.setDefaultCursor("default"); // 鼠标指针默认
	map.setMapStyle(mapPara.style); // 设置地图样式
	map.enableScrollWheelZoom(); // 允许鼠标滑轮操作
	
	// 设置当前视图为ip所在地
	function ipAddress(result){
		map.setCenter(result.name);
	}

	(function (){
		// 初始化地图到当前ip所在地
		map.centerAndZoom(new BMap.Point(new BMap.LocalCity()
				.get(ipAddress)), mapLayer ? mapLayer : 10);
		// 添加地图缩放监听事件
		map.addEventListener("zoomend", getMapZoom);
		asyncSingleShip();
	})();
	
	//每一分钟去刷新数据
	setInterval(function (){
		asyncSingleShip();
	}, 1500 * 60);
	
	function asyncSingleShip() {
		for(var i in _shipIds){
			$.ajax({
				method : "GET",
				url : _rootPath+"/client/monitor/current/refresh?shipID="+_shipIds[i].shipId+ "&" + _sic+"&userName="+userName,
				datatype : "json",
				success : function(data){
					var returnCode = $(data)[0].returnCode;
					var message = $(data)[0].message;
					if (returnCode == "Failure") {
						return false;
					} else {
						shipId_gpsData.put($(data)[0].shipGpsData.shipID,$(data)[0].shipGpsData);
						
						if(_point&&_point.shipID==$(data)[0].shipGpsData.shipID){
							// 更新当前船舶的gps信息
							_point = $(data)[0].shipGpsData; 
							getShipImages(_point);
							
							//更新当前船舶marker
							_marker = new BDPoi(_point, true).createMarker();
							addMarker2(_point.shipID,_marker);
						} else{
							// 异步增加marker
							if(cacheShipId && cacheShipId == $(data)[0].shipGpsData.shipID) {
								cacheShipId = false;
				  				_point = $(data)[0].shipGpsData;
								_marker = new BDPoi(_point, true).createMarker();
								addMarker2(_point.shipID,_marker);
								getShipImages(_point);
								setTimeout(function(){
									map.centerAndZoom(new BDPoi(_point, true).createBDPoi(), mapLayer);
								}, 2000);							
							} else {
								var marker = new BDPoi($(data)[0].shipGpsData, false).createMarker();
								addMarker2($(data)[0].shipGpsData.shipID, marker);
							}
						}
					}
				}
			});
		}
	}
	
	function addMarker2(shipID, marker){
		if(shipId_marker.get(shipID)){
			map.removeOverlay(shipId_marker.get(shipID));
		}
		shipId_marker.put(shipID, marker);
		
		map.addOverlay(marker); // 增加点
		marker.addEventListener("mouseover", showInfo);
		marker.addEventListener("mouseout", hideInfo);
		marker.addEventListener("click", attribute); // 添加事件
		marker.getLabel().addEventListener("click", attribute); // 添加事件
	}
	
	function showInfo(e){
		shipId_gpsData.each(function(key,value,index){
			var poi = value;
			var bdpoi = e.target.getPosition();
			var _t = wgs2bd(poi.gpsLatitude,poi.gpsLongitude);
			
			if(_t[1] == bdpoi.lng && _t[0] == bdpoi.lat){
				var x = e.clientX;
				var y = e.clientY;
				
				$(".ship-call").html(poi.shipName);
				$(".last-speed").html(poi.gpsSpeed + " 节");
				$(".last-time").html(poi.gpsTime);
				$(".ship-simple-info").css({"display" : "block", "left" : x + 10 + "px", "top" : y + 10 + "px"});
				
				if (x + $(".ship-simple-info").width() > $(window).width()) {
		  			$(".ship-simple-info").css({"left" : "auto", "right" : "8px"});
		  		}
				
				if(y + $(".ship-simple-info").height() > $(window).height()){
					$(".ship-simple-info").css("top", $(window).height() - 80);
				}
				return false;
			} 
	    });
	}
	
	function hideInfo(e){
		$(".ship-simple-info").css({"display" : "none"});
	}
	
	function attribute(e){
		var bdpoi = e.target.getPosition();
		shipId_gpsData.each(function(key,value,index){
			var poi = value;
			var _t = wgs2bd(poi.gpsLatitude,poi.gpsLongitude);
			
			if(_t[1] == bdpoi.lng && _t[0] == bdpoi.lat){
				cacheShipId = false;
				if(_marker){
					map.removeOverlay(_marker);
					var marker = new BDPoi(_point, false).createMarker();
					addMarker2(_point.shipID,marker);
				}
				_point = poi;
				_marker = new BDPoi(_point, true).createMarker();
				addMarker2(_point.shipID,_marker);
				getShipImages(_point);
				initFirstActive();
				
				sendMapCenter(userName, _point.shipID, map.getZoom());
				return false;
			} 
	    });
	}

  	// 获取镜头画面
  	function getShipImages(point) {
  		if(point){
  	  		new GetImage().createCurrImage(point);
  		}
  	}
  	
	setStyle();
  	$(window).resize(function(){
  		/** 
  		 * 此处if条件判断主要是手机首页端点击船舶搜索框是弹出键盘，
  		 * 影响手机界面的高度，导致手机端点击搜索列表里的船舶时，
  		 * 船舶不能置中，故点击搜索框时不能重新设置窗体高度。
  		 */
  		if(_isWindowResize){
  			setStyle();
  		}
  		_isWindowResize = true;
  	});
  	
  	// 船舶搜索框点击船舶
  	$(".ship-item", $(".home-ship-detail")).on("click", ".ship", function(event){
  		// event.stopPropagation();
  		var shipId = $(this).attr("data-shipid");
  		var shipInpoints = false;
  		
  		shipId_gpsData.each(function(key,value,index){
  			if(shipId == key){
  				cacheShipId = false;
  				shipInpoints = true;
  				if(_marker){
					map.removeOverlay(_marker);
					var marker = new BDPoi(_point, false).createMarker();
					addMarker2(_point.shipID, marker);
				}
				
  				_point = value;
				_marker = new BDPoi(_point, true).createMarker();
				addMarker2(_point.shipID,_marker);
				getShipImages(_point);
				initFirstActive();
				
				map.centerAndZoom(new BDPoi(_point, true).createBDPoi(), 14);
				sendMapCenter(userName, _point.shipID, map.getZoom());
  		  		return false;
  			}
  		});
  		
  		if(!shipInpoints) {
  			alert($(".power-shipname", $(this)).text() + " 无信号，无权限或设备断电！");
  		}
  	});
  	
  	function getMapZoom(){
  		if(_point){
  			sendMapCenter(userName, _point.shipID, map.getZoom());
  		}
  	}
  	
  	function sendMapCenter(userName, shipId, mapLayer) {
  		$.ajax({
  			type: "post",
  			url: _rootPath + "/client/monitor/current/cache",
  			data: {userName : userName, shipId : shipId, mapLayer : mapLayer},
  			dataType: "json",
  			success: function(data) {
  				var returnCode = $(data)[0].returnCode;
				var message = $(data)[0].message;
				if (returnCode == "Failure") {
					return false;
				} else {
					return true;
				}
			}
  		});
  	}
  	
  	$(".mapgps").on("click", function(){
  		if(_point){
  			var _t = wgs2bd(_point.gpsLatitude, _point.gpsLongitude);
  			var pt = new BMap.Point(_t[1], _t[0]);
  			map.setCenter(pt);
  		}
  	});
  	
  	var dom = document.getElementById("mapContent");
	BMapLib.EventWrapper.addDomListener(dom, "touchend", function(e){
		$("#mobilehomewords").blur().val("");
		$(".ship-item").css("display", "none");
		$(".mobile-account-menu").css("display", "none");
	});
	
	$(".shipimg").on("click", function(){
		_channel = $(this).attr("data-channel");
		
		var w = $(this).width();
		var h = $(this).height();
		var top = $(this).offset().top;
		var left = $(this).offset().left;
		$(".enlarge-ship").attr("src", $(this).attr("src"));
		$(".enlarge-time").text($(".time", $(this).parent()).text());
		$(".enlarge-one").css({
			"display" : "block", 
			"width" : (w - 2) + "px",
			"height" : (h - 2) + "px",
			"top" : (top + 1) + "px", 
			"left" : (left + 1) + "px",
		});
		
		targetW = $(".enlarge-one").width();
		targetH = $(".enlarge-one").height();
	});
	// 点击图片上的时间也放大图片
	$(".time").on("click", function(){
		$(this).parent().next().click();
	});
	
	// web端图片放大
	$("#enlarge-one").on("mousewheel DOMMouseScroll", function(e) {
		e.preventDefault();
		var delta = (e.originalEvent.wheelDelta && (e.originalEvent.wheelDelta > 0 ? 1
				: -1)) || (e.originalEvent.detail && (e.originalEvent.detail > 0 ? -1 : 1));
		var ew = $(this).width();
		var eh = $(this).height();
		var width = $(window).width();
		var height = $(window).height();
		var top = $(this).offset().top;
		var left = $(this).offset().left;
		var newWidth, newHeight, enlargeNum = 0.2; // 缩放基数
		if (delta > 0) {
			// 向上滚
			newWidth = ew + ew * 0.2;
			newHeight = eh + eh * 0.2;
			if(newHeight + top > height){ return; }
			if(newWidth + left > width){ return ; }
			
			$(this).width(newWidth);
			$(this).height(newHeight);
		} else if (delta < 0) {
			// 向下滚
			if(newWidth < targetW || newHeight < targetH){ return; }
			
			newWidth = ew - ew * 0.2;
			newHeight = eh - eh * 0.2;
			if(newWidth < targetW){ newWidth = targetW; }
			if(newHeight < targetH){ newHeight = targetH; }
			$(this).width(newWidth);
			$(this).height(newHeight);
		}
	});
	
	var hammerEvent = new MobileEvent();
	// 移动端滑动事件 
	var swipeHammer = new MobileJS("wrap-image").swipeHammer();
	hammerEvent.setHammer(swipeHammer);
	hammerEvent.swipe().swipeType;

	// 移动端图片移动事件
	var panHammer = new MobileJS("enlarge-one").panHammer();
	hammerEvent.setHammer(panHammer);
	hammerEvent.pan().panType;
	
	// 移动端图片放大事件
	var pinchHammer = new MobileJS("enlarge-one").pinchHammer()
	hammerEvent.setHammer(pinchHammer);
	hammerEvent.pinch().pinchType;
	
	$('.btn-block').click( function() {
		$.get(_rootPath+"/client/login/ajaxLogout", 
		function(data) {
			var returnCode = $(data)[0].returnCode;
			var message = $(data)[0].message;
			if (returnCode == "Failure") {
			} else {
				if (loginType == 'phone') {
					window.JSHook.logout();
				}else if(loginType == 'ios'){
					window.webkit.messageHandlers.JSHook.postMessage({loginName:'',password:'',requestType:'logout'});
					window.location.href = _rootPath+"/client/login/login?loginType=ios";
				}else {
					window.location.href = _rootPath+"/client/login/login";
				}
			}
		});
	});
	
	$('.btn-version').click( function() {
		if (loginType == 'phone') {
			window.JSHook.version();
		}else if(loginType == 'ios'){
			window.webkit.messageHandlers.JSHook.postMessage({versionNo:'',requestType:'versions'});
		}
	});
	
	$(".user-powers").on("click", function(){
		$("#userPowerShips").modal("show");
		var s = initUserPowerShips(_powerDatas);
		$(".power-all").html(s);
	});
	
	$(".arrow-left").on("click", function(){
		$(".wrap-display").fadeIn("fast", function(){
			$(this).css('display', 'block');
		});
		$(this).css('display', 'none');
	});
	
	$(".arrow-right").on("click", function(){
		$(".wrap-display").fadeOut("fast", function(){
			$(this).css('display', 'none');
		});
		$(".arrow-left").css('display', 'block');
	});
	
	function initFirstActive() {
		$(".wrap-gpsnav-item").first().click();
	}
	
	$(".wrap-gpsnav-item").on("click", function(){
		var target = $(this).attr("data-target");
		var hidde = $(this).siblings().attr("data-target");
		
		$(this).addClass("gpsnav-active");
		$(this).siblings().removeClass("gpsnav-active");
		
		$("." + target).css("display", "block");
		$("." + hidde).css("display", "none");
	});
	
	$(".powerinfo").on("click", function() {
		var powerData = getPowerByPoint(_point);
		var s = initShipPower(powerData);
		$(".power-one").html(s);
	});
	
	$(".wrap-powerinfo").on("click", ".power-module", function(){
		var trigger = $(this).attr("data-show");
		
		if(trigger == "0"){
			$(this).attr("data-show", "1");
			$(this).removeClass("down").addClass("up");
			$(this).parent().next("ul").css("display", "block");
			
			$(this).parents("li").siblings().find("ul").css("display", "none");
			$(this).parents("li").siblings().find(".power-module").attr("data-show", "0");
			$(this).parents("li").siblings().find(".power-module").removeClass("up").addClass("down");
			
			$(this).parents(".power-wrap").siblings().find("ul").css("display", "none");
			$(this).parents(".power-wrap").siblings().find(".power-module").attr("data-show", "0");
			$(this).parents(".power-wrap").siblings().find(".power-module").removeClass("up").addClass("down");
		} else {
			$(this).attr("data-show", "0");
			$(this).removeClass("up").addClass("down");
			$(this).parent().next("ul").css("display", "none");
		}
	});
	
});

