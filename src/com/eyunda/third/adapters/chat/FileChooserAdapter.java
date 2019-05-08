package com.eyunda.third.adapters.chat;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hy.client.R;

public class FileChooserAdapter extends BaseAdapter {

	private ArrayList<FileInfo> mFileLists;
	private LayoutInflater mLayoutInflater = null;

	private static ArrayList<String> PPT_SUFFIX = new ArrayList<String>();

	static {
		PPT_SUFFIX.add(".apk");
		PPT_SUFFIX.add(".txt");
		PPT_SUFFIX.add(".ppt");
		PPT_SUFFIX.add(".pptx");
		PPT_SUFFIX.add(".doc");
		PPT_SUFFIX.add(".log");
		PPT_SUFFIX.add(".docx");
		PPT_SUFFIX.add(".pdf");
		PPT_SUFFIX.add(".xls");
		PPT_SUFFIX.add(".xlsx");
		PPT_SUFFIX.add(".jpg");
		PPT_SUFFIX.add(".jpeg");
		PPT_SUFFIX.add(".png");
		PPT_SUFFIX.add(".bmp");
		PPT_SUFFIX.add(".gif");
		PPT_SUFFIX.add(".zip");
		PPT_SUFFIX.add(".rar");
		PPT_SUFFIX.add(".mp3");
		PPT_SUFFIX.add(".wma");
		PPT_SUFFIX.add(".mpg");
		PPT_SUFFIX.add(".mpeg");
		PPT_SUFFIX.add(".avi");
		PPT_SUFFIX.add(".mp4");
		PPT_SUFFIX.add(".rm");
		PPT_SUFFIX.add(".rmvb");
		PPT_SUFFIX.add(".m4a");
		PPT_SUFFIX.add(".amr");
	}

	public FileChooserAdapter(Context context, ArrayList<FileInfo> fileLists) {
		super();
		mFileLists = fileLists;
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFileLists.size();
	}

	@Override
	public FileInfo getItem(int position) {
		// TODO Auto-generated method stub
		return mFileLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			view = mLayoutInflater.inflate(R.layout.filechooser_gridview_item,
					null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		FileInfo fileInfo = getItem(position);
		// TODO

		holder.tvFileName.setText(fileInfo.getFileName());

		if (fileInfo.isDirectory()) { // 文件夹
			holder.imgFileIcon.setImageResource(R.drawable.lfile);
			holder.tvFileName.setTextColor(Color.GRAY);
		} else if (fileInfo.isPPTFile()) { // PPT文件
			holder.imgFileIcon.setImageResource(R.drawable.ic_ppt);
			holder.tvFileName.setTextColor(Color.RED);
		} else { // 未知文件
			holder.imgFileIcon.setImageResource(R.drawable.unknowfile);
			holder.tvFileName.setTextColor(Color.GRAY);
		}
		return view;
	}

	static class ViewHolder {
		ImageView imgFileIcon;
		TextView tvFileName;

		public ViewHolder(View view) {
			imgFileIcon = (ImageView) view.findViewById(R.id.imgFileIcon);
			tvFileName = (TextView) view.findViewById(R.id.tvFileName);
		}
	}

	enum FileType {
		FILE, DIRECTORY;
	}

	// =========================
	// Model
	// =========================
	public static class FileInfo {
		private FileType fileType;
		private String fileName;
		private String filePath;

		public FileInfo(String filePath, String fileName, boolean isDirectory) {
			this.filePath = filePath;
			this.fileName = fileName;
			fileType = isDirectory ? FileType.DIRECTORY : FileType.FILE;
		}

		public boolean isPPTFile() {
			if (fileName.lastIndexOf(".") < 0) // Don't have the suffix
				return false;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if (!isDirectory() && PPT_SUFFIX.contains(fileSuffix))
				return true;
			else
				return false;
		}

		public boolean isDirectory() {
			if (fileType == FileType.DIRECTORY)
				return true;
			else
				return false;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		@Override
		public String toString() {
			return "FileInfo [fileType=" + fileType + ", fileName=" + fileName
					+ ", filePath=" + filePath + "]";
		}
	}

}
