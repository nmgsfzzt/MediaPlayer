package com.example.zzt.mediaplayer.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.zzt.mediaplayer.R;
import com.example.zzt.mediaplayer.bean.VideoItem;
import com.example.zzt.mediaplayer.utils.Utils;

import static com.example.zzt.mediaplayer.R.id.tv_duration;
import static com.example.zzt.mediaplayer.R.id.tv_size;
import static com.example.zzt.mediaplayer.R.id.tv_title;

/**
 * Created by zzt on 2016/12/19.
 */

public class VideoListAdapter extends CursorAdapter {
    public VideoListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //1.创建一个itemView;
        View itemView = View.inflate(context, R.layout.item_video,null);
        //2.创建一个ViewHolder,保存itemView中控件的引用
        ViewHolder holder = new ViewHolder();
        holder.tv_title = (TextView) itemView.findViewById(tv_title);
        holder.tv_size = (TextView) itemView.findViewById(tv_size);
        holder.tv_duration = (TextView) itemView.findViewById(tv_duration);
        //3.把viewHolder保存到itemView中;
        itemView.setTag(holder);

        return itemView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //1.取出Holder
        ViewHolder holder = (ViewHolder) view.getTag();
        //把cursor转换为javabean
        VideoItem videoItem = VideoItem.fromCursor(cursor);
        //2.把数据显示到Holder中的控件中
        holder.tv_title.setText(videoItem.title);
        holder.tv_size.setText(Formatter.formatFileSize(context,videoItem.size));
        holder.tv_duration.setText(Utils.formatMillis(videoItem.duration));
    }

    class ViewHolder{
        TextView tv_title;
        TextView tv_size;
        TextView tv_duration;


    }
}
