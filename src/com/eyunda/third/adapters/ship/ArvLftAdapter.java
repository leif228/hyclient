package com.eyunda.third.adapters.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.ship.ArvLftListActivity;
import com.eyunda.third.activities.ship.ShipArvlftActivity;
import com.eyunda.third.activities.ship.ShipUpdownActivity;
import com.eyunda.third.adapters.ship.MyshipAdapter.lvButtonListener;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.ArvlftCode;
import com.eyunda.third.domain.ship.ArvlftShipData;
import com.eyunda.third.domain.ship.ShipStopData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ArvLftAdapter extends BaseAdapter{
	private Activity context;
	private ArrayList<Map<String, Object>> dataList;
	private btnViewHolder holder;
	private LayoutInflater mInflater;
	private Data_loader data;
	private DialogUtil dialogUtil;
	private ProgressDialog dialog;
	public ArvLftAdapter(Activity arvLftListActivity,
			ArrayList<Map<String, Object>> dataList) {
		this.context = arvLftListActivity;
		this.dataList=dataList;
		data = new Data_loader();
		mInflater = LayoutInflater.from(context);
		dialogUtil = new DialogUtil(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Map<String, Object> appInfo = dataList.get(position);
		if(convertView!=null){
			holder = (btnViewHolder) convertView.getTag();
		}else{
			convertView = mInflater.inflate(R.layout.eyd_dt_item, null);
			holder = new btnViewHolder();

			holder.up= (TextView)convertView.findViewById(R.id.dt_weight); 


			holder.down= (TextView)convertView.findViewById(R.id.downCargo); 


			holder.port = (TextView)convertView.findViewById(R.id.port);  //港口
			holder.arvTime = (TextView)convertView.findViewById(R.id.time1); //到港时间
			holder.leftTime = (TextView)convertView.findViewById(R.id.time2); //离港时间
			holder.goPort = (TextView)convertView.findViewById(R.id.next); //下一港口
			holder.remark= (TextView)convertView.findViewById(R.id.ship_dt_bz);//备注

			holder.xie= (Button)convertView.findViewById(R.id.down);
			holder.zhuang= (Button)convertView.findViewById(R.id.up);
			holder.edit= (Button)convertView.findViewById(R.id.dt_edit);
			holder.delete= (Button)convertView.findViewById(R.id.dt_delete);
			convertView.setTag(holder);
		}

		UserData user = GlobalApplication.getInstance().getUserData();
		
		holder.up.setText((String) appInfo.get("up"));
		holder.down.setText((String) appInfo.get("down"));

		holder.port.setText((String) appInfo.get("port"));
		holder.arvTime.setText((String) appInfo.get("time1"));
		holder.leftTime.setText((String) appInfo.get("time2"));
		holder.goPort.setText((String) appInfo.get("goPort"));
		holder.remark.setText((String) appInfo.get("remark"));
		
		if(user.isChildUser() && !user.getShips().equals("")){
			holder.edit.setOnClickListener(new dtListener(position));
			holder.delete.setOnClickListener(new dtListener(position));
			holder.xie.setOnClickListener(new dtListener(position));
			holder.zhuang.setOnClickListener(new dtListener(position));
		}else{
			holder.edit.setVisibility(View.GONE);
			holder.delete.setVisibility(View.GONE);
			holder.xie.setEnabled(false);
			holder.zhuang.setEnabled(false);
		}

		return convertView;
	}
	private class dtListener implements OnClickListener{
		private int position;
		public dtListener(int position) {
			this.position=position;
		}

		@Override
		public void onClick(View v) {
			final HashMap<String, Object> map = (HashMap<String, Object>) dataList.get(position);
			final HashMap<String, Object> laMap = (HashMap<String, Object>) dataList.get(0);

			switch (v.getId()) {
			case R.id.dt_edit:
				Intent editIntent = new Intent(context,ShipArvlftActivity.class);
				editIntent.putExtra("cur",position);
				editIntent.putExtra("all", dataList);
				context.startActivity(editIntent);
				break;

			case R.id.dt_delete:
				AlertDialog.Builder bulider = new Builder(context);
				bulider.setTitle("刪除提示");
				bulider.setMessage("是否刪除该动态?");
				bulider.setPositiveButton("确定", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {

						deleteLast(map,position);
					}
				});
				bulider.setNegativeButton("取消", null);
				bulider.show();
				break;
			case R.id.down:
				Intent intent = new Intent(context,ShipUpdownActivity.class);
				intent.putExtra("updown","卸货");
				intent.putExtra("data",(ArvlftShipData)map.get("data"));
				context.startActivity(intent);

				break;
			case R.id.up:
				Intent intent2 = new Intent(context,ShipUpdownActivity.class);
				intent2.putExtra("updown","装货");
				intent2.putExtra("data",(ArvlftShipData)map.get("data"));
				context.startActivity(intent2);

				break;
			}

		}

	}
	protected void deleteLast(final HashMap<String, Object> map, final int position) {
		AsyncHttpResponseHandler  handler = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "请求数据中...");
			}
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0);
				if(cd.getReturnCode().equals("Success")){
					context.finish();
					context.startActivity(new Intent(context,ArvLftListActivity.class));
					notifyDataSetChanged();
					Toast.makeText(context, cd.getMessage(), Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(context, cd.getMessage(), Toast.LENGTH_SHORT).show();
				}

			}
			@Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
				dialog.dismiss();
				Toast.makeText(context,"删除失败", Toast.LENGTH_SHORT).show();
			}
		};
		HashMap<String, Object> params = new HashMap<String, Object>();
		//	map.put("mmsi", DynamicAdapter.mmsi);
		params.put("arriveId", map.get("id"));
		data.getApiResult(handler, "/mobile/state/myShip/shipApply/delShipArvlft",params,"get");

	}
	class btnViewHolder{
		public TextView down;
		public TextView up;
		public Button zhuang;
		public Button xie;
		public TextView goPort;
		public TextView leftTime;
		public TextView arvTime;
		public TextView port;
		private TextView remark;
		private Button edit,delete;
	}
}
