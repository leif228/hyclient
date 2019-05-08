package com.eyunda.main.reg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.entity.FormFile;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;




import com.eyunda.main.CommonActivity;
import com.eyunda.main.Config;
import com.eyunda.main.data.Data_loader;
import com.eyunda.main.data.Image_loader;
import com.eyunda.main.json.DataConvert;
import com.eyunda.main.localdata.LocalData;
import com.eyunda.main.localdata.UserInfo;
import com.eyunda.main.util.Utils;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.part1.data.PartData_loader;
import com.hy.client.R;
import com.ta.TAApplication;
import com.ta.util.http.AsyncHttpResponseHandler;

public class UserInfoActivity extends CommonActivity {
	Button email, phone, logout;
	ImageView user_head;
	DialogUtil dialogUtil;
	RelativeLayout updatepwd,updateqa;
	PartData_loader data;
	Image_loader loader;
ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);
		email = (Button) findViewById(R.id.email);
		phone = (Button) findViewById(R.id.phone);
		logout = (Button) findViewById(R.id.logout);
		data = new PartData_loader();
		dialogUtil = new DialogUtil(this);
		user_head = (ImageView) findViewById(R.id.user_head);
		updatepwd = (RelativeLayout) findViewById(R.id.updatepwd);
		
		updateqa = (RelativeLayout) findViewById(R.id.updateqa);
		loader=new Image_loader(this, (TAApplication) getApplication());
		String headString =LocalData.get((TAApplication) getApplication()).getUserHead();
		if(headString!=null)
			loader.load_normal_Img(headString, user_head);
		updatepwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(UserInfoActivity.this, UpdatePwd.class));
			}
		});
		updateqa.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(UserInfoActivity.this, UpdateQA.class));
			}
		});
		user_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogUtil.showDialogForFile(getPath());
			}
		});
		UserInfo user = LocalData.get((TAApplication) getApplication())
				.getUser();
		phone.setText(user.getName());

		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				LocalData.get((TAApplication) getApplication()).delUser();
				Config.IFLOGIN = false;
				Config.LOGINNAME = "";
				Config.USERID = "";
				data.pushServiceandroidBind(new AsyncHttpResponseHandler(){}, Config.USERID, "", "", "");
				finish();
			}
		});

	}

	
	protected void onStart() {
		super.onStart();
		setTitle("个人中心");
		
	};
	String pathString;

	private String getPath() {

		String dir = Utils.getDiskCacheDir(context, "afinalCache")
				.getAbsolutePath();

		if (!new File(dir).exists())
			new File(dir).mkdirs();
		pathString = dir + File.separator + System.currentTimeMillis() + ".jpg";

		return pathString;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// if (requestCode == 1 || requestCode == 2) {
		// System.out.println(data);
		//
		// }

		if (resultCode == 44) {
			imgpath = pathString;
			if (pathString != null)
				dialogUtil.showDialogFromConfig("提示", "是否上传头像？", handler1);

		} else if (resultCode == RESULT_OK) {
			if (requestCode == 1) {

				if (pathString != null) {
					Intent intent = new Intent(this, EditImage.class);
					intent.putExtra("path", pathString);
					startActivityForResult(intent, requestCode);

				}
			} else if (requestCode == 2) {
				if (data != null) {

					retrunData = data;
					Uri contactData = retrunData.getData();

					String[] proj = { MediaStore.Images.Media.DATA };
					Cursor actualimagecursor = UserInfoActivity.this
							.managedQuery(contactData, proj, null, null, null);
					int actual_image_column_index = actualimagecursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					actualimagecursor.moveToFirst();

					String img_path = actualimagecursor
							.getString(actual_image_column_index);
					if (img_path.endsWith(".jpg") || img_path.endsWith(".png")
							|| img_path.endsWith(".bmp")) {
						Intent intent = new Intent(this, EditImage.class);
						intent.putExtra("path", img_path);
						pathString = img_path;
						startActivityForResult(intent, requestCode);

					}

				}
			}
		} else {
			pathString = null;
		}

	}

	Intent retrunData;
	String imgpath;
	Handler handler1 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				if (imgpath != null) {
				 

					
					data.uploadHead(new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(Throwable arg0) {
							super.onFailure(arg0);
							dialog.dismiss();
						}

						@Override
						public void onStart() {
							super.onStart();
							dialog=dialogUtil.loading( "头像上传中", "请稍候");
						}

						@Override
						public void onSuccess(String arg0) {
							super.onSuccess(arg0);
							Map<String,String>  tm=DataConvert.toMap(arg0);
							//{"result":0,"content":"头像上传成功",
//							"imgUrl":"http://115.28.227.82:9000/assets/headPortait/20140417142756688.octet-stream"}
							dialog.dismiss();
							if (imgpath.endsWith(".jpg") || imgpath.endsWith(".png")
									|| imgpath.endsWith(".bmp")) {
								BitmapFactory.Options option = new BitmapFactory.Options();
								option.inSampleSize = 4;
								Bitmap bm = BitmapFactory.decodeFile(imgpath, option);
								user_head.setImageBitmap(bm);
								LocalData.get((TAApplication) getApplication()).setUserHead(tm.get("imgUrl"));
							}
							imgpath = null;
						}
						
						 
						
						
						

					}, Config.USERID, imgpath);
				
				} else {
					// new PhotoUtil()
					// .createNewBitmapAndCompressByFile(pathString);
					FormFile f = new FormFile(pathString, pathString,
							"filedata", null);
					List<FormFile> list = new ArrayList<FormFile>();
					list.add(f);
					pathString = null;

				}
			} catch (Exception e) {
				Toast.makeText(UserInfoActivity.this, "上传头像失败",
						Toast.LENGTH_LONG).show();
				imgpath = null;
				e.printStackTrace();
			}
		}
	};
}
