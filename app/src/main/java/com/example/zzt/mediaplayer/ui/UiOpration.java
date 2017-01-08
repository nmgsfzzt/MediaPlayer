package com.example.zzt.mediaplayer.ui;

import android.view.View;

/**Ui操作接口
 * 这个接口就是让baseActivity和BaseFragmenga来实现的,
 * 因为他俩都有相同的方法,为了避免重复写相同的代码所以就写了一个这个接口
 * Created by zzt on 2016/12/18.
 */

public interface UiOpration extends View.OnClickListener{
    /**
     * 创建一个抽象的方法
     * 返回一个布局id用于返回一个正常界面
     * @return
     */
     int getContentViewLayoutId();

    /*** 初始化控件*/
    void initView();

    /*** 初始化监听器*/
     void initListener();

    /*** 初始化数据*/
    void initData();
}
