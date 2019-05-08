package com.eyunda.third.adapters.chat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Environment;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.chat.ChatActivity;
import com.eyunda.third.activities.chat.ContextMenu;
import com.eyunda.third.activities.chat.ShowBigImage;
import com.eyunda.third.activities.chat.ShowNormalFileActivity;
import com.eyunda.third.adapters.chat.domain.ChatMessage;
import com.eyunda.third.adapters.chat.domain.ChatMessage.Status;
import com.eyunda.third.adapters.chat.domain.ChatMessage.Type;
import com.eyunda.third.adapters.chat.domain.ChatRoom;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.adapters.chat.manager.ChatRoomManager;
import com.eyunda.third.adapters.chat.widget.DateUtils;
import com.eyunda.third.adapters.chat.widget.SmileUtils;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.chat.mina.MessageSender;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.ImageCompress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

public class MessageAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Activity activity;
	private ChatRoom chatRoom;
	private User toChatUser;
	private Context context;
	private Map<Long, Timer> timers = new Hashtable<Long, Timer>();
	ImageLoader mImageLoader;
	ImageCompress compress;
	ImageCompress.CompressOptions options;
	SharedPreferences sp;
	String userLogo;

	public MessageAdapter(Context context, User toChatUser) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		activity = (Activity) context;
		this.toChatUser = toChatUser;
		chatRoom = ChatRoomManager.getInstance().getChatRoom(toChatUser);
		// msgs=chatRoom.getAllMessagesSort();
		compress = new ImageCompress();
		options = new ImageCompress.CompressOptions();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance()
				.getImageLoaderConfiguration());

		sp = context.getSharedPreferences("eyundaBindingCode",
				Context.MODE_PRIVATE);
		userLogo = sp.getString("userLogo", "");
	}

	/** 更新adaper数据 */
	// @SuppressWarnings("unchecked")
	// public void updataMsgs(ArrayList<ChatMessage> msgs){
	// if(msgs!=null){
	// this.msgs=(ArrayList<ChatMessage>) msgs;
	// }
	// }

	/**
	 * 获取item数
	 */
	public int getCount() {
		return chatRoom!=null?chatRoom.getMsgCount():0;
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		try {
			if(chatRoom!=null)
				chatRoom.getAllMessagesSort();
			notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ChatMessage getItem(int position) {
		return chatRoom!=null?chatRoom.getMessage(position):null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ChatMessage message = getItem(position);
		if(message==null)
			return convertView;
		ViewHolder holder;
		// if (convertView == null) {
		holder = new ViewHolder();
		convertView = createViewByMessage(message, position);

		if (message.getType() == ChatMessage.Type.IMAGE) {
			try {
				holder.iv = ((ImageView) convertView
						.findViewById(R.id.iv_sendPicture));
				holder.head_iv = (ImageView) convertView
						.findViewById(R.id.iv_userhead);
				holder.tv = (TextView) convertView
						.findViewById(R.id.percentage);
				holder.pb = (ProgressBar) convertView
						.findViewById(R.id.progressBar);
				holder.staus_iv = (ImageView) convertView
						.findViewById(R.id.msg_status);
				holder.tv_userId = (TextView) convertView
						.findViewById(R.id.tv_userid);
			} catch (Exception e) {
			}

		} else if (message.getType() == ChatMessage.Type.TXT) {

			try {
				holder.pb = (ProgressBar) convertView
						.findViewById(R.id.pb_sending);
				holder.staus_iv = (ImageView) convertView
						.findViewById(R.id.msg_status);
				holder.head_iv = (ImageView) convertView
						.findViewById(R.id.iv_userhead);
				// 这里是文字内容
				holder.tv = (TextView) convertView
						.findViewById(R.id.tv_chatcontent);
				holder.tv_userId = (TextView) convertView
						.findViewById(R.id.tv_userid);
			} catch (Exception e) {
			}

		} else if (message.getType() == ChatMessage.Type.FILE) {
			try {
				holder.head_iv = (ImageView) convertView
						.findViewById(R.id.iv_userhead);
				holder.tv_file_name = (TextView) convertView
						.findViewById(R.id.tv_file_name);
				holder.tv_file_size = (TextView) convertView
						.findViewById(R.id.tv_file_size);
				holder.pb = (ProgressBar) convertView
						.findViewById(R.id.pb_sending);
				holder.staus_iv = (ImageView) convertView
						.findViewById(R.id.msg_status);
				holder.tv_file_download_state = (TextView) convertView
						.findViewById(R.id.tv_file_state);
				holder.ll_container = (LinearLayout) convertView
						.findViewById(R.id.ll_file_container);
				// 这里是进度值
				holder.tv = (TextView) convertView
						.findViewById(R.id.percentage);
			} catch (Exception e) {
			}
			try {
				holder.tv_userId = (TextView) convertView
						.findViewById(R.id.tv_userid);
			} catch (Exception e) {
			}

		}else if (message.getType() == ChatMessage.Type.VOICE) {
			try {
				holder.iv = ((ImageView) convertView.findViewById(R.id.iv_voice));
				holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
				holder.tv = (TextView) convertView.findViewById(R.id.tv_length);
				holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
				holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
				holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
				holder.iv_read_status = (ImageView) convertView.findViewById(R.id.iv_unread_voice);
			} catch (Exception e) {
			}
		} 
		// convertView.setTag(holder);
		//
		// } else {
		// holder = (ViewHolder) convertView.getTag();
		// }

		switch (message.getType()) {
		// 根据消息type显示item
		case IMAGE: // 图片
			handleImageMessage(message, holder, position, convertView);
			break;
		case TXT: // 文本
			handleTextMessage(message, holder, position);
			break;
		case FILE: // 一般文件
			handleFileMessage(message, holder, position, convertView);
			break;
		case VOICE: // 语音
			handleVoiceMessage(message, holder, position, convertView);
			break;
		default:
			break;
		}

		// sender和receiver分别显视头像
		if (message.getSenderId().equals(
				chatRoom.getToChatUser().getUserData().getId()))
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL
					+ chatRoom.getToChatUser().getUserData().getUserLogo(),
					holder.head_iv, MyshipAdapter.displayImageOptions);
		else
			mImageLoader.displayImage(
					ApplicationConstants.IMAGE_URL + userLogo, holder.head_iv,
					MyshipAdapter.displayImageOptions);

		TextView timestamp = (TextView) convertView
				.findViewById(R.id.timestamp);

		if (position == 0) {
			timestamp.setText(DateUtils.getTimestampString(message
					.getCreateTime().getTime()));
			timestamp.setVisibility(View.VISIBLE);
		} else {
			// 两条消息时间离得如果稍长，显示时间
			if (DateUtils.isCloseEnough(message.getCreateTime().getTime()
					.getTime(), chatRoom.getMessage(position - 1)
					.getCreateTime().getTime().getTime())) {
				timestamp.setVisibility(View.GONE);
			} else {
				timestamp.setText(DateUtils.getTimestampString(message
						.getCreateTime().getTime()));
				timestamp.setVisibility(View.VISIBLE);
			}
		}
		return convertView;

	}

	private View createViewByMessage(ChatMessage message, int position) {
		switch (message.getType()) {
		case IMAGE:
			return message.direct == ChatMessage.Direct.RECEIVE ? inflater
					.inflate(R.layout.eyd_chat_row_received_picture, null)
					: inflater
							.inflate(R.layout.eyd_chat_row_sent_picture, null);
		case FILE:
			return message.direct == ChatMessage.Direct.RECEIVE ? inflater
					.inflate(R.layout.row_received_file, null) : inflater
					.inflate(R.layout.row_sent_file, null);
		case VOICE:
			return message.direct == ChatMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_voice, null) : inflater.inflate(
					R.layout.row_sent_voice, null);
		default:
			return message.getDirect() == ChatMessage.Direct.RECEIVE ? inflater
					.inflate(R.layout.eyd_chat_row_received_message, null)
					: inflater
							.inflate(R.layout.eyd_chat_row_sent_message, null);
		}

	}

	private void handleVoiceMessage(final ChatMessage message, final ViewHolder holder,
			final int position, View convertView) {

		try {
			String voice = message.getContent();
			String[] arr = voice.split(":");
			final String remotePath = arr[2];
			final String voiceName = arr[3];
			String voiceLength = arr[4];
			holder.tv.setText(voiceLength + "\"");
			holder.iv.setOnClickListener(new VoicePlayClickListener(message, holder.iv, holder.iv_read_status, this, activity, ""));
			holder.iv.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					activity.startActivityForResult(
							(new Intent(activity, ContextMenu.class)).putExtra("position", position).putExtra("type",
									ChatMessage.Type.VOICE.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
					return true;
				}
			});
			//正在播放语音
			if (((ChatActivity)activity).playMsgId != null
					&& ((ChatActivity)activity).playMsgId.equals(message
							.getId())&&VoicePlayClickListener.isPlaying) {
				AnimationDrawable voiceAnimation;
				if (message.direct == ChatMessage.Direct.RECEIVE) {
					holder.iv.setImageResource(R.anim.voice_from_icon);
				} else {
					holder.iv.setImageResource(R.anim.voice_to_icon);
				}
				voiceAnimation = (AnimationDrawable) holder.iv.getDrawable();
				voiceAnimation.start();
			} else {
				//发送与收到消息图片显视
				if (message.direct == ChatMessage.Direct.RECEIVE) {
					holder.iv.setImageResource(R.drawable.chatfrom_voice_playing);
				} else {
					holder.iv.setImageResource(R.drawable.chatto_voice_playing);
				}
			}
			
			if (message.direct == ChatMessage.Direct.RECEIVE) {
				//如果消息已读
				if (message.isAcked) {
					// 隐藏语音未读标志
					holder.iv_read_status.setVisibility(View.INVISIBLE);
				} else {
					holder.iv_read_status.setVisibility(View.VISIBLE);
				}
				System.err.println("it is receive msg");
				if (message.status == ChatMessage.Status.INPROGRESS) {
					holder.pb.setVisibility(View.VISIBLE);
					//去服务端下载
					new Thread(new Runnable(){
						HttpURLConnection conn ;
						InputStream is ;
						FileOutputStream fos ;
						@Override
						public void run() {
							File file =null;
							try {
								String sessionId = "";
								if (GlobalApplication.getInstance().getUserData() != null) {
									sessionId = GlobalApplication.getInstance().getUserData().getSessionId();
								}
								if("".equals(sessionId)){
									activity.runOnUiThread(new Runnable() {
										public void run() {
											Toast.makeText(activity, "sessionId为空",
													0).show();
										}
									});
								}
								
								URL url = new URL(ApplicationConstants.VOICE_URL
										+ remotePath+"&sessionId="+sessionId);

								conn = (HttpURLConnection) url
										.openConnection();
								conn.connect();
								is = conn.getInputStream();
								File f = new File(Environment.getExternalStorageDirectory()
										+ "/eyunda/download/voice/");
								if (!f.exists()) {
									f.mkdir();
								}
								file = new File(Environment.getExternalStorageDirectory()
										+ "/eyunda/download/voice/" + voiceName);
								fos = new FileOutputStream(file);

								byte buf[] = new byte[1024];
								do {
									int numread = is.read(buf);
									if (numread <= 0) {
										break;
									}
									fos.write(buf, 0, numread);
								} while (true);
								message.status=ChatMessage.Status.SUCCESS;
								updateSendedView(message, holder);//更新视图
								fos.close();//释放资源
								is.close();
								conn.disconnect();
							} catch (Exception e) {
								e.printStackTrace();
								activity.runOnUiThread(new Runnable() {
									public void run() {
										try {
											File f = new File(Environment.getExternalStorageDirectory()
													+ "/eyunda/download/voice/" + voiceName);
											if(f!=null&&f.exists())
											       f.delete();
											message.status=ChatMessage.Status.FAIL;
											updateSendedView(message, holder);
											fos.close();
											is.close();
											conn.disconnect();
											//updateSendedView(message, holder);
//									Toast.makeText(activity, "下载文件失败: ",
//											0).show();
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								});
							}
						}}).start();

				} else {
					holder.pb.setVisibility(View.INVISIBLE);
				}
				return;
			}

			// until here, deal with send voice msg
			switch (message.status) {
			case SUCCESS:
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.GONE);
				break;
			case FAIL:
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.VISIBLE);
				break;
			case INPROGRESS:
				holder.pb.setVisibility(View.VISIBLE);
				holder.staus_iv.setVisibility(View.GONE);
				break;
			default:
				sendVoiceMsgInBackground(message, holder);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendVoiceMsgInBackground(final ChatMessage chatMessage, final ViewHolder holder) {
		chatMessage.setStatus(ChatMessage.Status.INPROGRESS);
		try {
		
		String[] arr = chatMessage.getContent().split(":");
		final String localPath = arr[1];
		final String fileName = arr[3];
		final String fileSize = arr[4];
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						Gson gson = new Gson();
						HashMap<String, Object> map = gson.fromJson(
								(String) content,
								new TypeToken<Map<String, Object>>() {
								}.getType());
						if (map.get("returnCode").equals("Success")) {
							String url = (String) map.get("content");
							chatMessage.setContent(Type.VOICE.toString()+":" + localPath
									+ ":" + url + ":" + fileName + ":" + fileSize);
							MessageSender.getInstance().sendMessageEvent(
									chatMessage, toChatUser);
							chatMessage.setStatus(ChatMessage.Status.SUCCESS);
							updateSendedView(chatMessage, holder);
						} else {
									chatMessage.setStatus(Status.FAIL);
									updateSendedView(chatMessage, holder);

						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
								chatMessage.setStatus(Status.FAIL);
								updateSendedView(chatMessage, holder);
					}
				};
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("room", chatMessage.getRoomName());
				map.put("mpf", new File(localPath));
				new Data_loader().getApiResult(handler, "/mobile/upload/voiceUpload", map, "post");
			}
		}).start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	
	}

	private void handleImageMessage(final ChatMessage message,
			final ViewHolder holder, final int position, View convertView) {
		try {
			String content = message.getContent();
			final String[] arr = content.split(":");

			final String localFilePath = arr[1];
				
			// holder.pb.setVisibility(View.VISIBLE);
			holder.iv.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					activity.startActivityForResult((new Intent(activity,
							ContextMenu.class)).putExtra("position", position)
							.putExtra("type", ChatMessage.Type.IMAGE.ordinal()),
							ChatActivity.REQUEST_CODE_CONTEXT_MENU);
					return true;
				}
			});

			if (message.status == ChatMessage.Status.SENDING) {
					sendPictureMessage(message, holder);
					message.setStatus(Status.INPROGRESS);
					updateSendedView(message, holder);
			}else if(message.status == ChatMessage.Status.INPROGRESS){
				File file = new File(localFilePath);
				Uri uri = Uri.fromFile(file);
				// 处理图片分辨率太大imageview不能显视出来
				options.uri = uri;
				options.maxWidth = activity.getWindowManager()
						.getDefaultDisplay().getWidth()/4;
				options.maxHeight = activity.getWindowManager()
						.getDefaultDisplay().getHeight()/4;
				Bitmap bitmap = compress.compressFromUri(activity, options);
				holder.iv.setImageBitmap(bitmap);
				showImageView(holder.iv, null, localFilePath);// 本地图片,图片点击进入大图
			} else if (message.status == ChatMessage.Status.SUCCESS) {
				String remoteFilePath = arr[2];
				if(new File(localFilePath).exists()){
					File file = new File(localFilePath);
					Uri uri = Uri.fromFile(file);
					// 处理图片分辨率太大imageview不能显视出来
					options.uri = uri;
					options.maxWidth = activity.getWindowManager()
							.getDefaultDisplay().getWidth()/4;
					options.maxHeight = activity.getWindowManager()
							.getDefaultDisplay().getHeight()/4;
					Bitmap bitmap = compress.compressFromUri(activity, options);
					holder.iv.setImageBitmap(bitmap);
				}else{
					//下载图片
					mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + remoteFilePath,
							holder.iv, MyshipAdapter.displayImageOptions);
				}	
				showImageView(holder.iv, remoteFilePath, localFilePath);// 远程图片,图片点击进入大图
				updateSendedView(message, holder);
			} else if (message.status == ChatMessage.Status.FAIL) {
				updateSendedView(message, holder);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleTextMessage(ChatMessage message, ViewHolder holder,
			final int position) {
		try {
			String txtBody = message.getContent();
			if(txtBody.contains("<br/>")||txtBody.contains("<br />")){
				txtBody = txtBody.replaceAll("<br/>", "\n");
				txtBody = txtBody.replaceAll("<br />", "\n");
			}
			Spannable span = SmileUtils.getSmiledText(context, txtBody);
			// 设置内容
			holder.tv.setText(span, BufferType.SPANNABLE);
			// 设置长按事件监听
			holder.tv.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					activity.startActivityForResult((new Intent(activity,
							ContextMenu.class)).putExtra("position", position)
							.putExtra("type", ChatMessage.Type.TXT.ordinal()),
							ChatActivity.REQUEST_CODE_CONTEXT_MENU);
					return true;
				}
			});
			updateSendedView(message, holder);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void handleFileMessage(final ChatMessage message,
			final ViewHolder holder, int position, View convertView) {
		try {
			String content = message.getContent();
			final String[] arr = content.split(":");

			final String localFilePath = arr[1];

			holder.tv_file_name.setText(arr[3]);
			holder.tv_file_size.setText(getDataSize(Long.valueOf(arr[4])));
			holder.ll_container.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					String localRFilePath = Environment
							.getExternalStorageDirectory()
							+ "/eyunda/download/file/" + arr[3];
					final String remoteFilePath = arr[2];
					// 如何是收到的消息，根据文件名判断本地文件是否存在
					if (message.direct == ChatMessage.Direct.RECEIVE) {
						File file  = new File(localRFilePath);
						if (file != null && file.exists()) {
							// 文件存在，直接打开
							openFile(file, (Activity) context);
						} else {
							// 下载
							context.startActivity(new Intent(context,
									ShowNormalFileActivity.class).putExtra("filePath",
											remoteFilePath).putExtra("fileName", arr[3]));
						}
					} else {
						File file  = new File(localFilePath);
						File localRFile  = new File(localRFilePath);
						if (file != null && file.exists()) {
							// 文件存在，直接打开
							openFile(file, (Activity) context);
						}else if(localRFile!=null&&localRFile.exists()){
							//是否已经下载过
							openFile(localRFile, (Activity) context);
						}else{
							//文件不存在了,去下载
							context.startActivity(new Intent(context,
									ShowNormalFileActivity.class).putExtra("filePath",
											remoteFilePath).putExtra("fileName", arr[3]));
						}
					}
				}
			});
			if (message.status == ChatMessage.Status.SENDING) {
				sendFileMessage(message, holder);
				message.setStatus(Status.INPROGRESS);
			}

			if (message.direct == ChatMessage.Direct.RECEIVE) { // 接收的消息
				String localRFilePath = Environment
						.getExternalStorageDirectory()
						+ "/eyunda/download/file/" + arr[3];
				File file = new File(localRFilePath);
				if (file != null && file.exists()) {
					holder.tv_file_download_state.setText("已下载");
				} else {
					holder.tv_file_download_state.setText("未下载");
				}
				return;
			}

			// until here, deal with send voice msg
			switch (message.status) {
			case SUCCESS:
				holder.pb.setVisibility(View.INVISIBLE);
				holder.tv.setVisibility(View.INVISIBLE);
				holder.staus_iv.setVisibility(View.INVISIBLE);
				break;
			case FAIL:
				holder.pb.setVisibility(View.INVISIBLE);
				holder.tv.setVisibility(View.INVISIBLE);
				holder.staus_iv.setVisibility(View.VISIBLE);
				break;
			case INPROGRESS:
				if (timers.containsKey(message.getId()))
					return;
				// set a timer
				final Timer timer = new Timer();
				timers.put(message.getId(), timer);
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								holder.pb.setVisibility(View.VISIBLE);
								holder.tv.setVisibility(View.VISIBLE);
								//holder.tv.setText(message.progress + "%");
								holder.tv.setText("");
								if (message.status == ChatMessage.Status.SUCCESS) {
									holder.pb.setVisibility(View.INVISIBLE);
									holder.tv.setVisibility(View.INVISIBLE);
									timer.cancel();
								} else if (message.status == ChatMessage.Status.FAIL) {
									holder.pb.setVisibility(View.INVISIBLE);
									holder.tv.setVisibility(View.INVISIBLE);
									holder.staus_iv.setVisibility(View.VISIBLE);
									Toast.makeText(activity, "文件上传失败", 0).show();
									timer.cancel();
								}
							}
						});

					}
				}, 0, 500);
				break;
			default:
				// 发送消息
				// sendMsgInBackground(message, holder);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendFileMessage(final ChatMessage chatMessage,
			final ViewHolder holder) {
		try {
		String[] arr = chatMessage.getContent().split(":");
		final String localPath = arr[1];
		final String fileName = arr[3];
		final String fileSize = arr[4];
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
					@Override
					public void onProgress(long totalSize, long currentSize,
							long speed) {
						super.onProgress(totalSize, currentSize, speed);
						long downloadPercent = currentSize * 100 / totalSize;
						// textView.setText(downloadPercent + "--------" + speed +
						// "kbps");
						chatMessage.progress = ((int) (downloadPercent));
						System.err.println(downloadPercent + "~~~~~~~~");
					}

					@Override
					public void onSuccess(String content) {
						Gson gson = new Gson();
						HashMap<String, Object> map = gson.fromJson(
								(String) content,
								new TypeToken<Map<String, Object>>() {
								}.getType());
						if (map.get("returnCode").equals("Success")) {
							String url = (String) map.get("content");
							chatMessage.setContent(Type.FILE.toString() +":"+ localPath
									+ ":" + url + ":" + fileName + ":" + fileSize);
							MessageSender.getInstance().sendMessageEvent(
									chatMessage, toChatUser);
							chatMessage.setStatus(ChatMessage.Status.SUCCESS);
							updateSendedView(chatMessage, holder);
						} else {
									chatMessage.setStatus(Status.FAIL);
									updateSendedView(chatMessage, holder);

						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
								chatMessage.setStatus(Status.FAIL);
								updateSendedView(chatMessage, holder);
					}
				};
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("room", chatMessage.getRoomName());
				map.put("mpf", new File(localPath));
				new Data_loader().getApiResult(handler, "/mobile/upload/fileUpload", map, "post");
			}
		}).start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 先保存图片到服务端，再调mina发送一条带图片标记的信息过去，对方收到后从服务器端下载图片显示 */
	private void sendPictureMessage(final ChatMessage message,
			final ViewHolder holder) {
		try {
			String[] arr = message.getContent().split(":");
			final String localPath = arr[1];
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String content) {
							Gson gson = new Gson();
							HashMap<String, Object> map = gson.fromJson(
									(String) content,
									new TypeToken<Map<String, Object>>() {
									}.getType());
							if (map.get("returnCode").equals("Success")) {
								String url = (String) map.get("content");
								message.setContent(Type.IMAGE.toString()+":"+localPath
										+ ":" + url + ":" + ":" );
								MessageSender.getInstance().sendMessageEvent(message,
										toChatUser);
								message.setStatus(ChatMessage.Status.SUCCESS);
								updateSendedView(message, holder);
							} else {
										message.setStatus(Status.FAIL);
										updateSendedView(message, holder);

							}
						}

						@Override
						public void onFailure(Throwable error, String content) {
							super.onFailure(error, content);
									message.setStatus(Status.FAIL);
									updateSendedView(message, holder);
						}
					};
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("room", message.getRoomName());
					map.put("mpf", new File(localPath));
					new Data_loader().getApiResult(handler, "/mobile/upload/imageUpload", map,
							"post");
				}
			}).start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新ui上消息发送状态
	 * 
	 * @param message
	 * @param holder
	 */
	private void updateSendedView(final ChatMessage message,
			final ViewHolder holder) {
		if(activity!=null)
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// send success
					// if (message.getType() == EMMessage.Type.VIDEO) {
					// holder.tv.setVisibility(View.GONE);
					// }
					if (message.status == Status.SENDING) {
						holder.pb.setVisibility(View.VISIBLE);
					}
					if (message.status == Status.INPROGRESS) {
						holder.pb.setVisibility(View.VISIBLE);
					}
					if (message.status == ChatMessage.Status.SUCCESS) {
						 if (message.getType() == ChatMessage.Type.IMAGE) {
							 holder.tv.setVisibility(View.INVISIBLE);
						} 
						holder.pb.setVisibility(View.GONE);
						
					}
					if (message.status == ChatMessage.Status.FAIL) {
						holder.pb.setVisibility(View.GONE);
						holder.staus_iv.setVisibility(View.VISIBLE);
					}
					// else if (message.status == ChatMessage.Status.FAIL) {
					// // if (message.getType() == EMMessage.Type.FILE) {
					// // holder.pb.setVisibility(View.INVISIBLE);
					// // } else {
					// holder.pb.setVisibility(View.GONE);
					// // }
					// holder.staus_iv.setVisibility(View.VISIBLE);
					// Toast.makeText(
					// activity,
					// "发送失败",
					// 0).show();
					// }
	
//					notifyDataSetChanged();
				}
			});
	}

	private void showImageView(final ImageView iv, final String remoteDir,
			final String localDir) {

		iv.setClickable(true);
		iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, ShowBigImage.class);

				intent.putExtra("remotepath", remoteDir);
				intent.putExtra("localpath", localDir);

				activity.startActivity(intent);
			}
		});
