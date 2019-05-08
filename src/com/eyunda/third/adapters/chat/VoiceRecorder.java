package com.eyunda.third.adapters.chat;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.AudioSource;
import android.media.MediaRecorder.OutputFormat;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.Time;

public class VoiceRecorder {

	MediaRecorder recorder;
	static final String PREFIX = "voice";
	static final String EXTENSION = ".amr";
	private boolean isRecording;
	private long startTime;
	private String voiceFilePath;
	private String voiceFileName;
	private File file;
	private Handler handler;

	public VoiceRecorder(Handler handler1) {
		isRecording = false;
		voiceFilePath = null;
		voiceFileName = null;
		handler = handler1;
	}

	public String startRecording(String s, String s1, Context context) {
		file = null;
		try {
			recorder = new MediaRecorder();
			recorder.setAudioSource(AudioSource.MIC);
			recorder.setOutputFormat(OutputFormat.AMR_NB);
			recorder.setAudioEncoder(AudioEncoder.AMR_NB);
			recorder.setAudioChannels(1);// 1为单声道2为立体声
			recorder.setAudioSamplingRate(8000);// 每秒采样率
			recorder.setAudioEncodingBitRate(64);
			voiceFileName = getVoiceFileName(s1);
			voiceFilePath = getVoiceFilePath();
			file = new File(voiceFilePath);
			System.err.println("file.getAbsolutePath():"+file.getAbsolutePath());
			recorder.setOutputFile(file.getAbsolutePath());
			recorder.prepare();
			isRecording = true;
			recorder.start();
		} catch (IOException ioexception) {
		}
		(new Thread(new Runnable() {
			
			public void run() {
				try {
					while (isRecording) {
						Message message = new Message();
						message.what = (recorder.getMaxAmplitude() * 13) / 32767;
						handler.sendMessage(message);
						SystemClock.sleep(100L);
					}
				} catch (Exception exception) {
				}
			}

//			final VoiceRecorder this$0;
//			{
//				this$0 = VoiceRecorder.this;
//				(this$0).super();
//				
//			}

		})).start();
		startTime = (new Date()).getTime();
		return file != null ? file.getAbsolutePath() : null;
	}

	public void discardRecording() {
		if (recorder != null) {
			try {
				recorder.stop();
				recorder.release();
				recorder = null;
				if (file != null && file.exists() && !file.isDirectory())
					file.delete();
			} catch (IllegalStateException illegalstateexception) {
			}
			isRecording = false;
		}
	}

	public int stopRecoding() {
		if (recorder != null) {
			isRecording = false;
			recorder.stop();
			recorder.release();
			recorder = null;
			int i = (int) ((new Date()).getTime() - startTime) / 1000;
			return i;
		} else {
			return 0;
		}
	}

	protected void finalize() throws Throwable {
		super.finalize();
		if (recorder != null)
			recorder.release();
	}

	public String getVoiceFileName(String s) {
		Time time = new Time();
		time.setToNow();
		System.err.println("time.toString().substring(0, 15):"
				+ time.toString().substring(0, 15));
		System.err.println("getVoiceFileName:"
				+ (new StringBuilder()).append(s)
						.append(time.toString().substring(0, 15))
						.append(".amr").toString());
		return (new StringBuilder()).append(s)
				.append(time.toString().substring(0, 15)).append(".amr")
				.toString();
	}

	public boolean isRecording() {
		return isRecording;
	}

	public String getVoiceFilePath() {
		String file = Environment.getExternalStorageDirectory()+ "/eyunda/local/voice";
		File f = new File(file);
        if(!f.exists())
            f.mkdirs();
		
		return (new StringBuilder())
				.append(file).append("/")
				.append(voiceFileName).toString();
	}

}
