package com.example.zzt.mediaplayer.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.example.zzt.mediaplayer.ui.UiOpration;
import com.example.zzt.mediaplayer.utils.Utils;

/**
 * 专门给其他的类来继承的
 * Created by zzt on 2016/12/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements UiOpration {

    protected Context context; //提供一个变量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayoutId());
        context = this;
        //用于查找到当前Activity的根view
        ViewGroup viewGrop = (ViewGroup) findViewById(android.R.id.content);
        Utils.setButtonOnClickListener(viewGrop, this);
        initView();
        initListener();
        initData();
    }
    public<T> T findView(int id){
        T view = (T)findViewById(id);
        return view;
    }


}