//		if (localDir == null && remoteDir != null) {
//			iv.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Intent intent = new Intent(activity, ShowBigImage.class);
//
//					intent.putExtra("remotepath", remoteDir);
//
//					activity.startActivity(intent);
//				}
//			});
//		} else if (localDir != null && remoteDir == null) {
//			iv.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Intent intent = new Intent(activity, ShowBigImage.class);
//
//					intent.putExtra("localpath", localDir);
//
//					activity.startActivity(intent);
//				}
//			});
//		}
	}

	private String getDataSize(long l) {
		DecimalFormat decimalformat = new DecimalFormat("###.00");
		if (l < 1024L)
			return (new StringBuilder()).append(l).append("bytes").toString();
		if (l < 1048576L)
			return (new StringBuilder())
					.append(decimalformat.format((float) l / 1024F))
					.append("KB").toString();
		if (l < 1073741824L)
			return (new StringBuilder())
					.append(decimalformat.format((float) l / 1024F / 1024F))
					.append("MB").toString();
		if (l < 0L)
			return (new StringBuilder())
					.append(decimalformat
							.format((float) l / 1024F / 1024F / 1024F))
					.append("GB").toString();
		else
			return "error";
	}

	private void openFile(File file, Activity activity) {
		Intent intent = new Intent();
		intent.addFlags(268435456);
		intent.setAction("android.intent.action.VIEW");
		String s = getMIMEType(file);
		intent.setDataAndType(Uri.fromFile(file), s);
		try {
			activity.startActivity(intent);
		} catch (Exception exception) {
			exception.printStackTrace();
			Toast.makeText(activity, "找不到文件打开方式", 1).show();
		}
	}

	private String getMIMEType(File file) {
		String s = "";
		String s1 = file.getName();
		String s2 = s1.substring(s1.lastIndexOf(".") + 1, s1.length())
				.toLowerCase();
		s = MimeTypeMap.getSingleton().getMimeTypeFromExtension(s2);
		return s;
	}

	public static class ViewHolder {
		ImageView iv;
		TextView tv;
		ProgressBar pb;
		ImageView staus_iv;
		ImageView head_iv;
		TextView tv_userId;
		ImageView playBtn;
		TextView timeLength;
		TextView size;
		LinearLayout container_status_btn;
		LinearLayout ll_container;
		ImageView iv_read_status;
		// 显示已读回执状态
		TextView tv_ack;
		// 显示送达回执状态
		TextView tv_delivered;

		TextView tv_file_name;
		TextView tv_file_size;
		TextView tv_file_download_state;
	}

}