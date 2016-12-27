package com.example.zzt.mediaplayer.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zzt.mediaplayer.R;
import com.example.zzt.mediaplayer.adapter.MainAdapter;
import com.example.zzt.mediaplayer.ui.fragment.AudioFragment;
import com.example.zzt.mediaplayer.ui.fragment.VideoFragment;
import com.example.zzt.mediaplayer.utils.Utils;

import java.util.ArrayList;

import static com.example.zzt.mediaplayer.R.id.tv_video;

public class MainActivity extends BaseActivity {


    private TextView mTv_audio;
    private TextView mTv_video;
    private View mIndicator;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments;
    private int mIndicatorWidth; //指针控件的宽度
    private TextView mTv_video1;


    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mTv_audio = findView(R.id.tv_audio);
        mTv_video = findView(tv_video);
        mIndicator = findView(R.id.indicator);
        mViewPager = findView(R.id.vp_viewPager);

    }

    @Override
    public void initListener() {
        mTv_audio.setOnClickListener(this);
        mTv_video.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            //监听ViewPager滑动
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                scrollIndicator(position, positionOffset);
            }

            //当ViewPager选中后有什么样的变化
            @Override
            public void onPageSelected(int position) {
                //super.onPageSelected(position);
                //改变标题的状态,字体的颜色变大,字体的颜色变绿
                changeTitleState(position == 0);

            }
        });
        //手动调用
        changeTitleState(true);
    }

    /**
     * 选择了视频
     * @param selectVideo
     */
    private void changeTitleState(boolean selectVideo) {
        mTv_video.setSelected(selectVideo);
        mTv_audio.setSelected(!selectVideo);

        //缩放字体

        scaleView(mTv_video,selectVideo ? 1.2f : 1.0f);
        scaleView(mTv_audio,!selectVideo ? 1.2f : 1.0f);
    }

    /**
     * 缩放一个view
     * @param scale
     * @param view
     */
    private void scaleView(TextView view,float scale) {
        ViewCompat.animate(view).scaleX(scale).scaleY(scale);
    }

    /**
     * 根据ViewPager滑动的指针控件
     *
     * @param position
     * @param positionOffset
     */
    private void scrollIndicator(int position, float positionOffset) {
        float translationX = mIndicatorWidth * position + mIndicatorWidth * positionOffset;
         ViewCompat.setTranslationX(mIndicator, translationX);
    }

    @Override
    public void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(new VideoFragment());
        mFragments.add(new AudioFragment());
        mViewPager.setAdapter(new MainAdapter(getSupportFragmentManager(), mFragments));

        //初始化指针控件
        initIndicator();
    }

    /**
     * 初始化指针控件
     */
    private void initIndicator() {
        int screenWidth = Utils.getScreenWidth(context);
        //设置指针控件的宽度
        mIndicatorWidth = screenWidth / mFragments.size();
        mIndicator.getLayoutParams().width = mIndicatorWidth;
        //设置完宽度后要告诉系统更新布局
        mIndicator.requestLayout();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case tv_video: //点击了视频
                mViewPager.setCurrentItem(0);
                Toast.makeText(context, "点击了视频", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_audio: //点击了音频
                mViewPager.setCurrentItem(1);
                Toast.makeText(context, "点击了音频", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
