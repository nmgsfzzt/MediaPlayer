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
import android.support.v4.view.ViewCompat;
import android.text.format.DateFormat;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
    private BroadcastReceiver mBatteryChangerRecriver;
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
    private int mCurrentVolume;
    private BroadcastReceiver mSoundChangedReceiver;

    private GestureDetector mGestureDetector;
    private View mScreen_bright;
    private float mResultAlpha;


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
        mScreen_bright = findView(R.id.screen_bright);  //屏幕亮度的调节


        //注册一个电池电量改变的广播接收者
        registerBatteryChangReceiver();
        //注册一个音量改变的广播接收者
        registerSoundChangedReceiver();
        //显示系统时间
        systemTimeShow();
        //初始化音量
        initSound();


    }

    /**
     * 注册一个音量改变的广播接收者
     */
    private void registerSoundChangedReceiver() {
        mSoundChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int currentVolume = getStreamVolume();
                mSb_voice_progress.setProgress(currentVolume);
            }
        };
        //拦截系统音量的广播
        IntentFilter filter = new IntentFilter("android.media.STREAM_DEVICES_CHANGED_ACTION");

        registerReceiver(mSoundChangedReceiver,filter);
    }

    /**
     *初始化音量
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
        mBatteryChangerRecriver = new BroadcastReceiver() {
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
        registerReceiver(mBatteryChangerRecriver,filter);

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
    public void initListener() {

        mVideoView.setOnPreparedListener(mOnPreparedListener);

        mSb_voice_progress.setOnSeekBarChangeListener(audioOnSeekBarChangeListener);


        mGestureDetector = new GestureDetector(this,mGestureListener);

    }

    @Override
    public void initData() {
        mVideoItems = (ArrayList<VideoItem>) getIntent().getSerializableExtra(Keys.ITEMS);
        mCurrentPosition = getIntent().getIntExtra(Keys.CURRENT_POSITION,-1);
        if (mVideoItems == null || mVideoItems.isEmpty() || mCurrentPosition == -1) {
            return;
            ///
        }
        mCurrentVideoItem = mVideoItems.get(mCurrentPosition);
        //把视频路径设置给VideoView,要想播放还要调用start方法
        mVideoView.setVideoPath(mCurrentVideoItem.data);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_voice: //点击了静音的按钮
                Utils.showToast(context, "点击了音量");
                muteToggle();
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
     * 触摸事件,在这里设置 双击屏幕显示全屏,滑动屏幕更改屏幕的亮度和音量大小;
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //1.要识别手势的滑动就会用到GestureDetector


        mGestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);

    }

    /**静音的的开关*/
    private void muteToggle() {
        //判断,如果有音量值就静音
        if (getStreamVolume() > 0) {
            //在静音之前先保存当前的音量值,方便下一次回复
            mCurrentVolume = getStreamVolume();
            setStreamSound(0);
            mSb_voice_progress.setProgress(0); //进度条也跟着显示
        }else{
            setStreamSound(mCurrentVolume);
            mSb_voice_progress.setProgress(mCurrentVolume);
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
        //第三个参数用于显示系统音量面板,1的话就显示系统弹出里的音量面板,0的话就不显示
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,1);
    }

    /**
     * 手势识别监听器
     */
    GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener(){

        private float mCurrentAlpha;
        private boolean mLeftPress;

        @Override //滑动
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {


            //2.计算滑动距离
            float moveDistence = e1.getY() - e2.getY();

            if (mLeftPress ) {
                //更改屏幕的亮度 ,为什么是负值呢?因为当屏幕向上滑动的术后,透明度的值越小,所以就要取反
                changeScreenBrightByMoveDistence(-moveDistence);
            }else{
                 //改变音量
                changeVomuleByMoveDistence(moveDistence);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);

        }

        /**
         * 通过滑动的距离改变音量值
         * @param moveDistence
         */
        private void changeVomuleByMoveDistence(float moveDistence) {
            //3.计算滑动的比例,屏幕的宽度与音量的最大值的比例;
            float moveScale = (float)mMaxVolume / Utils.getScreenHeight(VideoPlayerActivity.this);
            //4.计算滑动距离对应的音量值
            int moveVolume = (int) (moveScale * moveDistence);

            //5.在原有音量的基础上加上滑动屏幕是对应的音量值
            int resultVolume = mCurrentVolume + moveVolume;
            //6.限制音量值:以防音量在最大值的时候滑动屏幕时超出最大值的范围;
            if (resultVolume > mMaxVolume) {
                resultVolume = mMaxVolume;
            }else if (resultVolume < 0) {
                resultVolume = 0;
            }
            //7.把设置好的音量值设置给SeekBar控件显示
            mSb_voice_progress.setProgress(resultVolume);
            setStreamSound(resultVolume);
        }

        /**
         * 通过滑动的距离改变屏幕亮度
         * @param moveDistence
         */
        private void changeScreenBrightByMoveDistence(float moveDistence) {
            //3.计算滑动的比例,屏幕的宽度与音量的最大值的比例;
            float moveScale = 0.1f / Utils.getScreenHeight(VideoPlayerActivity.this);
            //4.计算滑动距离对应的音量值
            float moveAlpha =  (moveScale * moveDistence);

            //5.在原有透明度的基础上加上滑动屏幕是对应的透明度;
            mResultAlpha = mCurrentAlpha + moveAlpha;
            //6.限制音量值:以防音量在最大值的时候滑动屏幕时超出最大值的范围;
            if (mResultAlpha > 0.8f) {
                mResultAlpha = 0.8f;
            }else if (mResultAlpha < 0) {
                mResultAlpha = 0;
            }
            //7.把设置好的音量值iew
            ViewCompat.setAlpha(mScreen_bright,mResultAlpha);

        }


        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            //按下的时候获取一下当前的当前的音量值
            mCurrentVolume = getStreamVolume();
            // 按下的时候获取一下当前的当前的透明度
            mCurrentAlpha = ViewCompat.getAlpha( mScreen_bright);
            //左边按下
            mLeftPress = e.getX() < Utils.getScreenWidth(context) / 2;

            return super.onDown(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册
        unregisterReceiver(mBatteryChangerRecriver);
        mHandler.removeCallbacksAndMessages(null);
        unregisterReceiver(mSoundChangedReceiver);
    }
}
