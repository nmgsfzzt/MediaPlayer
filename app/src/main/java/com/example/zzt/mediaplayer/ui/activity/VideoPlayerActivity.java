package com.example.zzt.mediaplayer.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.zzt.mediaplayer.R;
import com.example.zzt.mediaplayer.bean.VideoItem;
import com.example.zzt.mediaplayer.interfaces.Keys;
import com.example.zzt.mediaplayer.utils.Utils;

import java.util.ArrayList;

/**
 * Created by zzt on 2016/12/19.
 */
public class VideoPlayerActivity extends BaseActivity {


    private TextView mTv_title;
    private TextView mTv_video;
    private TextView mTv_duration;
    private TextView mTv_size;
    private TextView mTv_audio;
    private TextView mTv_system_time;
    private TextView mTv_current_position;
    private ImageView mIv_battery;
    private SeekBar mSb_video_progress;
    private SeekBar mSb_voice_progress;
    private Button mBtn_full_screen;
    private Button mBtn_play;
    private ArrayList<VideoItem> mVideoItems;
    private int mCurrentPosition;
    private VideoView mVideoView;
    private VideoItem mCurrentVideoItem;
    private BroadcastReceiver mMBatteryChangerRecriver;
    private static final int UPDATE_SYSTEM_TIME_SHOE = 0;
    private Handler mHandler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_SYSTEM_TIME_SHOE:
                    systemTimeShow();  //更新系统的时间
                    break;
            }
        }
    };
    private AudioManager mAudioManager;
    private int mMaxVolume;
    private int mVolume;


    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_videoplayer;
    }

    @Override
    public void initView() {
        mVideoView = findView(R.id.video_view);
        mTv_title = findView(R.id.tv_title);
        mTv_video = findView(R.id.tv_video);
        mTv_duration = findView(R.id.tv_duration);
        mTv_size = findView(R.id.tv_size);
        mTv_audio = findView(R.id.tv_audio);
        mTv_system_time = findView(R.id.tv_system_time);
        mTv_current_position = findView(R.id.tv_current_position);
        mIv_battery = findView(R.id.iv_battery);
        mSb_video_progress = findView(R.id.sb_video_progress);
        mSb_voice_progress = findView(R.id.sb_voice_progress);
        mBtn_full_screen = findView(R.id.btn_full_screen);
        mBtn_play = findView(R.id.btn_play);
        //
        registerBatteryChangReceiver();
        //显示系统时间
        systemTimeShow();
        //初始化音量
        initSound();


    }

    /**
     *
     */
    private void initSound() {
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  //获取系统中的音量最大值
        mVolume = getStreamVolume();       //获取系统中的当前音量
        mSb_voice_progress.setMax(mMaxVolume);    //把音量最大值显示给view
        mSb_voice_progress.setProgress(mVolume);  //设置当前的音量
    }

    /**获取当前的音量值*/
    private int getStreamVolume() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }


    /**
     * 显示系统时间
     */
    private void systemTimeShow() {
        //这个只是显示系统的时间啊,要想与系统时间同步还得运用handler
        mTv_system_time.setText(DateFormat.format("kk:mm:ss",System.currentTimeMillis()));
        mHandler.sendEmptyMessageAtTime(UPDATE_SYSTEM_TIME_SHOE,1000);
    }

    /**
     * 注册一个广播接收者来接收系统发送过来的电池电量的广播
     */
    private void registerBatteryChangReceiver() {
        mMBatteryChangerRecriver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //取出系统当前的电量
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                //更新电池图标的背景
                updataBatteryImageViewBackground(level);
            }
        };
        //接收电量改变的广播
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mMBatteryChangerRecriver,filter);

    }

    /**
     * 更新电池图标的背景
     * @param level
     */
    public void updataBatteryImageViewBackground(int level) {
        System.out.println("当前电池电量为:" + level);
        int resId;
        if (level == 0) {
            resId = R.drawable.ic_battery_0;
        } else if (level <= 10) {
            resId = R.drawable.ic_battery_10;
        } else if (level <= 20) {
            resId = R.drawable.ic_battery_20;
        } else if (level <= 40) {
            resId = R.drawable.ic_battery_40;
        } else if (level <= 60) {
            resId = R.drawable.ic_battery_60;
        } else if (level <= 80) {
            resId = R.drawable.ic_battery_80;
        } else {
            resId = R.drawable.ic_battery_100;
        }
        mIv_battery.setImageResource(resId);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册
        unregisterReceiver(mMBatteryChangerRecriver);
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void initListener() {

        mVideoView.setOnPreparedListener(mOnPreparedListener);

        mSb_voice_progress.setOnSeekBarChangeListener(audioOnSeekBarChangeListener);

    }

    @Override
    public void initData() {
        mVideoItems = (ArrayList<VideoItem>) getIntent().getSerializableExtra(Keys.ITEMS);
        mCurrentPosition = getIntent().getIntExtra(Keys.CURRENT_POSITION,-1);
        if (mVideoItems == null || mVideoItems.isEmpty() || mCurrentPosition == -1) {
            return;
        }
        mCurrentVideoItem = mVideoItems.get(mCurrentPosition);
        //把视频路径设置给VideoView,要想播放还要调用start方法
        mVideoView.setVideoPath(mCurrentVideoItem.data);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_voice: //点击了音量
                Utils.showToast(context, "点击了音量");
                break;
            case R.id.btn_exite: //点击了返回
                Utils.showToast(context, "点击了返回");
                break;
            case R.id.btn_last:  //点击了上一个
                Utils.showToast(context, "点击了上一首");
                break;
            case R.id.btn_play:  //点击了播放
                playToggle();
                Utils.showToast(context, "点击了播放");
                break;
            case R.id.btn_next:   //点击了下一首
                Utils.showToast(context, "点击了下一首");
                break;
            case R.id.btn_full_screen: //点击了全屏
                Utils.showToast(context, "点击了全屏");
                break;
        }
    }

    /**
     * 播放和暂停
     */
    private void playToggle() {
        if (mVideoView.isPlaying()) { //如果视屏正在播放
            mVideoView.pause();    //点击后就暂停
            mBtn_play.setBackgroundResource(R.drawable.selector_btn_play); //按钮的图标显示播放的样子
        }else{
            mVideoView.start();  //否则点击后就会播放;
            mBtn_play.setBackgroundResource(R.drawable.selector_btn_pause);
        }
    }

    /**
     * 视屏准备的监听器
     */
    MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        //视频准备好就播放视频
        @Override
        public void onPrepared(MediaPlayer mp) {
           // 播放视频
            mVideoView.start();
            //显示视频标题
            mTv_title.setText(mCurrentVideoItem.title);
            //给播放按钮设置图标
            mBtn_play.setBackgroundResource(R.drawable.selector_btn_pause);
        }
    };

    /**
     * 创建SeekBar改变的的监听()
     */
    SeekBar.OnSeekBarChangeListener audioOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        //fromUser:表示进度的改变是不是由用户触发的
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if (fromUser) { //如果是用户触发的,就改变音量的大小,
                //设置系统音量
                setStreamSound(progress);

            }
        }


        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    /**
     * 设置系统音量
     * @param progress
     */
    private void setStreamSound(int progress) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,1);
    }
}
