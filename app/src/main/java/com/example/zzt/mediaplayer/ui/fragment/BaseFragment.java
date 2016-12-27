package com.example.zzt.mediaplayer.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zzt.mediaplayer.ui.UiOpration;
import com.example.zzt.mediaplayer.utils.Utils;

/**
 * Created by zzt on 2016/12/18.
 */

public abstract  class BaseFragment extends Fragment implements UiOpration {
    public Context context;
    private ViewGroup mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getActivity();//给context赋值,获取到activity中的context
        mRootView = (ViewGroup) inflater.inflate(getContentViewLayoutId(), null);
        Utils.setButtonOnClickListener(mRootView, this);
        initView();
        initListener();
        initData();
        return mRootView;
    }
    public<T> T findView(int id){
        T view = (T)mRootView.findViewById(id);
        return view;
    }

}
