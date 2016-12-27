package com.example.zzt.mediaplayer.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.example.zzt.mediaplayer.R;

/**
 * Created by zzt on 2016/12/18.
 */

public class SplashActivity extends BaseActivity {
    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_splash;
    }
    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

        //一秒钟跳转到首页
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(context,MainActivity.class));
                finish();
            }
        },2000);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

    }
}
