package com.eyunda.third.adapters.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.AccountData;
import com.eyunda.third.loaders.SynData_loader;

/**
 * 船舶查找 
 */
public class WalletPayTextWatcher implements TextWatcher {
	private final AutoCompleteTextView mTextView;
	private final OrderAddTYRAdapter mAdapter;
	private CharSequence mLastSearchString;
	private static final int AUTOCOMPLETION_DELAY = 600; // in milliseconds
	private final CountDownTimer mTimer;
	private Context context;
	private String[] evilDummyResults = {};
	
	private HashMap<String,AccountData> allShip;//保存本次请求的备选用户
	
	
	public WalletPayTextWatcher(AutoCompleteTextView autoCompleteTextView,
			OrderAddTYRAdapter autoCompleteAdapter) {
		mTextView = autoCompleteTextView;
		mAdapter = autoCompleteAdapter;
		mTimer = getAutoCompletionTimer();
		context = GlobalApplication.getInstance();
		allShip = new HashMap<String,AccountData>();
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
					synDL.setTimeout(1000);
					//
					Map<String, Object> apiParams = new HashMap<String, Object>();
					apiParams.put("loginName", mLastSearchString);
					//调用查找船舶接口
					String arg0 = synDL.getApiResult("/mobile/wallet/myWallet/findUserAccount", apiParams,"get");
					synDL = null;
					allShip = new HashMap<String,AccountData>(); 
					ConvertData cd= new ConvertData(arg0);
					if(cd!=null&&cd.getReturnCode().equals("Success")){
//						Log.setLog2FileEnabled(true);
//						Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("eyunda",".log"));
//						Log.v(arg0);
						List r = (ArrayList<AccountData>)cd.getContent();
						List<String> list = new ArrayList<String>(); 
						for(int i=0; i<r.size();i++){
							Map<String,Object> tmp =  (HashMap<String,Object>)r.get(i);

							AccountData ship = new AccountData(tmp);
							String shipName = ship.getAccounter()+":"+ship.getAccountNo();
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
//						Toast.makeText(context, "找不到收款账户信息", Toast.LENGTH_SHORT).show();
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
	public AccountData getSelectedShip(String shipName){
		if(null !=allShip && allShip.size()>0){
			AccountData u = allShip.get(shipName);
			if(null != u)return u;
		}
		return null;
	}
}
