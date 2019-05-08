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

public class CommonVideoView2 extends FrameLayout implements
		View.OnClickListener, SeekBar.OnSeekBarChangeListener,
		DateTimePickDialogUtil.OnDateSetListener {

	public final static int UPDATE_PLAY_SEEKBAR = 2;
	public final static int UPDATE_CACHE_SEEKBAR = 3;
	public final static int noPlayedPlay = 4;
	public final static int playedPlay = 5;
	public final static int cache = 6;

	private Context context;
	private LinearLayout videoPauseBtn;
	private LinearLayout screenSwitchBtn;
	private LinearLayout touchStatusView;
	private LinearLayout videoControllerLayout;
	private ImageView screen_status_img;
	private ImageView touchStatusImg;
	private ImageView videoPauseImg;
	private TextView touchStatusTime;
	private TextView videoCurTimeText;
	private TextView videoTotalTimeText;
	private SeekBar videoSeekBar;

	private ProgressBar progressBar;

	private int duration;
	private String formatTotalTime;

	private float touchLastX;
	// 定义用seekBar当前的位置，触摸快进的时候显示时间
	private int position;
	private int touchStep = 1000;// 快进的时间，1秒
	private int touchPosition = -1;

	private boolean videoControllerShow = true;// 底部状态栏的显示状态
	private boolean animation = false;

	public static boolean startedPlay = false;
	public static boolean isPlaying = false;
	public static boolean handerPause = false;
	public static boolean startedPlayNotifyed = false;
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
						isPlaying=false;
						ShipDynamicFragment.currPlayPosition=0;
						onCompletion();
					}
				}
				break;
			case noPlayedPlay:
				if (!startedPlayNotifyed) {
					startedPlayNotifyed = true;
					onPlay(0);
				}
				break;
			case playedPlay:
				if (!isPlaying) {
					if(!handerPause){
						isPlaying = true;
						onPlay(PauseAfterPlay);
					}
				}
				// threadStartPlay();
				break;
			case cache:
				if (isPlaying) {
					isPlaying = false;
					onCache();
				}
				// threadPausePlay();
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
	}
	public void setCommonVideoChangLintener(CommonVideoChangLintener commonVideoChangLintener){
		this.commonVideoChangLintener = commonVideoChangLintener;
	}

	public CommonVideoView2(Context context) {
		this(context, null);
	}

	public CommonVideoView2(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CommonVideoView2(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}

	public void init(Calendar startc, Calendar endc) {
		startTime.setText(initStartDateTime(startc));
		endTime.setText(initStartDateTime(endc));

		onCache();
	}

	private void onCache() {
		progressBar.setVisibility(View.VISIBLE);
		videoPauseImg.setVisibility(View.GONE);
		videoSeekBar.setEnabled(false);
	}

	public void onPause() {
		progressBar.setVisibility(View.GONE);
		videoPauseImg.setVisibility(View.VISIBLE);
		videoSeekBar.setEnabled(true);
	}

	public void onPlay(int progress) {
		progressBar.setVisibility(View.GONE);
		videoPauseImg.setVisibility(View.VISIBLE);
		videoSeekBar.setEnabled(true);
		if (progress == PauseAfterPlay) {
			videoPauseImg.setImageResource(R.drawable.icon_video_pause);
			// videoSeekBar.setProgress(LoadedList.getInstance().getCurrPlayPosition());
		} else {
			videoPauseImg.setImageResource(R.drawable.icon_video_play);
			// videoSeekBar.setProgress(progress);
		}
	}

	public void onCompletion() {
		videoSeekBar.setProgress(0);
		videoPauseImg.setImageResource(R.drawable.icon_video_play);
		videoPauseImg.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
	}

	public void setFullScreen() {
		touchStatusImg.setImageResource(R.drawable.iconfont_exit);
		this.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		// videoView.requestLayout();
	}

	public void setNormalScreen() {
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
		CommonVideoView2.startedPlay=false;
		CommonVideoView2.isPlaying=false;
		CommonVideoView2.handerPause=false;
		CommonVideoView2.startedPlayNotifyed=false;
		onCache();
		videoSeekBar.setSecondaryProgress(0);
		videoSeekBar.setProgress(0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.videoPauseImg:
			if (isPlaying) {
				videoPauseImg.setImageResource(R.drawable.icon_video_play);
				videoPauseImg.setVisibility(View.VISIBLE);
				isPlaying = false;
				handerPause = true;
				// threadPausePlay();
			} else {
				videoPauseImg.setVisibility(View.VISIBLE);
				videoPauseImg.setImageResource(R.drawable.icon_video_pause);
				startedPlay = true;
				isPlaying = true;
				handerPause = false;
				// threadStartPlay();
			}
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
			if(commonVideoChangLintener!=null)
				commonVideoChangLintener.fastChang();
			break;
		case R.id.mod:
			if(commonVideoChangLintener!=null)
				commonVideoChangLintener.modChang();
			break;
		case R.id.slo:
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
		// int[] time = getMinuteAndSecond(progress);
		// videoCurTimeText.setText(String.format("%02d:%02d", time[0],
		// time[1]));
		if (progress <= LoadedList.getInstance().loadedSize()) {
			// videoSeekBar.setProgress(progress);
			ShipDynamicFragment.currPlayPosition = progress;
			// if(LoadedList.getInstance().loadedSize()-progress>=LoadedList.preLoadedPlay){
			// onPlay(progress);
			// threadPausePlay();
			// threadStartPlay();
			// }else{
			// onCache();
			// threadPausePlay();
			// }
		} else {
			// videoSeekBar.setProgress(LoadedList.getInstance().loadedSize());
			ShipDynamicFragment.currPlayPosition = LoadedList.getInstance()
					.loadedSize();
			// onCache();
			// threadPausePlay();
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
