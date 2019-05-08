package com.eyunda.third.activities.user;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;

//import com.edmodo.cropper.CropImageView;
import com.eyunda.tools.ImageCompress;
import com.hy.client.R;

public class CropImageActivity extends Activity {

	// Static final constants
	private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
	private static final int ROTATE_NINETY_DEGREES = 90;
	private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
	private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";

	// Instance variables
	private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
	private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

	Bitmap croppedImage;
	
	ImageCompress compress;
	ImageCompress.CompressOptions options;

	// Saves the state upon rotating the screen/restarting the activity
	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
		bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
	}

	// Restores the state upon rotating the screen/restarting the activity
	@Override
	protected void onRestoreInstanceState(Bundle bundle) {
		super.onRestoreInstanceState(bundle);
		mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
		mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
	}

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_user_activity_cropimg);
		
		compress = new ImageCompress();
		options = new ImageCompress.CompressOptions();

		final Uri picPath = getIntent().getParcelableExtra("picPath");

		// Initialize components of the app
//		final CropImageView cropImageView = (CropImageView) findViewById(R.id.CropImageView);
		if (picPath != null)
			// 处理图片分辨率太大imageview不能显视出来
			options.uri = picPath;
			options.maxWidth = getWindowManager().getDefaultDisplay()
					.getWidth();
			options.maxHeight = getWindowManager().getDefaultDisplay()
					.getHeight();
			Bitmap bitmap = compress.compressFromUri(this, options);
//			cropImageView.setImageBitmap(bitmap);

		// Sets the rotate button
		final Button rotateButton = (Button) findViewById(R.id.Button_rotate);
//		rotateButton.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
//			}
//		});

		// Sets initial aspect ratio to 10/10, for demonstration purposes
//		cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES,
//				DEFAULT_ASPECT_RATIO_VALUES);

		final Button cropButton = (Button) findViewById(R.id.Button_crop);
		cropButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				croppedImage = cropImageView.getCroppedImage();
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
				String picName = formatter.format(new Date());
				
				String cropedImgPath = saveBitmap(picName, croppedImage);
				setResult(RESULT_OK,new Intent().putExtra("cropedImgPath", cropedImgPath));
				
				finish();
			}
		});
           Button cancelButton = (Button)findViewById(R.id.Button_Cancel);
           cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(picPath,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int	columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				setResult(RESULT_OK,new Intent().putExtra("cropedImgPath", picturePath));

				finish();	
			}
		});
	}

	public String saveBitmap(String picName, Bitmap bm) {
		try {
			File f = new File(Environment.getExternalStorageDirectory() + "/eyunda/img/croped"+picName+".png");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			return f.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
