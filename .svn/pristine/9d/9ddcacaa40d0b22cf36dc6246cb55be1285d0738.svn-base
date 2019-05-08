package com.eyunda.third.activities.chat;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.order.MyOrderActivity;
import com.eyunda.third.activities.order.SimpleOrderActivity;
import com.eyunda.third.adapters.chat.CommonUtils;
import com.eyunda.third.adapters.chat.ExpressionAdapter;
import com.eyunda.third.adapters.chat.ExpressionPagerAdapter;
import com.eyunda.third.adapters.chat.MessageAdapter;
import com.eyunda.third.adapters.chat.VoicePlayClickListener;
import com.eyunda.third.adapters.chat.VoiceRecorder;
import com.eyunda.third.adapters.chat.domain.ChatMessage;
import com.eyunda.third.adapters.chat.domain.ChatMessage.Direct;
import com.eyunda.third.adapters.chat.domain.ChatMessage.Status;
import com.eyunda.third.adapters.chat.domain.ChatRoom;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.adapters.chat.manager.ChatManager;
import com.eyunda.third.adapters.chat.manager.ChatRoomManager;
import com.eyunda.third.adapters.chat.widget.ExpandGridView;
import com.eyunda.third.adapters.chat.widget.PasteEditText;
import com.eyunda.third.adapters.chat.widget.SmileUtils;
import com.eyunda.third.chat.mina.MessageSender;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.chat.ChatMessageData;
import com.eyunda.third.domain.enumeric.UserRoleCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ChatActivity extends CommonListActivity implements OnClickListener {

	private static final int REQUEST_CODE_EMPTY_HISTORY = 1;
	public static final int REQUEST_CODE_CONTEXT_MENU = 2;
	public static final int REQUEST_CODE_COPY_AND_PASTE = 3;
	public static final int REQUEST_CODE_LOCAL = 4;
	public static final int REQUEST_CODE_SELECT_FILE = 5;

	public static final int RESULT_CODE_COPY = 6;
	public static final int RESULT_CODE_DELETE = 7;
	public static final int RESULT_CODE_FORWARD = 8;
	public static final  int REF = 10;

	public static final int RESULT_OK_CHATEMPTY = 9;
	public static final String EXTRA_FILE_CHOOSER = "file_chooser";

	private NewMessageBroadcastReceiver receiver;
	private MessageToSendedBroadcastReceiver toSendedReceiver;
	public MyHandler rhandler = new MyHandler(this);

	private ChatCusListView listView;
	private PasteEditText mEditTextContent;
	private View buttonSetModeKeyboard;
	private View buttonSetModeVoice;
	private View buttonSend;
	private View buttonPressToSpeak;
	private TextView recordingHint;
	private View recordingContainer;
	private VoiceRecorder voiceRecorder;
	private ImageView micImage;
	private ViewPager expressionViewpager;
	private LinearLayout expressionContainer;
	private LinearLayout btnContainer;
	private View more;
	private Drawable[] micImages;
	private ClipboardManager clipboard;
	private InputMethodManager manager;
	private List<String> reslist;
	private ChatRoom chatRoom;
	public static ChatActivity activityInstance = null;
	private User toChatUser;
	private MessageAdapter adapter;
	static int resendPos;

	private ImageView iv_emoticons_normal;
	private ImageView iv_emoticons_checked;
	private RelativeLayout edittext_layout;
	private ProgressBar loadmorePB;
	private Button btnMore;
	public Long playMsgId;
	int pageNo;
	boolean isClearRoom;
	Data_loader data;
	DialogUtil dialogUtil;
	ProgressDialog progressDialog;
	private Handler micImageHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// 切换msg切换图片
			micImage.setImageDrawable(micImages[msg.what]);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 4.0为http请求在主线程中
		// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		// .detectDiskReads().detectDiskWrites().detectNetwork()
		// .penaltyLog().build());
		// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		// .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
		// .penaltyLog().penaltyDeath().build());

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_chat_activity_chat);
		data = new Data_loader();
		dialogUtil = new DialogUtil(this);

		initView();
		setUpView();
	}

	protected void initView() {
		recordingContainer = findViewById(R.id.recording_container);
		listView = (ChatCusListView) findViewById(R.id.list);
		recordingHint = (TextView) findViewById(R.id.recording_hint);
		mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
		micImage = (ImageView) findViewById(R.id.mic_image);
		buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
		buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
		buttonSend = findViewById(R.id.btn_send);
		buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
		expressionViewpager = (ViewPager) findViewById(R.id.vPager);
		expressionContainer = (LinearLayout) findViewById(R.id.ll_face_container);
		btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
		iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
		iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
		loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
		btnMore = (Button) findViewById(R.id.btn_more);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		more = findViewById(R.id.more);
		// 动画资源文件,用于录制语音时
				micImages = new Drawable[] { getResources().getDrawable(R.drawable.record_animate_01),
						getResources().getDrawable(R.drawable.record_animate_02), getResources().getDrawable(R.drawable.record_animate_03),
						getResources().getDrawable(R.drawable.record_animate_04), getResources().getDrawable(R.drawable.record_animate_05),
						getResources().getDrawable(R.drawable.record_animate_06), getResources().getDrawable(R.drawable.record_animate_07),
						getResources().getDrawable(R.drawable.record_animate_08), getResources().getDrawable(R.drawable.record_animate_09),
						getResources().getDrawable(R.drawable.record_animate_10), getResources().getDrawable(R.drawable.record_animate_11),
						getResources().getDrawable(R.drawable.record_animate_12), getResources().getDrawable(R.drawable.record_animate_13),
						getResources().getDrawable(R.drawable.record_animate_14), };
		edittext_layout
				.setBackgroundResource(R.drawable.eyd_chat_input_bar_bg_normal);

		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");

		reslist = getExpressionRes(35);
		// 初始化表情viewpager
		List<View> views = new ArrayList<View>();
		View gv1 = getGridChildView(1);
		View gv2 = getGridChildView(2);
		views.add(gv1);
		views.add(gv2);
		expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
		edittext_layout.requestFocus();
		voiceRecorder = new VoiceRecorder(micImageHandler);
		buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
		mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					edittext_layout
							.setBackgroundResource(R.drawable.eyd_chat_input_bar_bg_active);
				} else {
					edittext_layout
							.setBackgroundResource(R.drawable.eyd_chat_input_bar_bg_normal);
				}

			}
		});
		mEditTextContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edittext_layout
						.setBackgroundResource(R.drawable.eyd_chat_input_bar_bg_active);
				more.setVisibility(View.GONE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				expressionContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
			}
		});
		// 监听文字框
		mEditTextContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)) {
					btnMore.setVisibility(View.GONE);
					buttonSend.setVisibility(View.VISIBLE);
				} else {
					btnMore.setVisibility(View.VISIBLE);
					buttonSend.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	private void setUpView() {
		try {
			activityInstance = this;

			toChatUser = (User) getIntent().getSerializableExtra("toChatUser");
			chatRoom = ChatRoomManager.getInstance().getChatRoom(toChatUser);

			iv_emoticons_normal.setOnClickListener(this);
			iv_emoticons_checked.setOnClickListener(this);

			adapter = new MessageAdapter(this, toChatUser);
			// 显示消息
			listView.setAdapter(adapter);
			// listView.setOnScrollListener(new ListScrollListener());
			int count = listView.getCount();
			if (count > 0) {
				listView.setSelection(count - 1);
			}

			listView.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					hideKeyboard();
					more.setVisibility(View.GONE);
					iv_emoticons_normal.setVisibility(View.VISIBLE);
					iv_emoticons_checked.setVisibility(View.INVISIBLE);
					expressionContainer.setVisibility(View.GONE);
					btnContainer.setVisibility(View.GONE);
					return false;
				}
			});

			listView.setOnRefreshListener(new com.eyunda.third.activities.chat.ChatCusListView.OnRefreshListener() {
				@Override
				public void toRefresh() {
					if(chatRoom!=null)
					   upDateList();
				}
			});

			// 注册接收消息广播
			receiver = new NewMessageBroadcastReceiver();
			IntentFilter intentFilter = new IntentFilter(
					"android.intent.action.NewMessageBroadcast");
			// 设置广播的优先级别大于ChatAllHistoryAtivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
			intentFilter.setPriority(5);
			registerReceiver(receiver, intentFilter);
			// 注册回执消息广播
			toSendedReceiver = new MessageToSendedBroadcastReceiver();
			IntentFilter intentFilter1 = new IntentFilter(
					"android.intent.action.MessageToSendedBroadcast");
			intentFilter1.setPriority(5);
			registerReceiver(toSendedReceiver, intentFilter1);

			if (chatRoom != null) {
				// 把此会话的未读数置为0
				chatRoom.resetUnsetMsgCount();

				isClearRoom = chatRoom.isIfCleared();
				chatRoom.setRdpage(1);
				pageNo = chatRoom.getRdpage();

				// 加载第一页的聊天内容
				chatRoom.getMessages().clear();// 先清空chatroom内所有消息记录
				loadDate(chatRoom.getRoomName(), 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void upDateList() {

		// 这里先判断是否还有下一页
		if (!chatRoom.isHaveNextPage()) {
			listView.onRefreshFinished();
			return; // 没有下一页直接返回
		}
		try {
			// 如果被清空过，下次下拉刷新时为第1页
			if (isClearRoom)
				pageNo = 0;

			pageNo++;
			if (pageNo == 1)
				chatRoom.getMessages().clear();// 先清空chatRoom再增加记录
			loadDate(chatRoom.getRoomName(), pageNo);

			listView.onRefreshFinished();
		} catch (Exception e) {
			Toast.makeText(this, "聊天页面出错了！", Toast.LENGTH_LONG).show();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
			switch (resultCode) {
			case RESULT_CODE_COPY: // 复制消息
				ChatMessage copyMsg = ((ChatMessage) adapter.getItem(data
						.getIntExtra("position", -1)));

				clipboard.setText(SmileUtils.getSmiledText(ChatActivity.this,
						(copyMsg.getContent())));
				// clipboard.setText(copyMsg.getContent());

				break;
			case RESULT_CODE_DELETE: // 删除消息
				ChatMessage deleteMsg = (ChatMessage) adapter.getItem(data
						.getIntExtra("position", -1));
				ChatManager.getInstance().removeMessage(chatRoom, deleteMsg,
						this);

				listView.setSelection(data.getIntExtra("position",
						adapter.getCount()) - 1);

				break;

			default:
				break;
			}
		}
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
				boolean chatbtnSure = data.getBooleanExtra("chatbtnSure", true);
				if (chatbtnSure) {
					if(chatRoom==null)
						return;
					// 清空会话
					chatRoom.clearChatRoom();
					isClearRoom = true;
					chatRoom.setHaveNextPage(true);
					// adapter.refresh();
					rhandler.sendEmptyMessage(REF);
				}
			} else if (requestCode == REQUEST_CODE_LOCAL) {

				/**
				 * 当选择的图片不为空的话，再获取到图片的途径
				 */
				Uri uri = data.getData();
				try {

					String[] pojo = { MediaStore.Images.Media.DATA };

					Cursor cursor = managedQuery(uri, pojo, null, null, null);
					if (cursor != null) {
						ContentResolver cr = this.getContentResolver();
						int colunm_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						String picPath = cursor.getString(colunm_index);
						/***
						 * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，
						 * 你选择的文件就不一定是图片了， 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
						 */
						if (picPath.endsWith("jpg") || picPath.endsWith("png")
								|| picPath.endsWith("jpeg")) {

							ChatMessage imgMsg = createImgSendMessage(picPath);
							if (imgMsg != null) {
								chatRoom.addMessage(imgMsg);
								rhandler.sendEmptyMessage(REF);
								// head_img.setImageBitmap(bitmap);
							} else {
								Toast.makeText(this, "获取用户信息失败！",
										Toast.LENGTH_LONG).show();
							}

						} else {
							alert();
						}
					} else {
						alert();
					}

				} catch (Exception e) {
				}

			} else if (requestCode == REQUEST_CODE_SELECT_FILE) { // 发送选择的文件
//				if (data != null) {
//					Uri uri = data.getData();
//					if (uri != null) {
//						sendFile(uri);
//					}
//				}
				if(resultCode == RESULT_CANCELED){
					toast(getText(R.string.open_file_none));
					return ;
				}
				//获取路径名
				String pptPath = data.getStringExtra(EXTRA_FILE_CHOOSER);
				if(pptPath != null){
					//toast("选择文件: " + pptPath);
					sendFile(pptPath);
				}
				else
					toast(getText(R.string.open_file_failed));

			}
		}
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
				// 粘贴
				if (!TextUtils.isEmpty(clipboard.getText())) {
					try {
						String pasteText = clipboard.getText().toString();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
	}

	public MessageAdapter getAdapter() {
		return adapter;
	}

	public MyHandler getRhandler() {
		return rhandler;
	}

	private void alert() {
		Dialog dialog = new android.app.AlertDialog.Builder(this)
				.setTitle("提示").setMessage("您选择的不是有效的图片")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// picPath = null;
					}
				}).create();
		dialog.show();
	}

	/**
	 * 消息图标点击事件
	 */
	@Override
	public void onClick(View view) {

		try {
			int id = view.getId();
			if (id == R.id.btn_send) {// 点击发送按钮(发文字和表情)
				String s = mEditTextContent.getText().toString();
				sendText(s);
			} else if (id == R.id.btn_picture) {
				selectPicFromLocal(); // 点击图片图标
			} else if (id == R.id.btn_file) {
				selectFileFromLocal(); // 点击文件图标
			} else if (id == R.id.iv_emoticons_normal) { // 点击显示表情框
				more.setVisibility(View.VISIBLE);
				iv_emoticons_normal.setVisibility(View.INVISIBLE);
				iv_emoticons_checked.setVisibility(View.VISIBLE);
				btnContainer.setVisibility(View.GONE);
				expressionContainer.setVisibility(View.VISIBLE);
				hideKeyboard();
			} else if (id == R.id.iv_emoticons_checked) { // 点击隐藏表情框
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				btnContainer.setVisibility(View.VISIBLE);
				expressionContainer.setVisibility(View.GONE);
				more.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void toast(CharSequence hint){
	    Toast.makeText(this, hint , Toast.LENGTH_SHORT).show();
	}

	/**
	 * 从图库获取图片
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}

	/** 选择文件 */
	private void selectFileFromLocal() {
		Intent fileChooserIntent =  new Intent(this , 
				FileChooserActivity.class);
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		    startActivityForResult(fileChooserIntent , REQUEST_CODE_SELECT_FILE);
    	else
    		toast(getText(R.string.sdcard_unmonted_hint));
		
		
//		Intent intent = null;
//		if (Build.VERSION.SDK_INT < 19) {
//			intent = new Intent(Intent.ACTION_GET_CONTENT);
//			intent.setType("*/*");
//			intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//		} else {
//			intent = new Intent(
//					Intent.ACTION_PICK,
//					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//		}
//		startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
	}

	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	/**
	 * 发送语音
	 */
	private void sendVoice(String filePath, String fileName, String length, boolean isResend) {
		File file = new File(filePath);
		if (!file.exists()) {
			return;
		}
		try {
			ChatMessage message = createVoiceSendMessage(file,length);

			chatRoom.addMessage(message);
			adapter.refresh();
			listView.setSelection(listView.getCount() - 1);
			setResult(RESULT_OK);
			// send file
			// sendVoiceSub(filePath, fileName, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 发送文件 */
	private void sendFile(String path) {
		File file = new File(path);
		if (file == null || !file.exists()) {
			Toast.makeText(getApplicationContext(), "文件不存在", 0).show();
			return;
		}
		if (file.length() > 10 * 1024 * 1024) {
			Toast.makeText(getApplicationContext(), "文件不能大于10M", 0).show();
			return;
		}

		// 创建一个文件消息
		ChatMessage fileMsg = createFileSendMessage(file);
		if (fileMsg != null) {
			chatRoom.addMessage(fileMsg);
			rhandler.sendEmptyMessage(REF);
			setResult(RESULT_OK);
		} else {
			Toast.makeText(this, "获取用户信息失败！", Toast.LENGTH_LONG).show();
		}
	}

	
	/**
	 * 发送文本消息
	 */
	private void sendText(String content) {

		if (content.length() > 0) {
			if (!isNetworkConnected(this)) {
				Toast.makeText(this, "获取用户信息失败！", Toast.LENGTH_LONG).show();
				return;
			}

			ChatMessage sendmsg = createSendMessage(content);
			if (sendmsg != null) {

				chatRoom.addMessage(sendmsg);
				chatRoom.setRecentlyTime(sendmsg.getCreateTime());
				chatRoom.setRecentlyTitle(sendmsg.getContent());
				// 通知adapter有消息变动，adapter会根据加入的这条message显示消息
				rhandler.sendEmptyMessage(REF);
				listView.setSelection(listView.getCount() - 1);
				mEditTextContent.setText("");
				// 调mina发送
				MessageSender.getInstance().sendMessageEvent(sendmsg,
						toChatUser);

				setResult(RESULT_OK);
			} else {
				Toast.makeText(this, "获取用户信息失败！", Toast.LENGTH_LONG).show();
			}
		}
	}

	/** 创建文本信息 */
	private ChatMessage createSendMessage(String content) {
		try {
			ChatMessage cm = new ChatMessage();
			cm.setId(System.currentTimeMillis());
			cm.setContent(content);
			cm.setReceiverId(toChatUser.getUserData().getId());
			cm.setDirect(Direct.SEND);
			cm.setType(ChatMessage.Type.TXT);
			cm.setStatus(Status.SENDING);
			cm.setRoomName(chatRoom.getRoomName());
			cm.setCreateTime(Calendar.getInstance());
			if (GlobalApplication.getInstance().getUserData() != null
					&& GlobalApplication.getInstance().getUserData().getId() != 0) {
				cm.setSenderId(GlobalApplication.getInstance().getUserData()
						.getId());
				return cm;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 创建图片信息 */
	private ChatMessage createImgSendMessage(String content) {
		try {
			ChatMessage cm = new ChatMessage();
			cm.setId(System.currentTimeMillis());
			//聊天content协议 IMAGE:localFilePath:remoteFilePath:fileName:fileSize
			cm.setContent(ChatMessage.Type.IMAGE.toString()+":"+content + ":"+":"+":");
			cm.setReceiverId(toChatUser.getUserData().getId());
			cm.setDirect(Direct.SEND);
			cm.setType(ChatMessage.Type.IMAGE);
			cm.setStatus(Status.SENDING);
			cm.setRoomName(chatRoom.getRoomName());
			cm.setCreateTime(Calendar.getInstance());
			if (GlobalApplication.getInstance().getUserData() != null
					&& GlobalApplication.getInstance().getUserData().getId() != 0) {
				cm.setSenderId(GlobalApplication.getInstance().getUserData()
						.getId());
				return cm;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 创建文件信息 */
	private ChatMessage createFileSendMessage(File file) {
		try {
			ChatMessage cm = new ChatMessage();
			cm.setId(System.currentTimeMillis());
			//聊天content协议 FILE:localFilePath:remoteFilePath:fileName:fileSize
			cm.setContent(ChatMessage.Type.FILE.toString()+":"+file.getAbsolutePath() + ":" +":"+ file.getName() + ":"
					+ file.length());
			cm.setReceiverId(toChatUser.getUserData().getId());
			cm.setDirect(Direct.SEND);
			cm.setType(ChatMessage.Type.FILE);
			cm.setStatus(Status.SENDING);
			cm.setRoomName(chatRoom.getRoomName());
			cm.setCreateTime(Calendar.getInstance());
			if (GlobalApplication.getInstance().getUserData() != null
					&& GlobalApplication.getInstance().getUserData().getId() != 0) {
				cm.setSenderId(GlobalApplication.getInstance().getUserData()
						.getId());
				return cm;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/** 创建语音信息 */
	private ChatMessage createVoiceSendMessage(File file, String length) {
		ChatMessage cm = new ChatMessage();
		cm.setId(System.currentTimeMillis());
		//聊天content协议 VOICE:localFilePath:remoteFilePath:fileName:fileSize
		cm.setContent(ChatMessage.Type.VOICE.toString()+":"+file.getAbsolutePath() + ":" +":"+file.getName() + ":"
				+ length);
		cm.setReceiverId(toChatUser.getUserData().getId());
		cm.setDirect(Direct.SEND);
		cm.setType(ChatMessage.Type.VOICE);
		cm.setStatus(Status.SENDING);
		cm.setRoomName(chatRoom.getRoomName());
		cm.setCreateTime(Calendar.getInstance());
		if (GlobalApplication.getInstance().getUserData() != null
				&& GlobalApplication.getInstance().getUserData().getId() != 0) {
			cm.setSenderId(GlobalApplication.getInstance().getUserData()
					.getId());
			return cm;
		} else {
			return null;
		}
	}

	/**
	 * 显示键盘图标
	 */
	public void setModeKeyboard(View view) {
		edittext_layout.setVisibility(View.VISIBLE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeVoice.setVisibility(View.VISIBLE);
		// mEditTextContent.setVisibility(View.VISIBLE);
		mEditTextContent.requestFocus();
		// buttonSend.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.GONE);
		if (TextUtils.isEmpty(mEditTextContent.getText())) {
			btnMore.setVisibility(View.VISIBLE);
			buttonSend.setVisibility(View.GONE);
		} else {
			btnMore.setVisibility(View.GONE);
			buttonSend.setVisibility(View.VISIBLE);
		}
	}
	/**
	 * 显示语音图标按钮
	 */
	public void setModeVoice(View view) {
		hideKeyboard();
		edittext_layout.setVisibility(View.GONE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeKeyboard.setVisibility(View.VISIBLE);
		buttonSend.setVisibility(View.GONE);
		btnMore.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.VISIBLE);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		btnContainer.setVisibility(View.VISIBLE);
		expressionContainer.setVisibility(View.GONE);

	}

	/**
	 * 点击清空聊天记录
	 * 
	 * @param view
	 */
	public void emptyHistory(View view) {
		startActivityForResult(
				new Intent(this, AlertDialog.class)
						.putExtra("titleIsCancel", true)
						.putExtra("msg", "是否清空所有聊天记录").putExtra("cancel", true),
				REQUEST_CODE_EMPTY_HISTORY);
	}

	/**
	 * 显示或隐藏图标按钮页
	 * 
	 * @param view
	 */
	public void more(View view) {
		if (more.getVisibility() == View.GONE) {
			System.out.println("more gone");
			hideKeyboard();
			more.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			expressionContainer.setVisibility(View.GONE);
		} else {
			if (expressionContainer.getVisibility() == View.VISIBLE) {
				expressionContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.VISIBLE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
			} else {
				more.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 点击文字输入框
	 * 
	 * @param v
	 */
	public void editClick(View v) {
		listView.setSelection(listView.getCount() - 1);
		if (more.getVisibility() == View.VISIBLE) {
			more.setVisibility(View.GONE);
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
		}
	}
	private PowerManager.WakeLock wakeLock;

	/**
	 * 按住说话listener
	 */
	class PressToSpeakListen implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!CommonUtils.isExitsSdcard()) {
					Toast.makeText(ChatActivity.this, "发送语音需要sdcard支持！", Toast.LENGTH_SHORT).show();
					return false;
				}
				try {
					v.setPressed(true);
					wakeLock.acquire();
					if (VoicePlayClickListener.isPlaying)
						VoicePlayClickListener.currentPlayListener.stopPlayVoice();
					recordingContainer.setVisibility(View.VISIBLE);
					recordingHint.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
					voiceRecorder.startRecording(null, toChatUser.getUserData().getLoginName(), getApplicationContext());
				} catch (Exception e) {
					e.printStackTrace();
					v.setPressed(false);
					if (wakeLock.isHeld())
						wakeLock.release();
					if(voiceRecorder != null)
						voiceRecorder.discardRecording();
					recordingContainer.setVisibility(View.INVISIBLE);
					Toast.makeText(ChatActivity.this, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
					return false;
				}

				return true;
			case MotionEvent.ACTION_MOVE: {
				if (event.getY() < 0) {
					recordingHint.setText(getString(R.string.release_to_cancel));
					recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
				} else {
					recordingHint.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
				}
				return true;
			}
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
				recordingContainer.setVisibility(View.INVISIBLE);
				if (wakeLock.isHeld())
					wakeLock.release();
				if (event.getY() < 0) {
					// discard the recorded audio.
					voiceRecorder.discardRecording();

				} else {
					// stop recording and send voice file
					try {
						int length = voiceRecorder.stopRecoding();
						System.err.println("voiceLength:"+length);
						if (length > 0) {
							sendVoice(voiceRecorder.getVoiceFilePath(), voiceRecorder.getVoiceFileName(toChatUser.getUserData().getLoginName()),
									Integer.toString(length), false);
						} else {
							Toast.makeText(getApplicationContext(), "录音时间太短", 0).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(ChatActivity.this, "发送失败，请检测服务器是否连接", Toast.LENGTH_SHORT).show();
					}

				}
				return true;
			default:
				recordingContainer.setVisibility(View.INVISIBLE);
				if(voiceRecorder != null)
					voiceRecorder.discardRecording();
				return false;
			}
		}
	}

	/**
	 * 获取表情的gridview的子view
	 */
	private View getGridChildView(int i) {
		View view = View.inflate(this, R.layout.eyd_chat_expression_gridview,
				null);
		ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		if (i == 1) {
			List<String> list1 = reslist.subList(0, 20);
			list.addAll(list1);
		} else if (i == 2) {
			list.addAll(reslist.subList(20, reslist.size()));
		}
		list.add("eyd_chat_delete_expression");
		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this,
				1, list);
		gv.setAdapter(expressionAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String filename = expressionAdapter.getItem(position);
				try {
					// 文字输入框可见时，才可输入表情
					// 按住说话可见，不让输入表情
					if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

						if (filename != "eyd_chat_delete_expression") { // 不是删除键，显示表情
							// 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
							Class clz = Class
									.forName("com.eyunda.third.adapters.chat.widget.SmileUtils");
							Field field = clz.getField(filename);
							mEditTextContent.append(SmileUtils.getSmiledText(
									ChatActivity.this, (String) field.get(null)));
						} else { // 删除文字或者表情
							if (!TextUtils.isEmpty(mEditTextContent.getText())) {

								int selectionStart = mEditTextContent
										.getSelectionStart();// 获取光标的位置
								if (selectionStart > 0) {
									String body = mEditTextContent.getText()
											.toString();
									String tempStr = body.substring(0,
											selectionStart);
									int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
									if (i != -1) {
										CharSequence cs = tempStr.substring(i,
												selectionStart);
										if (SmileUtils.containsKey(cs
												.toString()))
											mEditTextContent.getEditableText()
													.delete(i, selectionStart);
										else
											mEditTextContent.getEditableText()
													.delete(selectionStart - 1,
															selectionStart);
									} else {
										mEditTextContent.getEditableText()
												.delete(selectionStart - 1,
														selectionStart);
									}
								}
							}

						}
					}
				} catch (Exception e) {
				}

			}
		});
		return view;
	}

	public List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;

			reslist.add(filename);

		}
		return reslist;

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (toChatUser != null){
			if(!"".equals(toChatUser.getUserData().getNickName()))
			   setTitle(toChatUser.getUserData().getNickName());
			else if(!"".equals(toChatUser.getUserData().getTrueName()))
				setTitle(toChatUser.getUserData().getTrueName());
			else if(!"".equals(toChatUser.getUserData().getLoginName()))
				setTitle(toChatUser.getUserData().getLoginName());
		}
		UserData ud = GlobalApplication.getInstance().getUserData();
		if(ud!=null&&ud.getRoleCodes().size()==2
				&&ud.getRoleCodes().contains(UserRoleCode.sailor)&&ud.getRoleCodes().contains(UserRoleCode.member)){
			//船员不能查看合同
		}else if(ud!=null && !ud.getRoleCodes().contains(UserRoleCode.broker) && !ud.getRoleCodes().contains(UserRoleCode.handler)){
			//只能查看合同列表
			
			setRight("签约", new OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(ChatActivity.this,MyOrderActivity.class));
				}
			});
		}else if(ud!=null && !ud.getId().equals(toChatUser.getUserData().getId()) 
				&& (ud.getRoleCodes().contains(UserRoleCode.broker)||ud.getRoleCodes().contains(UserRoleCode.handler))){
			//能起草合同
			
			setRight("签约", new OnClickListener() {
				@Override
				public void onClick(View v) {
//				emptyHistory(v);
					startActivity(new Intent(ChatActivity.this,SimpleOrderActivity.class)
					.putExtra("ownerId", toChatUser!=null?toChatUser.getUserData().getId():0L));
				}
			});
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		unregisterReceiver(toSendedReceiver);
		if(chatRoom!=null){
			chatRoom.setIfCleared(isClearRoom);
			chatRoom.setRdpage(pageNo);
		}

		GlobalApplication.getInstance().setChatMsgNotifyed(false);// 当前聊天页面退出后设置为可以接收新聊天消息
	}

	@Override
	protected void onResume() {
		super.onResume();
		// adapter.refresh();
		rhandler.sendEmptyMessage(REF);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// if (wakeLock.isHeld())
		// wakeLock.release();
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 覆盖手机返回键
	 */
	@Override
	public void onBackPressed() {
		// if (more.getVisibility() == View.VISIBLE) {
		// more.setVisibility(View.GONE);
		// iv_emoticons_normal.setVisibility(View.VISIBLE);
		// iv_emoticons_checked.setVisibility(View.INVISIBLE);
		// } else {
		// super.onBackPressed();
		// }
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// 点击notification bar进入聊天页面，保证只有一个聊天页面
		User notiftochatUser = (User) intent.getSerializableExtra("user");
		if (notiftochatUser.getUserData().getId() == toChatUser.getUserData()
				.getId())
			super.onNewIntent(intent);
		else {
			finish();
			startActivity(intent);
		}

	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected void loadDate(String roomName, int pageNo) {
		final Map<String, Object> m = new HashMap<String, Object>();
		final AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();

				progressDialog = dialogUtil.loading("正在初始化!", "请稍候...");
			}
			@Override
			public void onSuccess(String content) {
				progressDialog.dismiss();
				try {
					Gson gson = new Gson();
					HashMap<String, Object> map = gson.fromJson(
							(String) content, new TypeToken<Map<String, Object>>() {
							}.getType());
					if (map.get("returnCode").equals("Success")) {
						if (map.containsKey(ApplicationConstants.CONTENTMD5CHANGED)){
							boolean contentMD5Changed = (Boolean) map.get(ApplicationConstants.CONTENTMD5CHANGED);
							SharedPreferencesUtils s = new SharedPreferencesUtils("/mobile/chat/chatMsgs", m);
							if(contentMD5Changed&&NetworkUtils.isNetworkAvailable()){
								s.setParam(content);
							}else{
								String localJsion = s.getParam();
								map = gson.fromJson(localJsion, new TypeToken<Map<String, Object>>() {
								}.getType());
							}
						} 
						
						Map<String, Object> contentMap = (Map<String, Object>) map
								.get("content");
						List<ChatMessageData> chatMsgDataMap = (List<ChatMessageData>) contentMap
								.get("chatMsgDatas");
						boolean isHaveNextPage = (Boolean) contentMap
								.get("isHaveNextPage");
						if(chatRoom!=null)
							chatRoom.setHaveNextPage(isHaveNextPage);
						if (chatMsgDataMap != null && chatMsgDataMap.size() > 0) {
							List<ChatMessageData> chatMsgDatas = new ArrayList<ChatMessageData>();
							for (int i = 0; i < chatMsgDataMap.size(); i++) {
								ChatMessageData data = new ChatMessageData(
										(Map<String, Object>) chatMsgDataMap.get(i));
								chatMsgDatas.add(data);
							}
							for (ChatMessageData d : chatMsgDatas) {
								ChatMessage chatMessage = ChatManager.getInstance()
										.toChangLocal(d);
								if(chatRoom!=null)
									chatRoom.addMessage(chatMessage);
							}
							rhandler.sendEmptyMessage(REF);
							isClearRoom = false;
							// adapter.refresh();
						}
					} else {
						ChatActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(ChatActivity.this,
										"加载消息失败", 1)
										.show();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				progressDialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ChatActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
			}
		};

		
		m.put("toUserId", toChatUser.getUserData().getId());
//		m.put("roomName", roomName);
		m.put("pageNo", pageNo);
		data.getApiResult(handler, "/mobile/chat/chatMsgs", m, "get");
	}

	/**
	 * 消息广播接收者
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String roomName = intent.getStringExtra("roomName");
			if(chatRoom==null)
				return;
			if (chatRoom.getRoomName().equals(roomName)) {
				GlobalApplication.getInstance().setChatMsgNotifyed(true);// 在当前聊天页面不在通知有新消息过来
				chatRoom.setUnreadMsgCount(0);
				// adapter.refresh();
				rhandler.sendEmptyMessage(REF);
				listView.setSelection(listView.getCount() - 1);
				// 记得把广播给终结掉
				abortBroadcast();
			}
		}
	}

	private class MessageToSendedBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Long msgid = intent.getLongExtra("msgid", 0L);
			String status = intent.getStringExtra("status");
			if(chatRoom==null)
				return;
			List<ChatMessage> msgs = chatRoom.getMessages();
			for (ChatMessage cm : msgs) {
				if (msgid.equals(cm.getId())) {
					if (status.equals(Status.SUCCESS.toString()))
						cm.setStatus(ChatMessage.Status.SUCCESS);
					else
						cm.setStatus(ChatMessage.Status.FAIL);
				}
			}
			// 通知adapter有新消息，更新ui
			rhandler.sendEmptyMessage(REF);
			// 记得把广播给终结掉
			// abortBroadcast();
		}
	}

	public static class MyHandler extends Handler {
		// WeakReference<ChatActivity> mActivity;
		ChatActivity mActivity;

		public MyHandler(ChatActivity activity) {
			// mActivity = new WeakReference<ChatActivity>(activity);
			mActivity = activity;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// ChatActivity mainActivity = mActivity.get();
			switch (msg.what) {
			case REF:
				mActivity.adapter.refresh();
				// mainActivity.adapter.refresh();
				// mainActivity.listView.onRefreshFinished();
				break;
			}
		}
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub

	}

}
