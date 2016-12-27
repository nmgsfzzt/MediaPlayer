package com.example.zzt.mediaplayer.bean;

import android.database.Cursor;
import android.provider.MediaStore;

import java.io.Serializable;

/**
 * Created by zzt on 2016/12/19.
 */
public class VideoItem implements Serializable{
    public String _id;
    public String title;
    public long size;
    public long duration;
    public String data;

    public static VideoItem fromCursor(Cursor cursor) {
        VideoItem videoItem = new VideoItem();
        videoItem._id = cursor.getString(0);
        videoItem.title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
        videoItem.duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
        videoItem.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
        videoItem.data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        return videoItem;
    }
}
