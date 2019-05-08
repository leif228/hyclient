package com.eyunda.third.adapters.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.loaders.SynData_loader;
import com.eyunda.tools.log.FilePathGenerator;
import com.eyunda.tools.log.Log;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

/**
 * 船舶查找 
 */
public class CCTextWatcher implements TextWatcher {
	private final AutoCompleteTextView mTextView;
	private final OrderAddTYRAdapter mAdapter;
	private CharSequence mLastSearchString;
	private static final int AUTOCOMPLETION_DELAY = 600; // in milliseconds
	private final CountDownTimer mTimer;
	private Context context;
	private String[] evilDummyResults = {};
	
	private HashMap<String,ShipData> allShip;//保存本次请求的备选用户
	
	
	public CCTextWatcher(AutoCompleteTextView autoCompleteTextView,
			OrderAddTYRAdapter autoCompleteAdapter) {
		mTextView = autoCompleteTextView;
		mAdapter = autoCompleteAdapter;
		mTimer = getAutoCompletionTimer();
		context = GlobalApplication.getInstance();
		allShip = new HashMap<String,ShipData>();
	}


	@SuppressWarnings("unchecked")
	private CountDownTimer getAutoCompletionTimer() {
		return new CountDownTimer(AUTOCOMPLETION_DELAY, AUTOCOMPLETION_DELAY) {
			@Override
			public void onTick(long l) {
			}

			@Override
			public void onFinish() {
				if (mLastSearchString.length() >= 0) {
					// Do the API call here
					SynData_loader synDL = new SynData_loader();
					//
					Map<String, Object> apiParams = new HashMap<String, Object>();
					apiParams.put("shipKeyWords", mLastSearchString);
					//调用查找船舶接口
					String arg0 = synDL.getApiResult("/mobile/order/findShip", apiParams,"get");
					synDL = null;
					allShip = new HashMap<String,ShipData>(); 
					ConvertData cd= new ConvertData(arg0);
					if(cd.getReturnCode().equals("Success")){
//						Log.setLog2FileEnabled(true);
//						Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("eyunda",".log"));
//						Log.v(arg0);
						List r = (ArrayList<ShipData>)cd.getContent();
						List<String> list = new ArrayList<String>(); 
						for(int i=0; i<r.size();i++){
							Map<String,Object> tmp =  (HashMap<String,Object>)r.get(i);

							ShipData ship = new ShipData(tmp);
							String shipName = ship.getShipName();
							allShip.put(shipName, ship);
							list.add(shipName);
						}
						mAdapter.clear();
						evilDummyResults=list.toArray(new String[1]);
						if (evilDummyResults.length > 0) {
							for (String result : evilDummyResults) {
								mAdapter.add(result);
							}
						}
					}else{
						Toast.makeText(context, "找不到该运输工具信息", Toast.LENGTH_SHORT).show();
					}
					
				}
			}
		};
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1,
			int i2) {
		// empty implementation because we dont need to do anything here
	}

	@Override
	public void onTextChanged(CharSequence currentSearchString, int start,
			int before, int count) {
		if (currentSearchString.length() < mTextView.getThreshold()) {
			cancelCountDownTimer();
			if (!mAdapter.isEmpty())
				mAdapter.clear();
			return;
		}

		mLastSearchString = currentSearchString;
		resetCountDownTimer();
	}

	@Override
	public void afterTextChanged(Editable editable) {
		// empty implementation because we dont need to do anything here
	}

	/**
	 * Resets the AutoCompletion Timer
	 */
	private void resetCountDownTimer() {
		cancelCountDownTimer();
		mTimer.start();
	}

	/**
	 * Cancels the AutoCompletions Timer
	 */
	private void cancelCountDownTimer() {
		mTimer.cancel();
	}
	
	//返回当前选择的ship对象
	public ShipData getSelectedShip(String shipName){
		if(null !=allShip && allShip.size()>0){
			ShipData u = allShip.get(shipName);
			if(null != u)return u;
		}
		return null;
	}
}
