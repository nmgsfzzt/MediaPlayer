package com.example.zzt.mediaplayer.ui.fragment;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zzt.mediaplayer.R;
import com.example.zzt.mediaplayer.adapter.VideoListAdapter;
import com.example.zzt.mediaplayer.bean.VideoItem;
import com.example.zzt.mediaplayer.interfaces.Keys;
import com.example.zzt.mediaplayer.ui.activity.VideoPlayerActivity;

import java.util.ArrayList;

/**
 * Created by zzt on 2016/12/18.
 */

public class VideoFragment extends BaseFragment {


    private ListView mListView;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    public void initView() {
        mListView = findView(R.id.lv_video_item);


    }



    @Override
    public void initListener() {

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1.取出视频列表
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                ArrayList<VideoItem> item = getViewItem(cursor);
                //2.把视频列表和点击的按钮放到intent中
                Intent intent = new Intent(context,VideoPlayerActivity.class);
                intent.putExtra(Keys.ITEMS,item);
                intent.putExtra(Keys.CURRENT_POSITION,position);
           //3.跳转到视频播放界面
                startActivity(intent);
            }
        });
    }

    /**
     * 把cursor中所有的数据封装到集合
     * @param cursor
     * @return
     */
    private ArrayList<VideoItem> getViewItem(Cursor cursor) {
        ArrayList<VideoItem> item = new ArrayList<>();
        cursor.moveToFirst(); //把cursor的游标移动到第一条
        do {
            VideoItem videoItem = VideoItem.fromCursor(cursor);
            item.add(videoItem);
        }while (cursor.moveToNext());

        return item;
    }

    @Override
    public void initData() {
        //通过内容提供或者来遍历手机中的多媒体文件,如果查询的数据量较大就会造成anr异常
        //context.getContentResolver().query();
        //在这里用另一个类,这个类就相当于Handler,只不过里面封装了查询的方法
        AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler(context.getContentResolver()) {
            //当查询结束后会调用这个方法;这个方法会运行在主线程中
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                //super.onQueryComplete(token, cookie, cursor);
                //Utils.printCursor(cursor);
                mListView.setAdapter(new VideoListAdapter(context,cursor));
            }
        };

        int token = 0; //AsyncQueryHandler继承了handler,这个参数类似于Message里面的what,是区分发送什么样的消息
        Object cookie = null; //类似于Message.object, 消息对象
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI; //定查询的地方,这里是外部存储设备
        String[] projection = {  //查询表中的列
                MediaStore.Video.Media._ID,  //地址
                MediaStore.Video.Media.TITLE,  //标题
                MediaStore.Video.Media.DURATION, //时长
                MediaStore.Video.Media.SIZE,       //大小
                MediaStore.Video.Media.DATA};      //文件的路径,这里不是用的Path
        String selection = null;                 //查询的条件,这里全部都要查询,传null就可以了;
        String[] selectionArgs = null;           //查询条件中的参数
        String orderBy = MediaStore.Video.Media.TITLE + " ASC"; //按标题排序(升序)
        // 这个方法会运行在子线程,并且把查询的结果返回给onQueryComplete()方法;
        asyncQueryHandler.startQuery(token,cookie,uri,projection,selection,selectionArgs,orderBy);




    }

    @Override
    public void onClick(View v) {

    }
}
