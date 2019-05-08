package com.eyunda.third.activities.user;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.activities.order.MyOrderActivity;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.ImageUtil;
//import com.github.gcacace.signaturepad.views.SignaturePad;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class SignatureActivity extends CommonListActivity {

//    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
	protected DialogUtil dialogUtil;
	protected ProgressDialog dialog;
    //保存路径
    private String path;
    
    private String orderId;//区分来自于用户基本设置还是合同 空为基本设置，不空为来自合同
    DialogUtil dialogOne = new DialogUtil(SignatureActivity.this);
    Data_loader dataLoader;
    File photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eyd_user_activity_signature_pad);
		Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();
		orderId=(String)bundle.getString("orderId","");
		dataLoader = new Data_loader();
		dialogUtil = new DialogUtil(this);
//        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
//        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
//            @Override
//            public void onSigned() {
//                mSaveButton.setEnabled(true);
//                mClearButton.setEnabled(true);
//            }
//
//            @Override
//            public void onClear() {
//                mSaveButton.setEnabled(false);
//                mClearButton.setEnabled(false);
//            }
//        });

        mClearButton = (Button) findViewById(R.id.clear_button);
        mSaveButton = (Button) findViewById(R.id.save_button);

//        mClearButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mSignaturePad.clear();
//            }
//        });

//        mSaveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
//                if("".equalsIgnoreCase(orderId)){
//	                if(addSignatureToGallery(signatureBitmap)) {
//	                	setResult(RESULT_OK,new Intent().putExtra("path", path));
//	                	finish();
//	                    //Toast.makeText(SignatureActivity.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
//	                    
//	                } else {
//	                    //Toast.makeText(SignatureActivity.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
//	                }
//                }else{
//                	try {
//                        photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
//                        saveSignToJPG(signatureBitmap, photo);
//                        //确认提交签名
//    					dialogOne.showDialogFromConfig("提示", "确认要提交您的签名么?", new Handler() {
//							@Override
//							public void handleMessage(Message msg) {
//								super.handleMessage(msg);
//								//异步后台签名
//								Map<String, Object> params = new HashMap<String, Object>();
//								params.put("signatureFile", photo);
//								params.put("id",Long.parseLong(orderId));
//								dataLoader.getApiResult(saveHandler, "/mobile/order/myOrder/signOrder", params, "post");
//							
//							}
//						});
//					} catch (Exception e) {
//						e.printStackTrace();
//						Toast.makeText(SignatureActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//					}
//                }
//            }
//        });
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
      //成功保存
        
        path=photo.getAbsolutePath();
    }
    public void saveSignToJPG(Bitmap bitmap, File photo) throws IOException {
    	Bitmap compbitmap = ImageUtil.scalImage(bitmap);
        Bitmap newBitmap = Bitmap.createBitmap(compbitmap.getWidth(), compbitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(compbitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
      //成功保存
        
        path=photo.getAbsolutePath();
    }
    public boolean addSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(photo);
            mediaScanIntent.setData(contentUri);
            SignatureActivity.this.sendBroadcast(mediaScanIntent);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
	protected void onStart() {
		super.onStart();
		if("".equalsIgnoreCase(orderId)){
			setTitle("签名"); 
		}else{
			setTitle("合同签名"); 
		}
		
	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		
	}
	
	AsyncHttpResponseHandler saveHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart(){
			dialog = dialogUtil.loading("通知", "合同签名中，请稍候...");
		}
		@Override
		public void onSuccess(String arg0) {
			dialog.dismiss();
			ConvertData cd = new ConvertData(arg0);
			Toast.makeText(SignatureActivity.this, cd.getMessage(),Toast.LENGTH_SHORT).show();
			if(cd.getReturnCode().equalsIgnoreCase("success")){
				Intent intent = new Intent(SignatureActivity.this,MyOrderActivity.class);
				startActivity(intent);
				finish();
			}
		}
		
	};

}
