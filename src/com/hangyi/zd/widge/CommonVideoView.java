package com.hangyi.zd.widge;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hangyi.tools.DateTimePickDialogUtil;
import com.hangyi.zd.activity.ShipDynamicFragment;
import com.hangyi.zd.play.CacheSeekBarTimer;
import com.hangyi.zd.play.LoadedList;
import com.hangyi.zd.play.PlayListenerTimer;
import com.hy.client.R;

public class CommonVideoView extends FrameLayout implements
		View.OnClickListener, SeekBar.OnSeekBarChangeListener,
		DateTimePickDialogUtil.OnDateSetListener {

	public final static int UPDATE_PLAY_SEEKBAR = 2;
	public final static int UPDATE_CACHE_SEEKBAR = 3;
	public final static int noPlayedPlay = 4;
	public final static int playedPlay = 5;
	public final static int cache = 6;
	public final static int pauseState = 14;
	public final static int playState = 15;
	public final static int cacheState = 16;

	private Context context;
	private LinearLayout videoPauseBtn;
	private LinearLayout screenSwitchBtn;
	private LinearLayout touchStatusView;
	private LinearLayout videoControllerLayout;
	private LinearLayout videoControllerLayout1,videoControllerLayout3,videoControllerLayout2;
	private ImageView screen_status_img;
	private ImageView touchStatusImg;
	private ImageView videoPauseImg;
	private TextView touchStatusTime;
	private TextView videoCurTimeText;
	private TextView videoTotalTimeText;
	private SeekBar videoSeekBar;
	private ProgressBar progressBar;

	public volatile static boolean startedPlay = false;
	public volatile static boolean isPlaying = false;
	public volatile static boolean handerPause = false;
	public volatile static boolean startedPlayNotifyed = false;
	public static final int PauseAfterPlay = -1;
	private Handler seekBarHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// case UPDATE_PLAY_SEEKBAR:
			// if (isPlaying) {
			// videoSeekBar.setProgress(LoadedList.getInstance().getCurrPlayPosition());
			// }
			// break;
			case UPDATE_CACHE_SEEKBAR:
				videoSeekBar.setSecondaryProgress(LoadedList.getInstance()
						.loadedSize());
				if (isPlaying) {
					if(videoSeekBar.getProgress() != LoadedList.needTotalSize){
						videoSeekBar.setProgress(ShipDynamicFragment.currPlayPosition);
					}else{
						ShipDynamicFragment.currPlayPosition=0;
						ShipDynamicFragment.flagFirst=true;
						ShipDynamicFragment.flag = 0;
						onCompletionState();
						isPlaying=false;
					}
				}
				break;
//			case noPlayedPlay:
//				if (!startedPlayNotifyed) {
//					startedPlayNotifyed = true;
//					onPlay();
//				}
//				break;
//			case playedPlay:
//				if (!isPlaying) {
//					if(!handerPause){
//						isPlaying = true;
//						onPlay();
//					}
//				}
//				break;
//			case cache:
//				if (isPlaying) {
//					isPlaying = false;
//					onCache();
//				}
//				break;
			case cacheState:
				onCacheState();
				break;
			case pauseState:
				onPauseState();
				break;
			case playState:
				onPlayState();
				break;
			}
		}
	};

	private LinearLayout startTimell;
	private LinearLayout endTimell;
	private TextView startTime;
	private TextView endTime;
	private TextView fast;
	private TextView mod;
	private TextView slo;

	private int startEndTimeFlag = 1;
	private Handler playHandler;
	private CommonVideoChangLintener commonVideoChangLintener;
	public interface CommonVideoChangLintener{
		void endTimeChang(Calendar startCalendar,Calendar endCalendar);
		void fastChang();
		void modChang();
		void sloChang();
		void playBigImg();
		void playSmaImg();
		void disPlayPauseImg();
		void noDisPlayPauseImg();
	}
	public void setCommonVideoChangLintener(CommonVideoChangLintener commonVideoChangLintener){
		this.commonVideoChangLintener = commonVideoChangLintener;
	}

	public CommonVideoView(Context context) {
		this(context, null);
	}

	public CommonVideoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CommonVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}

	public void init(Calendar startc, Calendar endc) {
		startTime.setText(initStartDateTime(startc));
		endTime.setText(initStartDateTime(endc));

		onCacheState();
	}

	private void onCacheState() {
		progressBar.setVisibility(View.VISIBLE);
		videoPauseImg.setVisibility(View.GONE);
//		videoSeekBar.setEnabled(false);
		videoSeekBar.setEnabled(true);
	}

	private void onPauseState() {
		commonVideoChangLintener.noDisPlayPauseImg();
		videoControllerLayout3.setVisibility(View.VISIBLE);
		videoControllerLayout1.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		videoPauseImg.setVisibility(View.VISIBLE);
		videoSeekBar.setEnabled(true);
	}

	private void onPlayState() {
		commonVideoChangLintener.disPlayPauseImg();
		videoControllerLayout3.setVisibility(View.GONE);
		videoControllerLayout1.setVisibility(View.GONE);
		progressBar.setVisibility(View.GONE);
		videoPauseImg.setVisibility(View.VISIBLE);
		videoSeekBar.setEnabled(true);
	}

	private void onCompletionState() {
		commonVideoChangLintener.noDisPlayPauseImg();
		videoControllerLayout3.setVisibility(View.VISIBLE);
		videoControllerLayout1.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		videoPauseImg.setVisibility(View.VISIBLE);
		videoSeekBar.setProgress(0);
		videoSeekBar.setEnabled(true);
	}
	
	public void playPause() {
		commonVideoChangLintener.noDisPlayPauseImg();
		videoControllerLayout3.setVisibility(View.VISIBLE);
		videoControllerLayout1.setVisibility(View.VISIBLE);
		isPlaying = false;
		handerPause = true;
	}

	private void setFullScreen() {
		touchStatusImg.setImageResource(R.drawable.iconfont_exit);
		this.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		// videoView.requestLayout();
	}

	private void setNormalScreen() {
		touchStatusImg.setImageResource(R.drawable.iconfont_enter_32);
		this.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 400));
		// videoView.requestLayout();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		initView();
	}
	public void setVideoTotalTimeText(String time){
		videoTotalTimeText.setText(time);
	}

	private void initView() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.zd_ship_play_fragment_play, null);
		startTimell = (LinearLayout) view.findViewById(R.id.startTimell);
		endTimell = (LinearLayout) view.findViewById(R.id.endTimell);
		videoControllerLayout1 = (LinearLayout) view.findViewById(R.id.videoControllerLayout1);
		videoControllerLayout3 = (LinearLayout) view.findViewById(R.id.videoControllerLayout3);
		startTime = (TextView) view.findViewById(R.id.startTime);
		endTime = (TextView) view.findViewById(R.id.endTime);
		videoSeekBar = (SeekBar) view.findViewById(R.id.videoSeekBar);
		videoTotalTimeText = (TextView) view.findViewById(R.id.videoTotalTime);
		videoPauseBtn = (LinearLayout) view.findViewById(R.id.videoPauseBtn);
		videoPauseImg = (ImageView) view.findViewById(R.id.videoPauseImg);
		screen_status_img = (ImageView) view.findViewById(R.id.screen_status_img);
		fast = (TextView) view.findViewById(R.id.fast);
		mod = (TextView) view.findViewById(R.id.mod);
		slo = (TextView) view.findViewById(R.id.slo);
		screenSwitchBtn = (LinearLayout) view
				.findViewById(R.id.screen_status_btn);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

		startTime.setText(initStartDateTime(null));
		endTime.setText(initEndDateTime());

		startTimell.setOnClickListener(this);
		endTimell.setOnClickListener(this);
		videoSeekBar.setOnSeekBarChangeListener(this);
		videoPauseImg.setOnClickListener(this);
		fast.setOnClickListener(this);
		mod.setOnClickListener(this);
		slo.setOnClickListener(this);
		screenSwitchBtn.setOnClickListener(this);
		addView(view);
	}

	// @Override
	// public void onPrepared(MediaPlayer mp) {
	// duration = videoView.getDuration();
	// int[] time = getMinuteAndSecond(duration);
	// videoTotalTimeText.setText(String.format("%02d:%02d", time[0], time[1]));
	// formatTotalTime = String.format("%02d:%02d", time[0], time[1]);
	// videoSeekBar.setMax(duration);
	// progressBar.setVisibility(View.GONE);
	//
	// mp.start();
	// videoPauseBtn.setEnabled(true);
	// videoSeekBar.setEnabled(true);
	// videoPauseImg.setImageResource(R.drawable.icon_video_pause);
	// timer.schedule(timerTask, 0, 1000);
	// }

	public void reLoadView() {
		CommonVideoView.startedPlay=false;
		CommonVideoView.isPlaying=false;
		CommonVideoView.handerPause=false;
		CommonVideoView.startedPlayNotifyed=false;
		onCacheState();
		videoSeekBar.setSecondaryProgress(0);
		videoSeekBar.setProgress(0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.videoPauseImg:
//			if (isPlaying) {
//				videoPauseImg.setImageResource(R.drawable.icon_video_play);
//				videoPauseImg.setVisibility(View.VISIBLE);
//				isPlaying = false;
//				handerPause = true;
//			} else {
//				videoPauseImg.setVisibility(View.VISIBLE);
//				videoPauseImg.setImageResource(R.drawable.icon_video_pause);
			onPlayState();
				startedPlay = true;
				isPlaying = true;
				handerPause = false;
				
//			}
			break;
		case R.id.screen_status_btn:
//			int i = getResources().getConfiguration().orientation;
//			if (i == Configuration.ORIENTATION_PORTRAIT) {
//				((Activity) context)
//						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//			} else if (i == Configuration.ORIENTATION_LANDSCAPE) {
//				((Activity) context)
//						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//			}
			
			String tag = (String) screen_status_img.getTag(); 
			if("iconfont_enter_32".equals(tag)){
				screen_status_img.setTag("iconfont_exit");
				screen_status_img.setImageResource(R.drawable.iconfont_exit);
				if(commonVideoChangLintener!=null)
					commonVideoChangLintener.playBigImg();
			}else{
				screen_status_img.setTag("iconfont_enter_32");
				screen_status_img.setImageResource(R.drawable.iconfont_enter_32);
				if(commonVideoChangLintener!=null)
					commonVideoChangLintener.playSmaImg();
			}
			break;
		case R.id.startTimell:
			startEndTimeFlag = 1;
			DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
					(Activity) context, initStartDateTime(null));
			dateTimePicKDialog.setOnDateSetListener(this);
			dateTimePicKDialog.dateTimePicKDialog(startTime);
			break;
		case R.id.endTimell:
			startEndTimeFlag = 2;
			DateTimePickDialogUtil dateTimePicKDialog2 = new DateTimePickDialogUtil(
					(Activity) context, initEndDateTime());
			dateTimePicKDialog2.setOnDateSetListener(this);
			dateTimePicKDialog2.dateTimePicKDialog(endTime);
			break;
		case R.id.fast:
			mod.setBackground(null);
			slo.setBackground(null);
			fast.setBackgroundResource(R.drawable.navlayout2);
			if(commonVideoChangLintener!=null)
				commonVideoChangLintener.fastChang();
			break;
		case R.id.mod:
			mod.setBackgroundResource(R.drawable.navlayout2);
			slo.setBackground(null);
			fast.setBackground(null);
			if(commonVideoChangLintener!=null)
				commonVideoChangLintener.modChang();
			break;
		case R.id.slo:
			mod.setBackground(null);
			slo.setBackgroundResource(R.drawable.navlayout2);
			fast.setBackground(null);
			if(commonVideoChangLintener!=null)
				commonVideoChangLintener.sloChang();
			break;

		}
	}

	// private void threadStartPlay(){
	// PlaySeekBarTimer.getInstance().setHandler(seekBarHandler);
	// PlaySeekBarTimer.getInstance().startPlaySeekBarTimer();
	// PlayGpsTimer.getInstance().setHandler(playHandler);
	// PlayGpsTimer.getInstance().startPlayGpsTimer();
	// PlayImgTimer.getInstance().setHandler(playHandler);
	// PlayImgTimer.getInstance().startPlayImgTimer();
	// }
	// private void threadPausePlay(){
	// PlaySeekBarTimer.getInstance().pausePlaySeekBarTimer();
	// PlayGpsTimer.getInstance().pausePlayGpsTimer();
	// PlayImgTimer.getInstance().pausePlayImgTimer();
	// }

	@Override
	public void getSetDate(Calendar calendar) {
		if (startEndTimeFlag == 2) {
			if (calendar.getTimeInMillis()
					- DateTimePickDialogUtil.getCalendarByInintData(
							startTime.getText().toString()).getTimeInMillis() >= 0) {
				// TODO 监听开始加载GPS数据
				if(commonVideoChangLintener!=null)
					commonVideoChangLintener.endTimeChang(DateTimePickDialogUtil.getCalendarByInintData(
						startTime.getText().toString()) ,calendar);
			} else
				Toast.makeText(context, "请设置结束时间大于开始时间！", Toast.LENGTH_LONG)
						.show();
		}
	}

	// 初始化开始时间
	private String initStartDateTime(Calendar c) {
		Calendar calendar = Calendar.getInstance();
		if (c != null)
			calendar = c;
		String initDateTime = calendar.get(Calendar.YEAR) + "年"
				+ (calendar.get(Calendar.MONTH) + 1) + "月"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "日 "
				+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE);
		return initDateTime;
	}

	// 初始化结束时间
	private String initEndDateTime() {
		Calendar calendar = Calendar.getInstance();
		String endDateTime = calendar.get(Calendar.YEAR) + "年"
				+ (calendar.get(Calendar.MONTH)+1) + "月"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "日 "
				+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE);
		return endDateTime;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (progress <= LoadedList.getInstance().loadedSize()) {
			ShipDynamicFragment.currPlayPosition = progress;
			if(!isPlaying){
				onPauseState();
			}
		} else {
			ShipDynamicFragment.currPlayPosition = LoadedList.getInstance()
					.loadedSize();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// videoView.pause();
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// videoView.seekTo(videoSeekBar.getProgress());
		// videoView.start();
		// videoPlayImg.setVisibility(View.INVISIBLE);
		// videoPauseImg.setImageResource(R.drawable.icon_video_pause);
	}

	public void startCacheSeekBarTimer() {
		videoSeekBar.setMax(LoadedList.needTotalSize);
		// LoadedList.getInstance().setCachePreplayListener(this);

		CacheSeekBarTimer.getInstance().setHandler(seekBarHandler);
		CacheSeekBarTimer.getInstance().startCacheSeekBarTimer();

		PlayListenerTimer.getInstance().setHandler(seekBarHandler);
		PlayListenerTimer.getInstance().startPlayListenerTimer();

	}


//	@Override
//	public void noPlayedPlay() {
//		if (!startedPlayNotifyed) {
//			startedPlayNotifyed = true;
//			seekBarHandler.sendEmptyMessage(noPlayedPlay);
//		}
//	}
//
//	@Override
//	public void playedPlay() {
//		if (!isPlaying) {
//			isPlaying = true;
//			seekBarHandler.sendEmptyMessage(playedPlay);
//
//		}
//	}
//
//	@Override
//	public void cache() {
//		if (isPlaying) {
//			isPlaying = false;
//			seekBarHandler.sendEmptyMessage(cache);
//
//		}
//
//	}

	// public void setPlayHandler(Handler playHandler) {
	// this.playHandler = playHandler;
	// }

}
