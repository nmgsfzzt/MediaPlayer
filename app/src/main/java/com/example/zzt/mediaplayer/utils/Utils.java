package com.example.zzt.mediaplayer.utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by zzt on 2016/12/17.
 */
public class Utils {
    private static String Tag = Utils.class.getSimpleName();

    /**
     * 遍历当前的activity中的所有的按钮和ImageBUtton和注册监听器
     *
     * @param viewGrop
     * @param listener
     */
    public static void setButtonOnClickListener(ViewGroup viewGrop, View.OnClickListener listener) {

        //遍历容器中的所有的view通过for循环
        for (int i = 0; i < viewGrop.getChildCount(); i++) {
            //拿到当前界面中所有的控件
            View childAt = viewGrop.getChildAt(i);
            //如果界面中的控件是button或者ImageBUttong就注册监听事件
            if (childAt instanceof Button || childAt instanceof ImageButton) {
                childAt.setOnClickListener(listener);
                //如果控件是一个容器,就接着运用递归遍历容器里面的控件
            } else if (childAt instanceof ViewGroup) {
                setButtonOnClickListener((ViewGroup) childAt, listener);
            }
        }

    }


    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        return screenWidth;
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        return screenHeight;
    }

    /**
     * 打印cursor中所有的数据
     *
     * @param cursor
     */
    public static void printCursor(Cursor cursor) {
        if (cursor == null) {
            return;
        }
        //遍历cursor
        Log.i(Tag, "总共有" + cursor.getCount() + "====列====");
        while (cursor.moveToNext()) {
            Log.i(Tag, "=====================");
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String columnName = cursor.getColumnName(i);//遍历所有的列名字
                String cursorString = cursor.getString(i);  //列的值
                Log.i(Tag, columnName + "=" + cursorString);
            }
        }
    }

    /**
     * 格式化时间,把毫秒值转化为 时:分:秒 或者 分:秒 当毫秒值达到小时的时候就格式化为 时分秒 达不到一小时就格式化 分秒
     * @param duration
     * @return
     */
    public static CharSequence formatMillis(long duration) {
        //这个已经创建好了时间了,是以收集的当前事件
        Calendar calendar =Calendar.getInstance();
        calendar.clear(); //清除这个时间就会保留的时间是1970年1月1日
        calendar.add(Calendar.MILLISECOND, (int) duration);
        long oneHours = 1 * 60 * 60 *  1000;
        boolean hasHours = duration / oneHours > 0; //当这个毫秒值除以1小时大于0
        CharSequence inFormat = hasHours? "kk:mm:ss" : "mm:ss" ;
        return android.text.format.DateFormat.format(inFormat,calendar);
    }

    public static void showToast(Context context, String text) {
        Toast makeText = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        //把吐司设置在屏幕的中间
        makeText.setGravity(Gravity.CENTER,0,0);
        makeText.show();

    }
}
