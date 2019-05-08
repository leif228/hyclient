package com.eyunda.third.adapters.chat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.adapters.chat.widget.Sidebar;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.chat.event.OnlineStatusCode;
import com.eyunda.third.domain.enumeric.ApplyStatusCode;
import com.hangyi.tools.CookieImageDownloader;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 简单的好友Adapter实现
 *
 */
public class ContactAdapter extends ArrayAdapter<User> implements
		SectionIndexer {

	private LayoutInflater layoutInflater;
	private EditText query;
	private ImageButton clearSearch;
	private SparseIntArray positionOfSection;
	private SparseIntArray sectionOfPosition;
	private Sidebar sidebar;
	private int res;
	ImageLoader mImageLoader;

	public ContactAdapter(Context context, int resource, List<User> objects,
			Sidebar sidebar) {
		super(context, resource, objects);
		this.res = resource;
		this.sidebar = sidebar;
		layoutInflater = LayoutInflater.from(context);
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance()
				.getImageLoaderConfiguration());
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return position == 0 ? 0 : 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position == 0) {// 搜索框
			if (convertView == null) {
				convertView = layoutInflater.inflate(
						R.layout.eyd_chat_search_bar_with_padding, null);
				query = (EditText) convertView.findViewById(R.id.query);
				clearSearch = (ImageButton) convertView
						.findViewById(R.id.search_clear);
				query.addTextChangedListener(new TextWatcher() {
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						getFilter().filter(s);
						if (s.length() > 0) {
							clearSearch.setVisibility(View.VISIBLE);
							if (sidebar != null)
								sidebar.setVisibility(View.GONE);
						} else {
							clearSearch.setVisibility(View.INVISIBLE);
							if (sidebar != null)
								sidebar.setVisibility(View.VISIBLE);
						}
					}

					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					public void afterTextChanged(Editable s) {
					}
				});
				clearSearch.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						InputMethodManager manager = (InputMethodManager) getContext()
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						if (((Activity) getContext()).getWindow()
								.getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
							if (((Activity) getContext()).getCurrentFocus() != null)
								manager.hideSoftInputFromWindow(
										((Activity) getContext())
												.getCurrentFocus()
												.getWindowToken(),
										InputMethodManager.HIDE_NOT_ALWAYS);
						// 清除搜索框文字
						query.getText().clear();
					}
				});
			}
		} else {
			if (convertView == null) {
				convertView = layoutInflater.inflate(res, null);
			}

			ImageView avatar = (ImageView) convertView
					.findViewById(R.id.avatar);
			TextView unreadMsgView = (TextView) convertView
					.findViewById(R.id.unread_msg_number);
			TextView nameTextview = (TextView) convertView
					.findViewById(R.id.name);
			TextView tvHeader = (TextView) convertView
					.findViewById(R.id.header);
			TextView signature = (TextView) convertView
					.findViewById(R.id.signature);
			User user = getItem(position);
			if (user == null)
				Log.d("ContactAdapter", position + "");
			// 设置nick，demo里不涉及到完整user，用username代替nick显示
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL
					+ user.getUserData().getUserLogo(), avatar,
					MyshipAdapter.displayImageOptions);
			String username = user.getUserData().getNickName();
			String header = user.getHeader();
			String onlineStatus = user.getUserData().getOnlineStatus()
					.toString();
			if (onlineStatus.equals(OnlineStatusCode.online.toString())) {
				signature.setText("[在线]");
				avatar.setAlpha(255);
			} 
//			else if (onlineStatus.equals(OnlineStatusCode.busy.toString())) {
//				signature.setText("[忙碌]");
//				avatar.setAlpha(255);
//			} else if (onlineStatus.equals(OnlineStatusCode.idle.toString())) {
//				signature.setText("[空闲]");
//				avatar.setAlpha(255);
//			} 
			else {
				signature.setText("[离线]");
				avatar.setAlpha(80);// 这里设置透明度，当用户不在线时
			}

			if (position == 0 || header != null
					&& !header.equals(getItem(position - 1).getHeader())) {
				if ("".equals(header)) {
					tvHeader.setVisibility(View.GONE);
				} else {
					tvHeader.setVisibility(View.VISIBLE);
					tvHeader.setText(header);
				}
			} else {
				tvHeader.setVisibility(View.GONE);
			}
			if(!username.equals(""))
			   nameTextview.setText(username);
			else if(!user.getUserData().getTrueName().equals(""))
				nameTextview.setText(user.getUserData().getTrueName());
			else if(!user.getUserData().getLoginName().equals(""))
				nameTextview.setText(user.getUserData().getLoginName());
				
			if (user.getUserData().getApplyStatus() == ApplyStatusCode.apply) {
				unreadMsgView.setBackgroundResource(R.drawable.eyd_chat_red_circle_friendcontact);
				unreadMsgView.setVisibility(View.VISIBLE);
			}else if(user.getUserData().getApplyStatus() == ApplyStatusCode.approve){
				unreadMsgView.setVisibility(View.INVISIBLE);
			}else if(user.getUserData().getApplyStatus() == ApplyStatusCode.reject){
				unreadMsgView.setVisibility(View.INVISIBLE);
			}
		}
		return convertView;
	}

	@Override
	public User getItem(int position) {
		return position == 0 ? new User() : super.getItem(position - 1);
	}

	@Override
	public int getCount() {
		// 有搜索框，count+1
		return super.getCount() + 1;
	}

	public int getPositionForSection(int section) {
		return positionOfSection.get(section);
	}

	public int getSectionForPosition(int position) {
		return sectionOfPosition.get(position);
	}

	@Override
	public Object[] getSections() {
		positionOfSection = new SparseIntArray();
		sectionOfPosition = new SparseIntArray();
		int count = getCount();
		List<String> list = new ArrayList<String>();
		list.add(getContext().getString(R.string.eyd_chat_search_header));
		positionOfSection.put(0, 0);
		sectionOfPosition.put(0, 0);
		for (int i = 1; i < count; i++) {

			String letter = getItem(i).getHeader();
			System.err.println("contactadapter getsection getHeader:" + letter
					+ " name:" + getItem(i).getUserData().getLoginName());
			int section = list.size() - 1;
			if (list.get(section) != null && !list.get(section).equals(letter)) {
				list.add(letter);
				section++;
				positionOfSection.put(section, i);
			}
			sectionOfPosition.put(i, section);
		}
		return list.toArray(new String[list.size()]);
	}

}
