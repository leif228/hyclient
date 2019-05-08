package com.eyunda.third.adapters.chat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.chat.ChatActivity;
import com.eyunda.third.adapters.chat.domain.ChatMessage;
import com.hy.client.R;

public class VoicePlayClickListener implements View.OnClickListener {

	ChatMessage message;
	ImageView voiceIconView;

	private AnimationDrawable voiceAnimation = null;
	MediaPlayer mediaPlayer = null;
	ImageView iv_read_status;
	Activity activity;
	private BaseAdapter adapter;
	String localPath;
	String remotePath;
	String voiceName;

	public static boolean isPlaying = false;
	public static VoicePlayClickListener currentPlayListener = null;

	public VoicePlayClickListener(ChatMessage message, ImageView v,
			ImageView iv_read_status, BaseAdapter adapter, Activity activity,
			String username) {
		this.message = message;
		this.iv_read_status = iv_read_status;
		this.adapter = adapter;
		voiceIconView = v;
		this.activity = activity;

		String voice = message.getContent();
		String[] arr = voice.split(":");
		localPath = arr[1];
		remotePath = arr[2];
		voiceName = arr[3];
	}

	public void stopPlayVoice() {
		voiceAnimation.stop();
		if (message.direct == ChatMessage.Direct.RECEIVE) {
			voiceIconView.setImageResource(R.drawable.chatfrom_voice_playing);
		} else {
			voiceIconView.setImageResource(R.drawable.chatto_voice_playing);
		}
		// stop play voice
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
		isPlaying = false;
		((ChatActivity) activity).playMsgId = null;
		adapter.notifyDataSetChanged();
	}

	public void playVoice(String filePath) {
		if (!(new File(filePath).exists())) {
			return;
		}
		((ChatActivity) activity).playMsgId = message.getId();
		AudioManager audioManager = (AudioManager) activity
				.getSystemService(Context.AUDIO_SERVICE);

		mediaPlayer = new MediaPlayer();
		// 根据手机感应来判断是否是免提播放（，）
		// if (EMChatManager.getInstance().getChatOptions().getUseSpeaker()){
		audioManager.setMode(AudioManager.MODE_NORMAL);
		audioManager.setSpeakerphoneOn(true);// 免提
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
		// }
		// else{
		// audioManager.setSpeakerphoneOn(false);//关闭扬声器
		// //把声音设定成Earpiece（听筒）出来，设定为正在通话中
		// audioManager.setMode(AudioManager.MODE_IN_CALL);
		// mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
		// }
		try {
			mediaPlayer.setDataSource(filePath);
			mediaPlayer.prepare();
			mediaPlayer
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							// TODO Auto-generated method stub
							mediaPlayer.release();
							mediaPlayer = null;
							stopPlayVoice(); // stop animation
						}

					});
			isPlaying = true;
			currentPlayListener = this;
			mediaPlayer.start();
			showAnimation();
			try {
				// 如果是接收的消息
				if (message.direct == ChatMessage.Direct.RECEIVE) {
					message.isAcked = true;
					if (iv_read_status != null
							&& iv_read_status.getVisibility() == View.VISIBLE) {
						// 隐藏自己未播放这条语音消息的标志
						iv_read_status.setVisibility(View.INVISIBLE);
						// EMChatDB.getInstance().updateMessageAck(message.getId(),
						// true);
					}
					// 告知对方已读这条消息
					// if(chatType != ChatType.GroupChat)
					// EMChatManager.getInstance().ackMessageRead(message.getFrom(),
					// message.getMsgId());
				}
			} catch (Exception e) {
				// message.isAcked = false;
			}
		} catch (Exception e) {
		}
	}

	private void showAnimation() {
		if (message.direct == ChatMessage.Direct.RECEIVE) {
			voiceIconView.setImageResource(R.anim.voice_from_icon);
		} else {
			voiceIconView.setImageResource(R.anim.voice_to_icon);
		}
		voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
		voiceAnimation.start();
	}

	public void download(final String remotePath, final String voiceName) {
		new Thread(new Runnable() {
			HttpURLConnection conn;
			InputStream is;
			FileOutputStream fos;

			@Override
			public void run() {
				File file = null;
				try {
					String sessionId = "";
					if (GlobalApplication.getInstance().getUserData() != null) {
						sessionId = GlobalApplication.getInstance()
								.getUserData().getSessionId();
					}
					if ("".equals(sessionId)) {
						activity.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(activity, "sessionId为空", 0)
										.show();
								return;
							}
						});
					}

					URL url = new URL(ApplicationConstants.VOICE_URL
							+ remotePath + "&sessionId=" + sessionId);

					conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					int length = Integer.valueOf(conn
							.getHeaderField("Content_Length"));
					is = conn.getInputStream();
					
					File f = new File(Environment.getExternalStorageDirectory()
							+ "/eyunda/download/voice/");
					if (!f.exists()) {
						f.mkdirs();
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
					} while (true);// 点击取消就停止下载.
					message.status = ChatMessage.Status.SUCCESS;

					fos.close();// 释放资源
					is.close();
					conn.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
					activity.runOnUiThread(new Runnable() {
						public void run() {
							try {
								File f = new File(Environment
										.getExternalStorageDirectory()
										+ "/eyunda/download/voice/" + voiceName);
								if (f != null && f.exists())
									f.delete();
								message.status = ChatMessage.Status.FAIL;
								Toast.makeText(activity, "下载语音失败: ", 0).show();
								fos.close();
								is.close();
								conn.disconnect();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		if (isPlaying) {
			if (((ChatActivity) activity).playMsgId != null
					&& ((ChatActivity) activity).playMsgId.equals(message
							.getId())) {
				currentPlayListener.stopPlayVoice();
				return;
			}
			currentPlayListener.stopPlayVoice();
		}

		if (message.direct == ChatMessage.Direct.SEND) {
			File localFile = new File(localPath);
			if (localFile != null && localFile.exists()) {
				playVoice(localPath);
			} else {
				File localRFile = new File(Environment.getExternalStorageDirectory()
						+ "/eyunda/download/voice/" + voiceName);
				if (localRFile.exists() && localRFile.isFile())
					playVoice(localRFile.getAbsolutePath());
				else {
					// 文件不存在，去下载
					Toast.makeText(activity, "正在下载语音，稍后点击", Toast.LENGTH_SHORT)
					.show();
					download(remotePath, voiceName);
				}
			}
		} else {
			if (message.status == ChatMessage.Status.SUCCESS) {

				File file = new File(Environment.getExternalStorageDirectory()
						+ "/eyunda/download/voice/" + voiceName);
				if (file.exists() && file.isFile())
					playVoice(file.getAbsolutePath());
				else {
					// 文件不存在，去下载
					Toast.makeText(activity, "正在下载语音，稍后点击", Toast.LENGTH_SHORT)
					.show();
					download(remotePath, voiceName);
				}

			} else if (message.status == ChatMessage.Status.INPROGRESS) {
				Toast.makeText(activity, "正在下载语音，稍后点击", Toast.LENGTH_SHORT)
						.show();
			} else if (message.status == ChatMessage.Status.FAIL) {
				Toast.makeText(activity, "正在下载语音，稍后点击", Toast.LENGTH_SHORT)
						.show();
				// 去服务端下载
				download(remotePath, voiceName);
			}
		}
	}
}
