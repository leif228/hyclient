package com.eyunda.third.activities.ship;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.activities.ship.widget.MyListView;
import com.eyunda.third.activities.user.CropImageActivity;
import com.eyunda.third.adapters.ship.AttaAdapter;
import com.eyunda.third.domain.ship.ShipAttaData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;
/**
 * 图文上传页
 * @author LiGuang
 *
 */
public class UploadActivity extends ShipinfoActivity {

	private TextView activity_selectimg_send;
	private View layout;
	private PopupWindow popupwindow;
	private String shipId;
	private Button topRight;
	private DialogUtil dialogUtil;
	private ProgressDialog dialog;
	private String imageContent;
	private EditText content;
	private MyListView mListView;
	private AttaAdapter mAdapter;
	private ArrayList<Map<String,Object>> mInfos;
	private Data_loader data =new Data_loader();
	private ImageView selectImage;
	public static final int REQUEST_CODE_GETSYSIMG = 2;
	public static final int REQUEST_CODE_CROPIMG = 3;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectimg);
		Intent intent =getIntent();
	    shipId=  intent.getStringExtra("shipId");
		dialogUtil = new DialogUtil(this);
		Init();
	}
    @Override
    protected void onStart() {
    	super.onStart();
    	menu_upload.setOnClickListener(null);
		menu_basic.setBackgroundColor(Color.parseColor("#3B79C4"));
		menu_upload.setBackgroundColor(0xFF6db7ff);
		setTitle("图片上传");
    }
	public void Init() {
		topRight =(Button)findViewById(R.id.top_commit);
		content =(EditText)findViewById(R.id.image_content);
		mListView =(MyListView)findViewById(R.id.listView);
		selectImage = (ImageView)findViewById(R.id.iv1);
		mAdapter = new AttaAdapter(this, getData()); 
		mListView.setAdapter(mAdapter);	

		activity_selectimg_send = (TextView) findViewById(R.id.faver);
		activity_selectimg_send.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				imageContent = content.getText().toString().trim();
		
				upLoad(picturePath);
			}
		});
		selectImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new PopupWindows(UploadActivity.this, v);

			}
		});
	}

	protected void upLoad(String path) {
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "正在上传中...");

			}
			@Override
			public void onSuccess(String arg) {
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson((String) arg,
						new TypeToken<Map<String, Object>>() {
				}.getType());

				if (map.get("returnCode").equals("Success")) {
					Toast.makeText(UploadActivity.this,"上传"+map.get("message"),
							Toast.LENGTH_SHORT).show();
					dialog.dismiss();
					finish();
					Intent intent =new Intent(getApplicationContext(),UploadActivity.class);
					intent.putExtra("shipId", shipId);
					startActivity(intent);
				} else {

					Toast.makeText(UploadActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onFailure(Throwable arg0,String content) {
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host")){
					Toast.makeText(UploadActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				}	else if (content != null && content.equals("socket time out")) {
					Toast.makeText(UploadActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else{
					Toast.makeText(UploadActivity.this, "上传失败",
							Toast.LENGTH_LONG).show();
				}
			}
		};
		// 传参请求服务
		Map<String, Object> params = new HashMap<String, Object>();
		if(path!=null){
			params.put("shipImageFile", new File(path));
		}
		params.put("shipId", shipId);
		params.put("title",imageContent);
		Log.v("params", params.toString());
		data.getApiResult(handler, "/mobile/ship/myShip/saveDetailInfo", params,"post");
	}
	
	
	private ArrayList<Map<String, Object>> getData() {
		mInfos= new ArrayList<Map<String,Object>>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
        		dialog = dialogUtil.loading("通知", "加载中...");

			}
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();
				Gson gson = new Gson();
				final HashMap<String, Object> rmap = gson.fromJson(
						(String) arg0, new TypeToken<Map<String, Object>>() {}.getType());

				if (rmap.get("returnCode").equals("Success")) {
					Map<String, Object> ma = (Map<String, Object>) rmap.get("content");
					HashMap<String, Object> mm = (HashMap<String, Object>) ma.get("shipData");
					List<Map<String,Object>> atta= (List<Map<String, Object>>) mm.get("myShipAttaDatas");
					if(atta!=null){
						for (int i = 0; i < atta.size(); i++) {
							ShipAttaData  attaData = new ShipAttaData((Map<String, Object>) atta.get(i));
							Map<String,Object> item = new HashMap<String,Object>(); 
							item.put("shipId", shipId);
							item.put("id", attaData.getId().toString());
							item.put("img", attaData.getUrl()); 
							item.put("describtion", attaData.getTitleDes()); 
							mInfos.add(item);
						}
						mAdapter.notifyDataSetChanged();
					}
				}

			}

			@Override
			public void onFailure(Throwable arg0,String content) {
				super.onFailure(arg0,content);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(UploadActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(UploadActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				}
			}
		};

		HashMap<String, Object>  hashMap = new HashMap<String, Object>();
		hashMap.put("id", shipId);
		Log.v("hashMap", hashMap.toString());
		data.getApiResult(handler, "/mobile/ship/myShip/edit",hashMap,"get");

		return mInfos;
	}


	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.MATCH_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, REQUEST_CODE_GETSYSIMG);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";
	private String picturePath;
	//调用系统相机，相片存入相册
	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK && requestCode==TAKE_PICTURE){
			camara(data);
		}
		if(resultCode==RESULT_OK && requestCode==2){
			Uri selectedImage = data.getData();

			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int	columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			// 进入头像裁剪页面
			Intent intent = new Intent(
					UploadActivity.this,
					CropImageActivity.class);
			intent.putExtra("picPath", selectedImage);
			startActivityForResult(intent,REQUEST_CODE_CROPIMG);
		}
		if(resultCode==RESULT_OK && requestCode==REQUEST_CODE_CROPIMG){

			// 裁剪后图片
			String cropedImgPath = data.getStringExtra("cropedImgPath");
			if (cropedImgPath != null) {
				picturePath = cropedImgPath;
				selectImage.setImageBitmap(BitmapFactory
						.decodeFile(cropedImgPath));
			} else {
				Toast.makeText(this, "裁剪图片失败", Toast.LENGTH_SHORT).show();
			}

		}
	}

	public Bitmap camara(Intent data) {  
		String sdStatus = Environment.getExternalStorageState();  
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用  
			Log.i("TestFile", "SD card is not avaiable/writeable right now.");  
			Toast.makeText(this,  "SD卡不存在，请插入SD卡", Toast.LENGTH_LONG).show();  
			return null;  
		}  
		new DateFormat();
		String name = DateFormat.format("yyyyMMdd_hhmmss",  
				Calendar.getInstance(Locale.CHINA))  
				+ ".jpg";  
		Bundle bundle = data.getExtras();  
		Bitmap bitmap = (Bitmap) bundle.get("data");  
		FileOutputStream b = null;  
		File file = new File("sdcard/DCIM/Camera/"); 
		if(!file.exists())
			file.mkdirs();// 创建文件夹  
		String fileName = "sdcard/DCIM/Camera/" + name;  
		picturePath=null;
		picturePath=fileName;
		selectImage.setImageBitmap(bitmap);

		try {  
			b = new FileOutputStream(fileName);  
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件  
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		} finally {  
			try {  
				b.flush();  
				b.close();  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
		return bitmap;  
	}  
	public void back(View view){
		finish();
	}

}
